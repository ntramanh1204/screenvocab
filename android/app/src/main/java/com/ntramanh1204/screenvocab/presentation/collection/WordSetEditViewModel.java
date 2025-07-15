package com.ntramanh1204.screenvocab.presentation.collection;

import android.util.Log;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ntramanh1204.screenvocab.domain.model.Collection;
import com.ntramanh1204.screenvocab.domain.model.Word;
import com.ntramanh1204.screenvocab.domain.usecase.collection.DeleteCollectionUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.collection.GetCollectionDetailUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.collection.UpdateCollectionUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.word.CreateWordUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.word.DeleteWordUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.word.GetWordsByCollectionUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.word.UpdateWordUseCase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WordSetEditViewModel extends ViewModel {
    private final GetCollectionDetailUseCase getCollectionDetailUseCase;
    private final GetWordsByCollectionUseCase getWordsByCollectionUseCase;
    private final UpdateCollectionUseCase updateCollectionUseCase;
    private final DeleteCollectionUseCase deleteCollectionUseCase;
    private final CreateWordUseCase createWordUseCase;
    private final UpdateWordUseCase updateWordUseCase;
    private final DeleteWordUseCase deleteWordUseCase;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<Collection> collectionLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Word>> wordsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> saveSuccessLiveData = new MutableLiveData<>();
    public LiveData<Boolean> getSaveSuccess() { return saveSuccessLiveData; }

    private final MutableLiveData<Boolean> hasChangesLiveData = new MutableLiveData<>(false);
    public LiveData<Boolean> getHasChanges() { return hasChangesLiveData; }

    public WordSetEditViewModel(GetCollectionDetailUseCase getCollectionDetailUseCase,
                                GetWordsByCollectionUseCase getWordsByCollectionUseCase,
                                UpdateCollectionUseCase updateCollectionUseCase,
                                DeleteCollectionUseCase deleteCollectionUseCase,
                                CreateWordUseCase createWordUseCase,
                                UpdateWordUseCase updateWordUseCase,
                                DeleteWordUseCase deleteWordUseCase) {
        this.getCollectionDetailUseCase = getCollectionDetailUseCase;
        this.getWordsByCollectionUseCase = getWordsByCollectionUseCase;
        this.updateCollectionUseCase = updateCollectionUseCase;
        this.deleteCollectionUseCase = deleteCollectionUseCase;
        this.createWordUseCase = createWordUseCase;
        this.updateWordUseCase = updateWordUseCase;
        this.deleteWordUseCase = deleteWordUseCase;
    }

    public LiveData<Collection> getCollection() {
        return collectionLiveData;
    }

    public LiveData<List<Word>> getWords() {
        return wordsLiveData;
    }

    public LiveData<Boolean> getLoading() {
        return loadingLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }

    public void loadCollection(String collectionId) {
        loadingLiveData.setValue(true);
        disposables.add(
                getCollectionDetailUseCase.execute(collectionId)
                        .flatMap(collection ->
                                getWordsByCollectionUseCase.execute(collectionId)
                                        .map(words -> new Pair<>(collection, words))
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(pair -> {
                            collectionLiveData.setValue(pair.first);
                            wordsLiveData.setValue(pair.second);
                            loadingLiveData.setValue(false);
                            hasChangesLiveData.setValue(false); // Reset changes after loading
                        }, throwable -> {
                            errorLiveData.setValue(throwable.getMessage());
                            loadingLiveData.setValue(false);
                        })
        );
    }

    public void updateCollection(Collection collection) {
        Log.d("WordSetEditVM", "updateCollection() called with name = " + collection.getName());

        loadingLiveData.setValue(true);
        disposables.add(
                updateCollectionUseCase.execute(collection)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(updatedCollection -> {
                            Log.d("WordSetEditVM", "Collection updated successfully: " + updatedCollection.getName());
                            collectionLiveData.setValue(updatedCollection);
                            loadingLiveData.setValue(false);
                            saveSuccessLiveData.setValue(true);
                            hasChangesLiveData.setValue(false);
                        }, throwable -> {
                            Log.d("WordSetEditVM", "Collection update failed: " + throwable.getMessage());
                            errorLiveData.setValue(throwable.getMessage());
                            loadingLiveData.setValue(false);
                            saveSuccessLiveData.setValue(false);
                        })
        );
    }

    public void markChanged() {
        hasChangesLiveData.setValue(true);
    }

    public void markSaved() {
        hasChangesLiveData.setValue(false);
    }

    // THIS IS THE KEY METHOD - Handle word updates from adapter
    public void onWordUpdated(int position, Word updatedWord) {
        Log.d("WordSetEditVM", "onWordUpdated() called for position " + position + " with term: " + updatedWord.getTerm());

        List<Word> current = wordsLiveData.getValue();
        if (current == null || position >= current.size()) {
            Log.e("WordSetEditVM", "Invalid position or null word list");
            return;
        }

        List<Word> updated = new ArrayList<>(current);
        updated.set(position, updatedWord);
        wordsLiveData.setValue(updated);
        markChanged();
    }

    public void onWordDeleted(int position) {
        List<Word> current = wordsLiveData.getValue();
        if (current == null || position >= current.size()) return;

        Word wordToDelete = current.get(position);

        // Remove from UI first
        List<Word> updated = new ArrayList<>(current);
        updated.remove(position);
        wordsLiveData.setValue(updated);
        markChanged();

        // Delete from database if it's not a new word
        if (wordToDelete.getWordId() != null && !wordToDelete.getWordId().isEmpty()) {
            deleteWord(wordToDelete);
        }
    }

public void addEmptyWord() {
    List<Word> currentWords = wordsLiveData.getValue();
    if (currentWords == null) currentWords = new ArrayList<>();
    else currentWords = new ArrayList<>(currentWords);

    Collection currentCollection = collectionLiveData.getValue();
    if (currentCollection == null) {
        Log.e("WordSetEditVM", "Collection not loaded, cannot add word");
        errorLiveData.setValue("Collection not loaded");
        return;
    }

    String currentCollectionId = currentCollection.getCollectionId();
    Log.d("WordSetEditVM", "Adding empty word to collection: " + currentCollectionId);

    // Create a new word with NULL wordId (to indicate it's new)
    Word newWord = new Word(
            null, // Explicitly set wordId to null for new word
            "",   // Empty term
            "",   // Empty pronunciation
            "",   // Empty definition
            "en", // Default language
            currentWords.size(), // Position
            System.currentTimeMillis(),
            currentCollectionId
    );

    Log.d("WordSetEditVM", "Created new word: " + newWord.toString());
    currentWords.add(newWord);
    wordsLiveData.setValue(currentWords);
    markChanged();
}
    public void deleteWord(Word word) {
        if (word.getWordId() == null || word.getWordId().isEmpty()) {
            Log.d("WordSetEditVM", "Word has no ID, skipping database deletion");
            return;
        }

        loadingLiveData.setValue(true);
        disposables.add(
                deleteWordUseCase.execute(word)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Log.d("WordSetEditVM", "Word deleted successfully");
                            loadingLiveData.setValue(false);
                        }, throwable -> {
                            Log.e("WordSetEditVM", "Error deleting word: " + throwable.getMessage());
                            errorLiveData.setValue(throwable.getMessage());
                            loadingLiveData.setValue(false);
                        })
        );
    }

    public void saveAllWords(Collection updatedCollection) {
    Log.d("WordSetEditVM", "saveAllWords() called");

    List<Word> words = wordsLiveData.getValue();
    if (words == null) {
        Log.e("WordSetEditVM", "No words to save, proceeding to update collection");
        updateCollection(updatedCollection);
        return;
    }

    Collection originalCollection = collectionLiveData.getValue();
    if (originalCollection == null) {
        Log.e("WordSetEditVM", "Original collection is null");
        errorLiveData.setValue("Original collection is null");
        return;
    }

    Log.d("WordSetEditVM", "Will save " + words.size() + " words");

    // Filter out empty words and validate
    List<Word> validWords = new ArrayList<>();
    for (Word word : words) {
        // Skip empty words instead of throwing error
        if (word.getTerm() == null || word.getTerm().trim().isEmpty()) {
            Log.d("WordSetEditVM", "Skipping empty word");
            continue;
        }
        validWords.add(word);
        Log.d("WordSetEditVM", "Validated word: " + word.getTerm() + " [ID: " + (word.getWordId() == null ? "null" : word.getWordId()) + "]");
    }

    loadingLiveData.setValue(true);
    List<Completable> tasks = new ArrayList<>();

    for (Word word : validWords) {
        Log.d("WordSetEditVM", "Processing word: " + word.getTerm() + " [ID: " + (word.getWordId() == null ? "null" : word.getWordId()) + "]");
        
        // Check if word is new (wordId is null or empty) or existing
        if (word.getWordId() == null || word.getWordId().trim().isEmpty()) {
            Log.d("WordSetEditVM", "New word detected -> creating: " + word.getTerm());
            tasks.add(
                    createWordUseCase.execute(new CreateWordUseCase.Params(
                            word.getTerm().trim(),
                            word.getPronunciation() != null ? word.getPronunciation().trim() : "",
                            word.getDefinition() != null ? word.getDefinition().trim() : "",
                            word.getLanguage() != null ? word.getLanguage() : "en",
                            word.getCollectionId(),
                            originalCollection.getUserId()
                    )).ignoreElement()
            );
        } else {
            Log.d("WordSetEditVM", "Existing word -> updating: " + word.getTerm());
            tasks.add(
                    updateWordUseCase.execute(new UpdateWordUseCase.Params(word))
            );
        }
    }

    // Execute all tasks in parallel
    disposables.add(
            Completable.merge(tasks)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        Log.d("WordSetEditVM", "All words saved successfully, updating collection");
                        updateCollection(updatedCollection);
                    }, throwable -> {
                        Log.e("WordSetEditVM", "Error saving words: " + throwable.getMessage(), throwable);
                        errorLiveData.setValue("Error saving words: " + throwable.getMessage());
                        loadingLiveData.setValue(false);
                        saveSuccessLiveData.setValue(false);
                    })
    );
}
    public void deleteCollection(Runnable onSuccess) {
        loadingLiveData.setValue(true);
        disposables.add(
                deleteCollectionUseCase.execute(collectionLiveData.getValue())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            loadingLiveData.setValue(false);
                            onSuccess.run();
                        }, throwable -> {
                            errorLiveData.setValue(throwable.getMessage());
                            loadingLiveData.setValue(false);
                        })
        );
    }

    @Override
    protected void onCleared() {
        disposables.clear();
        super.onCleared();
    }
}