package com.ntramanh1204.screenvocab.domain.usecase.collection;

import com.ntramanh1204.screenvocab.domain.model.Collection;
import com.ntramanh1204.screenvocab.domain.repository.CollectionRepository;
import com.ntramanh1204.screenvocab.data.mapper.CollectionMapper;
import com.ntramanh1204.screenvocab.core.utils.ValidationUtils;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.Completable;

public class CreateCollectionUseCase {

    public final CollectionRepository collectionRepository;

    public CreateCollectionUseCase(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    // Params class với thêm trường description
    public static class Params {
        private final String name;
        private final String description;
        private final String userId;

        public Params(String name, String description, String userId) {
            this.name = name;
            this.description = description;
            this.userId = userId;
        }

        public String getName() { return name; }
        public String getDescription() { return description; }
        public String getUserId() { return userId; }
    }

    public Single<Collection> execute(Params params) {
        return validateInput(params)
                .andThen(checkDuplicateCollection(params))
                .andThen(createAndSaveCollection(params));
    }

    private Completable validateInput(Params params) {
        return Completable.fromAction(() -> {
            if (ValidationUtils.isEmpty(params.getName())) {
                throw new IllegalArgumentException("Collection name cannot be empty");
            }
            if (ValidationUtils.isEmpty(params.getUserId())) {
                throw new IllegalArgumentException("User ID cannot be empty");
            }
            if (params.getName().length() > 50) {
                throw new IllegalArgumentException("Collection name is too long (max 50 characters)");
            }
        });
    }

    private Completable checkDuplicateCollection(Params params) {
        return collectionRepository.getCollectionsByUser(params.getUserId())
                .flatMapCompletable(collections -> {
                    boolean exists = collections.stream()
                            .anyMatch(entity -> entity.getName().equalsIgnoreCase(params.getName().trim()));
                    if (exists) {
                        return Completable.error(new IllegalArgumentException("Collection name already exists"));
                    }
                    return Completable.complete();
                });
    }

    private Single<Collection> createAndSaveCollection(Params params) {
        return Single.fromCallable(() ->
                Collection.create(params.getName().trim(), params.getDescription(), params.getUserId())
        ).flatMap(collection ->
                collectionRepository.insertCollection(CollectionMapper.toEntity(collection))
                        .andThen(Single.just(collection))
        );
    }
}
