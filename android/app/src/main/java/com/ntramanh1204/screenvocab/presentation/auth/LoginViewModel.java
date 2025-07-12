package com.ntramanh1204.screenvocab.presentation.auth;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.ntramanh1204.screenvocab.domain.model.User;
import com.ntramanh1204.screenvocab.domain.usecase.auth.LoginUseCase;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginViewModel extends ViewModel {
    private final LoginUseCase loginUseCase;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<User> loginSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> passwordResetSuccess = new MutableLiveData<>();

    public LoginViewModel(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    public MutableLiveData<Boolean> getIsLoading() { return isLoading; }
    public MutableLiveData<String> getError() { return error; }
    public MutableLiveData<User> getLoginSuccess() { return loginSuccess; }
    public MutableLiveData<String> getPasswordResetSuccess() { return passwordResetSuccess; }

    public void login(String email, String password) {
        isLoading.setValue(true);
        error.setValue(null);
        Disposable disposable = loginUseCase.execute(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user -> {
                            isLoading.setValue(false);
                            loginSuccess.setValue(user);
                        },
                        throwable -> {
                            isLoading.setValue(false);
                            error.setValue(parseError(throwable));
                        }
                );
        compositeDisposable.add(disposable);
    }

    public void sendPasswordResetEmail(String email) {
        isLoading.setValue(true);
        error.setValue(null);
        Disposable disposable = loginUseCase.sendPasswordResetEmail(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            isLoading.setValue(false);
                            passwordResetSuccess.setValue("Password reset email sent successfully");
                        },
                        throwable -> {
                            isLoading.setValue(false);
                            error.setValue(parsePasswordResetError(throwable));
                        }
                );
        compositeDisposable.add(disposable);
    }

    private String parseError(Throwable throwable) {
        if (throwable == null || throwable.getMessage() == null) {
            return "An unexpected error occurred";
        }
        String message = throwable.getMessage();
        if (message.contains("There is no user record") || message.contains("user-not-found")) {
            return "No account found with this email address";
        } else if (message.contains("password is invalid") || message.contains("wrong-password")) {
            return "Incorrect password";
        } else if (message.contains("user-disabled")) {
            return "This account has been disabled";
        } else if (message.contains("too-many-requests")) {
            return "Too many failed attempts. Please try again later";
        } else if (message.contains("invalid-email") || message.contains("invalid-credential") ||
                message.contains("INVALID_LOGIN_CREDENTIALS")) {
            return "Invalid email or password";
        } else if (message.contains("network") || message.contains("timeout")) {
            return "Network error. Please check your connection";
        } else {
            return "Invalid email or password";
        }
    }

    private String parsePasswordResetError(Throwable throwable) {
        if (throwable == null || throwable.getMessage() == null) {
            return "Failed to send password reset email";
        }
        String message = throwable.getMessage();
        if (message.contains("user-not-found")) {
            return "No account found with this email address";
        } else if (message.contains("invalid-email")) {
            return "Invalid email address";
        } else {
            return "Failed to send password reset email";
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