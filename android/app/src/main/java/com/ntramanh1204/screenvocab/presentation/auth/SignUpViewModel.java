package com.ntramanh1204.screenvocab.presentation.auth;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.ntramanh1204.screenvocab.data.local.entities.UserEntity;
import com.ntramanh1204.screenvocab.data.mapper.UserMapper;
import com.ntramanh1204.screenvocab.domain.model.User;
import com.ntramanh1204.screenvocab.domain.usecase.auth.SignUpUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.auth.CreateUserProfileUseCase;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SignUpViewModel extends ViewModel {
    private final SignUpUseCase signUpUseCase;
    private final CreateUserProfileUseCase createUserProfileUseCase;
    private final UserMapper userMapper;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<User> signUpSuccess = new MutableLiveData<>();
    private final MutableLiveData<Boolean> profileCreated = new MutableLiveData<>();

    public SignUpViewModel(SignUpUseCase signUpUseCase, CreateUserProfileUseCase createUserProfileUseCase, UserMapper userMapper) {
        this.signUpUseCase = signUpUseCase;
        this.createUserProfileUseCase = createUserProfileUseCase;
        this.userMapper = userMapper;
    }

    public MutableLiveData<Boolean> getIsLoading() { return isLoading; }
    public MutableLiveData<String> getError() { return error; }
    public MutableLiveData<User> getSignUpSuccess() { return signUpSuccess; }
    public MutableLiveData<Boolean> getProfileCreated() { return profileCreated; }

    public void signUp(String email, String password) {
        isLoading.setValue(true);
        error.setValue(null);
        Disposable disposable = signUpUseCase.execute(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user -> {
                            isLoading.setValue(false);
                            signUpSuccess.setValue(user);
                        },
                        throwable -> {
                            isLoading.setValue(false);
                            error.setValue(parseError(throwable));
                        }
                );
        compositeDisposable.add(disposable);
    }

    public void createUserProfile(User user, String displayName) {
        isLoading.setValue(true);
        error.setValue(null);
        UserEntity userEntity = userMapper.domainToEntity(user);
        userEntity.setDisplayName(displayName);
        userEntity.setEmail(user.getEmail());
        Disposable disposable = createUserProfileUseCase.execute(userEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            isLoading.setValue(false);
                            profileCreated.setValue(true);
                        },
                        throwable -> {
                            isLoading.setValue(false);
                            error.setValue("Error creating profile: " + throwable.getMessage());
                        }
                );
        compositeDisposable.add(disposable);
    }

    private String parseError(Throwable throwable) {
        if (throwable == null || throwable.getMessage() == null) {
            return "An unexpected error occurred";
        }
        String message = throwable.getMessage();
        if (message.contains("email address is already in use")) {
            return "Email is already registered";
        } else if (message.contains("invalid-email")) {
            return "Invalid email address";
        } else if (message.contains("weak-password")) {
            return "Password is too weak";
        } else {
            return "Sign up failed: " + message;
        }
    }

    public void clearError() {
        error.setValue(null);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}