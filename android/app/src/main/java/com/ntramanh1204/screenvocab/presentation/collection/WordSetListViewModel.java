package com.ntramanh1204.screenvocab.presentation.collection;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ntramanh1204.screenvocab.domain.model.Collection;
import com.ntramanh1204.screenvocab.domain.usecase.collection.GetCollectionsByUserUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.collection.DeleteCollectionUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.collection.UpdateCollectionUseCase;
import com.ntramanh1204.screenvocab.domain.repository.AuthRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WordSetListViewModel extends ViewModel {
    private final GetCollectionsByUserUseCase getCollectionsByUserUseCase;
    private final DeleteCollectionUseCase deleteCollectionUseCase;
    private final UpdateCollectionUseCase updateCollectionUseCase;
    private final AuthRepository authRepository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MutableLiveData<List<Collection>> _collections = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<Collection>> collections = _collections;

    private final MutableLiveData<List<Collection>> _originalCollections = new MutableLiveData<>(new ArrayList<>());

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public final LiveData<Boolean> isLoading = _isLoading;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public final LiveData<String> error = _error;

    private final MutableLiveData<Boolean> _isEmpty = new MutableLiveData<>(true);
    public final LiveData<Boolean> isEmpty = _isEmpty;

    private final MutableLiveData<String> _searchQuery = new MutableLiveData<>("");
    public final LiveData<String> searchQuery = _searchQuery;

    public WordSetListViewModel(
            GetCollectionsByUserUseCase getCollectionsByUserUseCase,
            DeleteCollectionUseCase deleteCollectionUseCase,
            UpdateCollectionUseCase updateCollectionUseCase,
            AuthRepository authRepository) {
        this.getCollectionsByUserUseCase = getCollectionsByUserUseCase;
        this.deleteCollectionUseCase = deleteCollectionUseCase;
        this.updateCollectionUseCase = updateCollectionUseCase;
        this.authRepository = authRepository;
        loadCollections();
    }

    public void loadCollections() {
        _isLoading.setValue(true);
        _error.setValue(null);

        String currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            _error.setValue("User not authenticated");
            _isLoading.setValue(false);
            return;
        }

        compositeDisposable.add(
                getCollectionsByUserUseCase.execute(currentUserId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                collections -> {
                                    _isLoading.setValue(false);
                                    _originalCollections.setValue(collections);
                                    String currentQuery = _searchQuery.getValue();
                                    if (currentQuery != null && !currentQuery.trim().isEmpty()) {
                                        applySearchFilter(currentQuery, collections);
                                    } else {
                                        _collections.setValue(collections);
                                    }
                                    _isEmpty.setValue(collections.isEmpty());

                                    Log.d("ViewModel", "Loaded collections: " + collections.size());
                                    for (Collection c : collections) {
                                        Log.d("ViewModel", "Collection name: " + c.getName());
                                    }
                                },
                                throwable -> {
                                    _isLoading.setValue(false);
                                    _error.setValue("Failed to load word sets: " + throwable.getMessage());
                                }
                        )
        );
    }

    private void applySearchFilter(String query, List<Collection> collections) {
        List<Collection> filteredCollections = collections.stream()
                .filter(collection -> collection.getName().toLowerCase()
                        .contains(query.toLowerCase().trim()))
                .collect(Collectors.toList());
        _collections.setValue(filteredCollections);
        _isEmpty.setValue(filteredCollections.isEmpty());
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
