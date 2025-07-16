package com.ntramanh1204.screenvocab.presentation.collection;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ntramanh1204.screenvocab.core.utils.ValidationUtils;
import com.ntramanh1204.screenvocab.data.mapper.CollectionMapper;
import com.ntramanh1204.screenvocab.domain.model.Collection;
import com.ntramanh1204.screenvocab.domain.usecase.collection.CreateCollectionUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.collection.UpdateCollectionUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.word.CreateWordUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.word.UpdateWordUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.word.DeleteWordUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.word.GetWordsByCollectionUseCase;
import com.ntramanh1204.screenvocab.domain.repository.AuthRepository;
import com.ntramanh1204.screenvocab.presentation.model.WordItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CreateWordSetViewModel extends ViewModel {

    private final CreateCollectionUseCase createCollectionUseCase;
    private final UpdateCollectionUseCase updateCollectionUseCase;
    private final CreateWordUseCase createWordUseCase;
    private final UpdateWordUseCase updateWordUseCase;
    private final DeleteWordUseCase deleteWordUseCase;
    private final GetWordsByCollectionUseCase getWordsByCollectionUseCase;
    private final AuthRepository authRepository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    // Form data
    private final MutableLiveData<String> _title = new MutableLiveData<>("");
    public final LiveData<String> title = _title;

    private final MutableLiveData<String> _description = new MutableLiveData<>("");
    public final LiveData<String> description = _description;

    private final MutableLiveData<Boolean> _isPublic = new MutableLiveData<>(false); // Changed default to false for privacy
    public final LiveData<Boolean> isPublic = _isPublic;

    private final MutableLiveData<List<WordItem>> _words = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<WordItem>> words = _words;
//    private final MutableLiveData<List<WordItem>> words = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<WordItem>> getWords() {
        return words;
    }

    // UI states
    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public final LiveData<Boolean> isLoading = _isLoading;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public final LiveData<String> error = _error;

    private final MutableLiveData<Boolean> _isSaveSuccessful = new MutableLiveData<>(false);
    public final LiveData<Boolean> isSaveSuccessful = _isSaveSuccessful;

    // Form validation states
    private final MutableLiveData<Boolean> _isTitleValid = new MutableLiveData<>(true);
    public final LiveData<Boolean> isTitleValid = _isTitleValid;

    private final MutableLiveData<Integer> _validWordCount = new MutableLiveData<>(0);
    public final LiveData<Integer> validWordCount = _validWordCount;

    private Collection editingCollection = null;

    // Constants
    private static final int MIN_WORDS_REQUIRED = 2;
    private static final int MAX_WORDS_ALLOWED = 150;

    public CreateWordSetViewModel(
            CreateCollectionUseCase createCollectionUseCase,
            UpdateCollectionUseCase updateCollectionUseCase,
            CreateWordUseCase createWordUseCase,
            UpdateWordUseCase updateWordUseCase,
            DeleteWordUseCase deleteWordUseCase,
            GetWordsByCollectionUseCase getWordsByCollectionUseCase,
            AuthRepository authRepository) {
        this.createCollectionUseCase = createCollectionUseCase;
        this.updateCollectionUseCase = updateCollectionUseCase;
        this.createWordUseCase = createWordUseCase;
        this.updateWordUseCase = updateWordUseCase;
        this.deleteWordUseCase = deleteWordUseCase;
        this.getWordsByCollectionUseCase = getWordsByCollectionUseCase;
        this.authRepository = authRepository;

        initializeWithEmptyWords();
    }

    private void initializeWithEmptyWords() {
        List<WordItem> initialWords = new ArrayList<>();
        initialWords.add(new WordItem()); // First empty word
        initialWords.add(new WordItem()); // Second empty word
        _words.setValue(initialWords);
        updateValidWordCount();
    }

    public void setTitle(String title) {
        _title.setValue(title);
        validateTitle(title);
    }

    public void setDescription(String description) {
        _description.setValue(description);
    }

    public void setPublic(boolean isPublic) {
        _isPublic.setValue(isPublic);
    }

    public void addNewWord() {
        List<WordItem> currentWords = _words.getValue();
        if (currentWords != null && currentWords.size() < MAX_WORDS_ALLOWED) {
            List<WordItem> newList = new ArrayList<>(currentWords); // Tạo list mới!
            newList.add(new WordItem());
//            _words.setValue(newList); // Cập nhật bằng list mớis
            _words.setValue(new ArrayList<>(newList)); // Defensive copy
            updateValidWordCount();
        } else if (currentWords != null && currentWords.size() >= MAX_WORDS_ALLOWED) {
            _error.setValue("Maximum " + MAX_WORDS_ALLOWED + " words allowed per collection");
        }
    }

    public void updateWord(int position, String term, String pronunciation, String definition) {
        List<WordItem> currentWords = _words.getValue();
        if (currentWords != null && position >= 0 && position < currentWords.size()) {
            WordItem wordItem = currentWords.get(position);
            wordItem.setTerm(term);
            wordItem.setPronunciation(pronunciation);
            wordItem.setDefinition(definition);
            _words.setValue(currentWords);
            updateValidWordCount();
        }
    }

    public void removeWord(int position) {
        List<WordItem> currentWords = _words.getValue();
        if (currentWords != null && position >= 0 && position < currentWords.size() && currentWords.size() > 2) {
            currentWords.remove(position);
            _words.setValue(currentWords);
            updateValidWordCount();
        }
    }

    public void saveWordSet() {
        if (!validateForm()) {
            _isLoading.setValue(false);
            return;
        }

        _isLoading.setValue(true);
        _error.setValue(null);

        String currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            _error.setValue("User not authenticated");
            _isLoading.setValue(false);
            return;
        }

        CreateCollectionUseCase.Params params = new CreateCollectionUseCase.Params(
                _title.getValue().trim(),
                _description.getValue() != null ? _description.getValue().trim() : "",
                currentUserId
        );

        compositeDisposable.add(
                createCollectionUseCase.execute(params)
                        .flatMap(this::saveWordsToCollection)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                collection -> {
                                    _isLoading.setValue(false);
                                    _isSaveSuccessful.setValue(true);
                                },
                                throwable -> {
                                    _isLoading.setValue(false);
                                    _error.setValue("Failed to save: " + throwable.getMessage());
                                }
                        )
        );
    }

    private void validateTitle(String title) {
        boolean isValid = title != null && !title.trim().isEmpty();
        _isTitleValid.setValue(isValid);
    }

    private void updateValidWordCount() {
        List<WordItem> currentWords = _words.getValue();
        if (currentWords != null) {
            int count = (int) currentWords.stream()
                    .filter(WordItem::isValid)
                    .count();
            _validWordCount.setValue(count);
        }
    }

    private Single<Collection> saveWordsToCollection(Collection collection) {
        List<WordItem> validWords = getValidWords();
        if (ValidationUtils.isEmpty(collection.getCollectionId())) {
            return Single.error(new IllegalStateException("Collection ID is empty"));
        }
        String currentUserId = getCurrentUserId();
        return io.reactivex.rxjava3.core.Observable.fromIterable(validWords)
                .flatMapCompletable(wordItem -> {
                    CreateWordUseCase.Params wordParams = new CreateWordUseCase.Params(
                            wordItem.getTerm().trim(),
                            wordItem.getDefinition().trim(), // Đổi thứ tự để khớp REQ-VOCAB-001
                            wordItem.getPronunciation().trim(),
                            "en", // Ngôn ngữ mặc định
                            collection.getCollectionId().trim(),
                            currentUserId
                    );
                    return createWordUseCase.execute(wordParams).ignoreElement();
                })
                .andThen(Single.just(collection)); // Không xóa collection nếu thất bại
    }

    private List<WordItem> getValidWords() {
        List<WordItem> currentWords = _words.getValue();
        if (currentWords == null) {
            return new ArrayList<>();
        }

        return currentWords.stream()
                .filter(WordItem::isValid)
                .collect(Collectors.toList());
    }

    private boolean validateForm() {
        List<WordItem> currentWords = _words.getValue();
        if (currentWords == null || currentWords.isEmpty()) {
            _error.setValue("No words available");
            return false;
        }
        String currentTitle = _title.getValue();
        if (currentTitle == null || currentTitle.trim().isEmpty()) {
            _error.setValue("Title cannot be empty");
            return false;
        }
        List<WordItem> validWords = getValidWords();
        Log.d("ValidateForm", "Valid words count: " + validWords.size());
        if (validWords.size() < MIN_WORDS_REQUIRED) {
            _error.setValue("Please add at least " + MIN_WORDS_REQUIRED + " valid words (term and definition required)");
            return false;
        }
        if (validWords.size() > MAX_WORDS_ALLOWED) {
            _error.setValue("Maximum " + MAX_WORDS_ALLOWED + " words allowed");
            return false;
        }
        return true;
    }

    private String getCurrentUserId() {
        try {
            return authRepository.getCurrentUserId();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
