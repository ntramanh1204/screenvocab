package com.ntramanh1204.screenvocab.presentation.wordset;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ntramanh1204.screenvocab.domain.model.Collection;
import com.ntramanh1204.screenvocab.domain.usecase.collection.GetCollectionsByUserUseCase;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChooseWordSetViewModel extends ViewModel {

    private final GetCollectionsByUserUseCase getCollectionsByUserUseCase;
    private final MutableLiveData<List<Collection>> collections = new MutableLiveData<>();
    private final MutableLiveData<Collection> selectedCollection = new MutableLiveData<>();
    private final CompositeDisposable disposables = new CompositeDisposable();

    public ChooseWordSetViewModel(GetCollectionsByUserUseCase useCase, String userId) {
        this.getCollectionsByUserUseCase = useCase;
        fetchCollections(userId);
    }

    private void fetchCollections(String userId) {
        disposables.add(
                getCollectionsByUserUseCase.execute(userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(collections::setValue, throwable -> {
                            // Optional: Log or show error
                        })
        );
    }

    public LiveData<List<Collection>> getCollections() {
        return collections;
    }

    public void selectCollection(Collection collection) {
        selectedCollection.setValue(collection);
    }

    public LiveData<Collection> getSelectedCollection() {
        return selectedCollection;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
