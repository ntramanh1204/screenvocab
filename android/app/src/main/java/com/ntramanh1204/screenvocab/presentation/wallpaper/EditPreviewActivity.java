package com.ntramanh1204.screenvocab.presentation.wallpaper;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.ntramanh1204.screenvocab.R;
import com.ntramanh1204.screenvocab.core.di.AppContainer;
import com.ntramanh1204.screenvocab.core.utils.SaveUtils;
import com.ntramanh1204.screenvocab.data.local.entities.WallpaperEntity;
import com.ntramanh1204.screenvocab.domain.model.Collection;
import com.ntramanh1204.screenvocab.domain.model.Word;
import com.ntramanh1204.screenvocab.presentation.shared.SharedWallpaperViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class EditPreviewActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_REQUEST_CODE = 100;
    private CardView pendingCardView;
    private EditPreviewViewModel viewModel;
    private SharedWallpaperViewModel sharedWallpaperViewModel;
    private TextView tvTitle, tvDesc;
    private GridLayout gridVocab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_wallpaper);

        sharedWallpaperViewModel = new ViewModelProvider(this).get(SharedWallpaperViewModel.class);

        CardView cardView = findViewById(R.id.card_preview);
        int selectedColor = getIntent().getIntExtra("selected_theme_color", -1);
        cardView.setCardBackgroundColor(selectedColor);

        String collectionId = getIntent().getStringExtra("collection_id");

        EditPreviewViewModelFactory factory = new EditPreviewViewModelFactory(
                AppContainer.getInstance().getGetCollectionWithWordsByIdUseCase(),
                AppContainer.getInstance().getSaveWallpaperUseCase());
        viewModel = new ViewModelProvider(this, factory).get(EditPreviewViewModel.class);

        tvTitle = findViewById(R.id.tv_set_title);
        tvDesc = findViewById(R.id.tv_set_desc);
        gridVocab = findViewById(R.id.grid_vocab);

        viewModel.getSelectedCollection().observe(this, collection -> {
            if (collection != null) {
                renderPreview(collection);
            }
        });

        if (collectionId != null) {
            viewModel.loadCollectionById(collectionId);
        }

        TextView btnFinish = findViewById(R.id.btn_finish);
        btnFinish.setOnClickListener(v -> saveWallpaper(cardView));
    }

    private void renderPreview(Collection collection) {
        tvTitle.setText(collection.getDisplayName());
        tvDesc.setText(collection.getDescription());

        LayoutInflater inflater = LayoutInflater.from(this);
        gridVocab.removeAllViews();

        for (Word word : collection.getWords()) {
            View item = inflater.inflate(R.layout.item_vocab, gridVocab, false);

            TextView tvWord = item.findViewById(R.id.tv_vocab_word);
            TextView tvPron = item.findViewById(R.id.tv_vocab_pron);
            TextView tvMean = item.findViewById(R.id.tv_vocab_meaning);

            tvWord.setText(word.getTerm());
            tvPron.setText(word.getPronunciation());
            tvMean.setText(word.getDefinition());

            gridVocab.addView(item);
        }
    }

    private void saveWallpaper(CardView cardView) {
        Bitmap bitmap = getBitmapFromView(cardView);
        if (bitmap == null) {
            Toast.makeText(this, "Không thể tạo ảnh từ view", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String internalPath = SaveUtils.saveImageToInternalStorage(this, bitmap);
            String externalPath;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                externalPath = SaveUtils.saveImageToGallery(this, bitmap);
            } else {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    pendingCardView = cardView;
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            STORAGE_PERMISSION_REQUEST_CODE);
                    return;
                }
                externalPath = saveToExternalStorage(bitmap);
            }

            saveWallpaperToDatabase(internalPath, externalPath);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi lưu hình!", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveWallpaperToDatabase(String internalPath, String externalPath) {
        String wallpaperId = "wallpaper_" + System.currentTimeMillis();
        String collectionId = getSelectedCollectionId();
        String userId = getCurrentUserId();

        WallpaperEntity wallpaperEntity = new WallpaperEntity(
                wallpaperId,
                collectionId,
                userId,
                null,
                null,
                internalPath,
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                "Wallpaper " + wallpaperId
        );

        viewModel.saveWallpaper(wallpaperEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            sharedWallpaperViewModel.setWallpaperSaved(true); // Trigger reload ở HomeFragment
                            showSavedDialog("Đã lưu hình nền:\nInternal: " + internalPath + "\nExternal: " + externalPath);
                        },
                        error -> Toast.makeText(this, "Lưu file thành công nhưng lỗi database", Toast.LENGTH_SHORT).show()
                );
    }

    private String getSelectedCollectionId() {
        return getIntent().getStringExtra("collection_id");
    }

    private String getCurrentUserId() {
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        return prefs.getString("current_user_id", "guest_user");
    }

    private String saveToExternalStorage(Bitmap bitmap) throws IOException {
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyWallpapers");
        if (!dir.exists()) dir.mkdirs();

        String fileName = "wallpaper_" + System.currentTimeMillis() + ".png";
        File file = new File(dir, fileName);

        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        }

        addImageToGallery(file);

        return file.getAbsolutePath();
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        view.draw(canvas);
        return returnedBitmap;
    }

    private void showSavedDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Đã lưu hình nền")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    finish(); // Quay lại HomeFragment
                })
                .show();
    }

    private void addImageToGallery(File imageFile) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(imageFile);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }
}
