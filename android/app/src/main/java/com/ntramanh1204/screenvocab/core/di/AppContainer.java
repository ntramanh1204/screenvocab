package com.ntramanh1204.screenvocab.core.di;

import androidx.room.Room;
import com.ntramanh1204.screenvocab.ScreenVocabApp;
import com.ntramanh1204.screenvocab.data.local.database.ScreenVocabDatabase;
import com.ntramanh1204.screenvocab.data.mapper.UserMapper;
import com.ntramanh1204.screenvocab.data.remote.CloudinaryDataSource;
import com.ntramanh1204.screenvocab.data.remote.FirebaseAuthDataSource;
import com.ntramanh1204.screenvocab.data.remote.FirestoreDataSource;
import com.ntramanh1204.screenvocab.data.repository.AuthRepositoryImpl;
import com.ntramanh1204.screenvocab.data.repository.CollectionRepositoryImpl;
import com.ntramanh1204.screenvocab.data.repository.UserRepositoryImpl;
import com.ntramanh1204.screenvocab.data.repository.WallpaperRepositoryImpl;
import com.ntramanh1204.screenvocab.data.repository.WordRepositoryImpl;
import com.ntramanh1204.screenvocab.domain.repository.AuthRepository;
import com.ntramanh1204.screenvocab.domain.repository.CollectionRepository;
import com.ntramanh1204.screenvocab.domain.repository.UserRepository;
import com.ntramanh1204.screenvocab.domain.repository.WallpaperRepository;
import com.ntramanh1204.screenvocab.domain.repository.WordRepository;
import com.ntramanh1204.screenvocab.domain.usecase.auth.CreateUserProfileUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.auth.LoginUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.auth.SignUpUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.collection.CreateCollectionUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.collection.DeleteCollectionUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.collection.GetCollectionDetailUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.collection.GetCollectionsByUserUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.collection.UpdateCollectionUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.word.CreateWordUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.word.DeleteWordUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.word.GetWordsByCollectionUseCase;
import com.ntramanh1204.screenvocab.domain.usecase.word.UpdateWordUseCase;

public class AppContainer {
    private static AppContainer instance;

    // Database
    private ScreenVocabDatabase database;

    // Data sources
    private FirebaseAuthDataSource authDataSource;
    private FirestoreDataSource firestoreDataSource;
    private CloudinaryDataSource cloudinaryDataSource;

    // Repositories
    private AuthRepository authRepository;
    private UserRepository userRepository;
    private CollectionRepository collectionRepository;
    private WallpaperRepository wallpaperRepository;
    private WordRepository wordRepository;

    // Use cases
    private LoginUseCase loginUseCase;
    private SignUpUseCase signUpUseCase;
    private CreateUserProfileUseCase createUserProfileUseCase;

    private CreateWordUseCase createWordUseCase;
    private UpdateWordUseCase updateWordUseCase;
    private DeleteWordUseCase deleteWordUseCase;
    private GetWordsByCollectionUseCase getWordsByCollectionUseCase;

    private CreateCollectionUseCase createCollectionUseCase;
    private UpdateCollectionUseCase updateCollectionUseCase;
    private DeleteCollectionUseCase deleteCollectionUseCase;
    private GetCollectionsByUserUseCase getCollectionsByUserUseCase;
    private GetCollectionDetailUseCase getCollectionDetailUseCase;

    // Mappers
    private UserMapper userMapper;

    private AppContainer() {
        initializeDependencies();
    }

    public static AppContainer getInstance() {
        if (instance == null) {
            instance = new AppContainer();
        }
        return instance;
    }

    private void initializeDependencies() {
        // 1. Get database instance from ScreenVocabApp (không khởi tạo mới)
        database = ScreenVocabApp.getInstance().getDatabase();

        // 2. Initialize mappers
        userMapper = new UserMapper();

        // 3. Initialize data sources
        authDataSource = new FirebaseAuthDataSource();
        firestoreDataSource = new FirestoreDataSource();
        cloudinaryDataSource = new CloudinaryDataSource();

        // 4. Initialize repositories
        authRepository = new AuthRepositoryImpl(
                authDataSource,
                userMapper
        );

        userRepository = new UserRepositoryImpl(
                database.userDao(),
                firestoreDataSource
        );

        collectionRepository = new CollectionRepositoryImpl(
                database.collectionDao()
        );

        wallpaperRepository = new WallpaperRepositoryImpl(
                database.wallpaperDao()
        );

        wordRepository = new WordRepositoryImpl(
                database.wordDao()
        );

        // 5. Initialize use cases
        loginUseCase = new LoginUseCase(authRepository);
        signUpUseCase = new SignUpUseCase(authRepository);
        createUserProfileUseCase = new CreateUserProfileUseCase(userRepository);

        createWordUseCase = new CreateWordUseCase(wordRepository, collectionRepository);
        updateWordUseCase = new UpdateWordUseCase(wordRepository);
        deleteWordUseCase = new DeleteWordUseCase(wordRepository);
        getWordsByCollectionUseCase = new GetWordsByCollectionUseCase(wordRepository);

        createCollectionUseCase = new CreateCollectionUseCase(collectionRepository);
        updateCollectionUseCase = new UpdateCollectionUseCase(collectionRepository);
        deleteCollectionUseCase = new DeleteCollectionUseCase(collectionRepository, wordRepository);
        getCollectionsByUserUseCase = new GetCollectionsByUserUseCase(collectionRepository);
        getCollectionDetailUseCase = new GetCollectionDetailUseCase(collectionRepository);

    }

    // Getters
    public LoginUseCase getLoginUseCase() { return loginUseCase; }
    public SignUpUseCase getSignUpUseCase() { return signUpUseCase; }
    public CreateUserProfileUseCase getCreateUserProfileUseCase() { return createUserProfileUseCase; }

    public CreateWordUseCase getCreateWordUseCase() { return createWordUseCase; }
    public UpdateWordUseCase getUpdateWordUseCase() { return updateWordUseCase; }
    public DeleteWordUseCase getDeleteWordUseCase() { return deleteWordUseCase; }
    public GetWordsByCollectionUseCase getGetWordsByCollectionUseCase() { return getWordsByCollectionUseCase; }

    public CreateCollectionUseCase getCreateCollectionUseCase() { return createCollectionUseCase; }
    public UpdateCollectionUseCase getUpdateCollectionUseCase() { return updateCollectionUseCase; }
    public DeleteCollectionUseCase getDeleteCollectionUseCase() { return deleteCollectionUseCase; }
    public GetCollectionsByUserUseCase getGetCollectionsByUserUseCase() { return getCollectionsByUserUseCase; }
    public GetCollectionDetailUseCase getGetCollectionDetailUseCase() { return getCollectionDetailUseCase; }

    public AuthRepository getAuthRepository() { return authRepository; }
    public UserRepository getUserRepository() { return userRepository; }
    public CollectionRepository getCollectionRepository() { return collectionRepository; }
    public WallpaperRepository getWallpaperRepository() { return wallpaperRepository; }
    public WordRepository getWordRepository() { return wordRepository; }
}