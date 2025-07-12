package com.ntramanh1204.screenvocab.data.mapper;

import com.google.firebase.auth.FirebaseUser;
import com.ntramanh1204.screenvocab.data.dto.UserDto;
import com.ntramanh1204.screenvocab.data.local.entities.UserEntity;
import com.ntramanh1204.screenvocab.domain.model.User;

public class UserMapper {

    public UserMapper() {}

    // Entity -> Domain
    public User entityToDomain(UserEntity entity) {
        if (entity == null) return null;
        return new User(
            entity.getUserId(),
            entity.getDisplayName(),
            entity.getEmail(),
            entity.isGuest(),
            entity.getCreatedAt()
        );
    }

    // Domain -> Entity
    public UserEntity domainToEntity(User user) {
        if (user == null) return null;
        UserEntity entity = new UserEntity();
        entity.setUserId(user.getUserId());
        entity.setDisplayName(user.getDisplayName());
        entity.setEmail(user.getEmail());
        entity.setGuest(user.isGuest());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setLastSyncAt(System.currentTimeMillis());
        return entity;
    }

    // DTO -> Domain
    public User dtoToDomain(UserDto dto) {
        if (dto == null) return null;
        return new User(
            dto.getUserId(),
            dto.getDisplayName(),
            dto.getEmail(),
            dto.isGuest(),
            dto.getCreatedAt()
        );
    }

    // Domain -> DTO
    public UserDto domainToDto(User user) {
        if (user == null) return null;
        return new UserDto(
            user.getUserId(),
            user.getDisplayName(),
            user.getEmail(),
            user.isGuest(),
            user.getCreatedAt(),
            null, // profileImageUrl - not available in domain
            System.currentTimeMillis() // lastLogin - set to current time
        );
    }

    // DTO -> Entity
    public UserEntity dtoToEntity(UserDto dto) {
        if (dto == null) return null;
        UserEntity entity = new UserEntity();
        entity.setUserId(dto.getUserId());
        entity.setDisplayName(dto.getDisplayName());
        entity.setEmail(dto.getEmail());
        entity.setGuest(dto.isGuest());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setLastSyncAt(System.currentTimeMillis());
        return entity;
    }

    // FirebaseUser -> Domain
    public User firebaseToDomain(FirebaseUser firebaseUser) {
        if (firebaseUser == null) return null;
        return new User(
            firebaseUser.getUid(),
            firebaseUser.getDisplayName() != null ? firebaseUser.getDisplayName() : "",
            firebaseUser.getEmail() != null ? firebaseUser.getEmail() : "",
            firebaseUser.isAnonymous(),
            firebaseUser.getMetadata() != null ? firebaseUser.getMetadata().getCreationTimestamp() : System.currentTimeMillis()
        );
    }

    // FirebaseUser -> Entity
    public UserEntity firebaseToEntity(FirebaseUser firebaseUser) {
        if (firebaseUser == null) return null;
        UserEntity entity = new UserEntity();
        entity.setUserId(firebaseUser.getUid());
        entity.setDisplayName(firebaseUser.getDisplayName() != null ? firebaseUser.getDisplayName() : "");
        entity.setEmail(firebaseUser.getEmail() != null ? firebaseUser.getEmail() : "");
        entity.setGuest(firebaseUser.isAnonymous());
        entity.setCreatedAt(firebaseUser.getMetadata() != null ? firebaseUser.getMetadata().getCreationTimestamp() : System.currentTimeMillis());
        entity.setLastSyncAt(System.currentTimeMillis());
        return entity;
    }
}