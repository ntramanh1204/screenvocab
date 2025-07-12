package com.ntramanh1204.screenvocab.domain.usecase.auth;

import com.ntramanh1204.screenvocab.data.local.entities.UserEntity;
import com.ntramanh1204.screenvocab.domain.repository.UserRepository;
import io.reactivex.rxjava3.core.Completable;

public class CreateUserProfileUseCase {
    private final UserRepository userRepository;

    public CreateUserProfileUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Completable execute(UserEntity userEntity) {
        return userRepository.createUserProfile(userEntity);
    }
}