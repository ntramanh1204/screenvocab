package com.ntramanh1204.screenvocab.presentation.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ntramanh1204.screenvocab.R;
import com.ntramanh1204.screenvocab.core.di.AppContainer;
import com.ntramanh1204.screenvocab.data.local.entities.CollectionEntity;
import com.ntramanh1204.screenvocab.data.local.entities.WallpaperEntity;
import com.ntramanh1204.screenvocab.domain.repository.CollectionRepository;
import com.ntramanh1204.screenvocab.domain.repository.WallpaperRepository;
import com.ntramanh1204.screenvocab.presentation.dashboard.DashboardActivity;

import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WelcomeActivity extends AppCompatActivity {

    private Button btnSignUp;
    private Button btnContinueGuest;
    private CollectionRepository collectionRepository;
    private WallpaperRepository wallpaperRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        initDependencies();
        initViews();
        setupClickListeners();
    }

    private void initDependencies() {
        AppContainer appContainer = AppContainer.getInstance();
        collectionRepository = appContainer.getCollectionRepository();
        wallpaperRepository = appContainer.getWallpaperRepository();
    }

    private void initViews() {
        btnSignUp = findViewById(R.id.btn_sign_up);
        btnContinueGuest = findViewById(R.id.btn_continue_guest);
    }

    private void setupClickListeners() {
        btnSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });

        btnContinueGuest.setOnClickListener(v -> {
            // Đánh dấu trạng thái guest
            SharedPreferences prefs = getSharedPreferences("ScreenVocabPrefs", MODE_PRIVATE);
            prefs.edit().putBoolean("isGuest", true).apply();
            // Tạo collection mặc định cho guest
            String guestCollectionId = "guest_collection_" + UUID.randomUUID().toString();
            CollectionEntity guestCollection = new CollectionEntity();
            guestCollection.setCollectionId(guestCollectionId);
            guestCollection.setUserId("guest");
            guestCollection.setName("Guest Collection");
            guestCollection.setCreatedAt(System.currentTimeMillis());

            collectionRepository.insertCollection(guestCollection)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> {
                                // them du lieu mau
                                WallpaperEntity guestWallpaper = new WallpaperEntity();
                                guestWallpaper.setWallpaperId(UUID.randomUUID().toString());
                                guestWallpaper.setCollectionId(guestCollectionId);
                                guestWallpaper.setTheme("CLEAN_WHITE");
                                guestWallpaper.setRows(5);
                                guestWallpaper.setCols(5);
                                guestWallpaper.setTextHierarchy("primaryText");
                                guestWallpaper.setLocalFileUrl("file://path/to/sample.png");
                                guestWallpaper.setCreatedAt(System.currentTimeMillis());
                                guestWallpaper.setResolution("1080x2340");
                                guestWallpaper.setFormat("PNG");
                                guestWallpaper.setFileSize(1024);
                                wallpaperRepository.insertWallpaper(guestWallpaper)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe();
                                /// het them

                                Intent intent = new Intent(this, DashboardActivity.class);
                                intent.putExtra("guestCollectionId", guestCollectionId);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            },
                            throwable -> {
                                // Xử lý lỗi
                                Toast.makeText(this, "Failed to setup guest mode: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                    );
        });

        /*
         * Logic guest: Khi nhấn "Continue as Guest", lưu isGuest = true vào SharedPreferences và tạo 1 CollectionEntity mặc định (ID ngẫu nhiên, userId = "guest") để guest có thể tạo wallpaper dựa trên collection này.
         * Không tạo wallpaper ngay: Chỉ tạo collection, để guest tự tạo wallpaper trong HomeFragment (đúng với yêu cầu của bạn và SRS REQ-AUTH-002).
         * Truyền guestCollectionId: Gửi ID collection qua Intent để HomeFragment biết dùng collection nào.
         */
    }
}