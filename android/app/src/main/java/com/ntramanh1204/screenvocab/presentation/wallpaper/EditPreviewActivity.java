package com.ntramanh1204.screenvocab.presentation.wallpaper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.ntramanh1204.screenvocab.R;
import com.ntramanh1204.screenvocab.core.di.AppContainer;
import com.ntramanh1204.screenvocab.core.utils.SaveUtils;
import com.ntramanh1204.screenvocab.domain.model.Collection;
import com.ntramanh1204.screenvocab.domain.model.Word;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class EditPreviewActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_REQUEST_CODE = 100;
    private CardView pendingCardView;
    private EditPreviewViewModel viewModel;
    private TextView tvTitle, tvDesc;
    private GridLayout gridVocab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_wallpaper);

        CardView cardView = findViewById(R.id.card_preview);

        int selectedColor = getIntent().getIntExtra("selected_theme_color", -1);
        Log.d("EditPreviewActivity", "Selected color received: " + selectedColor);
        cardView.setCardBackgroundColor(selectedColor);

        String collectionId = getIntent().getStringExtra("collection_id");
        Log.d("EditPreviewActivity", "Collection ID received: " + collectionId);

        EditPreviewViewModelFactory factory = new EditPreviewViewModelFactory(
                AppContainer.getInstance().getGetCollectionWithWordsByIdUseCase());
        viewModel = new ViewModelProvider(this, factory).get(EditPreviewViewModel.class);

        tvTitle = findViewById(R.id.tv_set_title);
        tvDesc = findViewById(R.id.tv_set_desc);
        gridVocab = findViewById(R.id.grid_vocab);

        viewModel.getSelectedCollection().observe(this, collection -> {
            if (collection != null) {
                Log.d("EditPreviewActivity", "Collection loaded: id=" + collection.getCollectionId()
                        + ", name=" + collection.getName());
                renderPreview(collection);
            } else {
                Log.d("EditPreviewActivity", "Collection is null");
            }
        });

        if (collectionId != null) {
            viewModel.loadCollectionById(collectionId);
        } else {
            Log.d("EditPreviewActivity", "No collection ID passed");
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

            Log.d("EditPreviewActivity-word", "Words size: " +
                    (collection.getWords() != null ? collection.getWords().size() : "null"));
        }
        Log.d("EditPreviewActivity-word", "Words size: " + (collection.getWords() != null ? collection.getWords().size() : "null"));
    }

    private void saveWallpaper(CardView cardView) {
        Bitmap bitmap = getBitmapFromView(cardView);

        if (bitmap == null) {
            Toast.makeText(this, "Không thể tạo ảnh từ view", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String savedPath;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+: MediaStore
                savedPath = SaveUtils.saveImageToGallery(this, bitmap);
                showSavedDialog(savedPath);
            } else {
                // Android 9-: Cần permission
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Lưu cardView để xử lý sau khi có permission
                    pendingCardView = cardView;

                    // Request permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            STORAGE_PERMISSION_REQUEST_CODE);
                    return;
                }

                // Có permission rồi, tiến hành lưu
                savedPath = saveToExternalStorage(bitmap);
                showSavedDialog(savedPath);
            }

            Log.d("savelocation", savedPath);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi lưu hình!", Toast.LENGTH_SHORT).show();
        }
    }

    // Xử lý kết quả permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, tiến hành lưu
                if (pendingCardView != null) {
                    try {
                        Bitmap bitmap = getBitmapFromView(pendingCardView);
                        String savedPath = saveToExternalStorage(bitmap);
                        showSavedDialog(savedPath);
                        Log.d("savelocation", savedPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Lỗi khi lưu hình!", Toast.LENGTH_SHORT).show();
                    }
                    pendingCardView = null;
                }
            } else {
                // Permission denied
                Toast.makeText(this, "Cần quyền truy cập bộ nhớ để lưu hình nền", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Tách logic lưu file thành method riêng
    private String saveToExternalStorage(Bitmap bitmap) throws IOException {
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyWallpapers");
        if (!dir.exists()) dir.mkdirs();

        String fileName = "wallpaper_" + System.currentTimeMillis() + ".png";
        File file = new File(dir, fileName);

        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        }

        // Notify gallery
        addImageToGallery(file);

        return file.getAbsolutePath();
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        view.draw(canvas);
        return returnedBitmap;
    }

    private void showSavedDialog(String filePath) {
        new AlertDialog.Builder(this)
                .setTitle("Đã lưu hình nền")
                .setMessage("Hình nền đã được lưu vào:\n" + filePath)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void addImageToGallery(File imageFile) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(imageFile);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

}
