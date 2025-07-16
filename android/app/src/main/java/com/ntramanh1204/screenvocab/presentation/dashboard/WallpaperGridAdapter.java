package com.ntramanh1204.screenvocab.presentation.dashboard;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ntramanh1204.screenvocab.R;
import com.ntramanh1204.screenvocab.domain.model.Wallpaper;

import java.io.File;
import java.util.List;

public class WallpaperGridAdapter extends RecyclerView.Adapter<WallpaperGridAdapter.ViewHolder> {
    private static final String TAG = "WallpaperGridAdapter";
    private List<Wallpaper> wallpapers;
    private OnWallpaperClickListener clickListener;
    private Context context;

    public interface OnWallpaperClickListener {
        void onWallpaperClick(int position);
        void onWallpaperLongClick(int position);
    }

    public WallpaperGridAdapter(List<Wallpaper> wallpapers, OnWallpaperClickListener listener) {
        this.wallpapers = wallpapers;
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_wallpaper, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Wallpaper wallpaper = wallpapers.get(position);

        // Clear previous image
        holder.ivWallpaper.setImageDrawable(null);
        holder.ivWallpaper.setBackgroundColor(0xFFE0E0E0);

        String imageUrl = wallpaper.getLocalFileUrl();
        Log.d(TAG, "Loading image from getLocalFileUrl(): " + imageUrl);

        if (imageUrl == null || imageUrl.isEmpty() || imageUrl.equals("demo_placeholder")) {
            // Try to find file by wallpaperId
            String wallpaperId = wallpaper.getWallpaperId();
            Log.d(TAG, "Wallpaper ID: " + wallpaperId);

            String foundFilePath = findFileByWallpaperId(wallpaperId);
            if (foundFilePath != null) {
                Log.d(TAG, "Found file for wallpaper: " + foundFilePath);
                loadImageFromFile(holder, foundFilePath, position);
            } else {
                Log.d(TAG, "No file found for wallpaper ID: " + wallpaperId);
                setPlaceholderColor(holder, position);
            }
        } else {
            loadImageFromFile(holder, imageUrl, position);
        }

        // Set click listeners
        holder.itemView.setOnClickListener(v -> clickListener.onWallpaperClick(position));
        holder.itemView.setOnLongClickListener(v -> {
            clickListener.onWallpaperLongClick(position);
            return true;
        });
    }

    private String findFileByWallpaperId(String wallpaperId) {
        try {
            File filesDir = context.getFilesDir();
            File[] files = filesDir.listFiles();

            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName();

                    // Check if file name contains wallpaper ID or similar pattern
                    if (fileName.startsWith("wallpaper_") &&
                            (fileName.contains(wallpaperId.replace("wallpaper_", "")) ||
                                    fileName.startsWith(wallpaperId))) {

                        Log.d(TAG, "Found matching file: " + fileName + " for ID: " + wallpaperId);
                        return file.getAbsolutePath();
                    }
                }

                // If no exact match, try to find by timestamp pattern
                String timestamp = extractTimestamp(wallpaperId);
                if (timestamp != null) {
                    for (File file : files) {
                        String fileName = file.getName();
                        if (fileName.startsWith("wallpaper_") && fileName.contains(timestamp)) {
                            Log.d(TAG, "Found file by timestamp: " + fileName + " for ID: " + wallpaperId);
                            return file.getAbsolutePath();
                        }
                    }
                }

                // Last resort: try similar timestamps (within 1000ms range)
                return findSimilarTimestampFile(wallpaperId, files);
            }

        } catch (Exception e) {
            Log.e(TAG, "Error finding file by wallpaper ID: " + e.getMessage());
        }

        return null;
    }

    private String extractTimestamp(String wallpaperId) {
        try {
            // Extract timestamp from wallpaperId like "wallpaper_1752651873882"
            String[] parts = wallpaperId.split("_");
            if (parts.length >= 2) {
                return parts[1];
            }
        } catch (Exception e) {
            Log.e(TAG, "Error extracting timestamp: " + e.getMessage());
        }
        return null;
    }

    private String findSimilarTimestampFile(String wallpaperId, File[] files) {
        try {
            String targetTimestamp = extractTimestamp(wallpaperId);
            if (targetTimestamp == null) return null;

            long targetTime = Long.parseLong(targetTimestamp);

            for (File file : files) {
                String fileName = file.getName();
                if (fileName.startsWith("wallpaper_") && fileName.contains(".")) {
                    // Extract timestamp from filename
                    String fileTimestamp = fileName.substring(10, fileName.lastIndexOf('.'));
                    try {
                        long fileTime = Long.parseLong(fileTimestamp);
                        // Check if within 1000ms range
                        if (Math.abs(targetTime - fileTime) < 1000) {
                            Log.d(TAG, "Found similar timestamp file: " + fileName + " for ID: " + wallpaperId);
                            return file.getAbsolutePath();
                        }
                    } catch (NumberFormatException e) {
                        // Skip this file
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error finding similar timestamp file: " + e.getMessage());
        }

        return null;
    }

    private void loadImageFromFile(ViewHolder holder, String imageUrl, int position) {
        try {
            File imageFile = new File(imageUrl);

            // Debug: Print file info
            Log.d(TAG, "Original path: " + imageUrl);
            Log.d(TAG, "File exists: " + imageFile.exists());
            Log.d(TAG, "File absolute path: " + imageFile.getAbsolutePath());

            // Check if file exists
            if (!imageFile.exists()) {
                Log.e(TAG, "Image file does not exist: " + imageUrl);

                // Try alternative paths
                String alternativePath = tryAlternativePaths(imageUrl);
                if (alternativePath != null) {
                    imageFile = new File(alternativePath);
                    Log.d(TAG, "Using alternative path: " + alternativePath);
                } else {
                    Log.e(TAG, "No valid alternative path found");
                    setPlaceholderColor(holder, position);
                    return;
                }
            }

            Log.d(TAG, "Loading image with Glide: " + imageFile.getAbsolutePath());

            // Load with Glide
            Glide.with(context)
                    .load(imageFile)
                    .centerCrop()
                    .placeholder(R.color.gray_light)
                    .error(R.color.gray_light)
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // Disable cache for local files
                    .into(holder.ivWallpaper);

        } catch (Exception e) {
            Log.e(TAG, "Error loading image: " + e.getMessage());
            e.printStackTrace();
            setPlaceholderColor(holder, position);
        }
    }

    private String tryAlternativePaths(String originalPath) {
        // Get filename from original path
        String filename = getFileNameFromPath(originalPath);

        // Try different common paths for app data
        String[] possiblePaths = {
                originalPath,
                "/data/data/" + context.getPackageName() + "/files/" + filename,
                "/data/data/" + context.getPackageName() + "/cache/" + filename,
                context.getFilesDir() + "/" + filename,
                context.getCacheDir() + "/" + filename,
                context.getExternalFilesDir(null) + "/" + filename
        };

        Log.d(TAG, "Trying alternative paths for: " + filename);

        for (String path : possiblePaths) {
            if (path != null) {
                File testFile = new File(path);
                Log.d(TAG, "Checking path: " + path + " - exists: " + testFile.exists());
                if (testFile.exists()) {
                    return path;
                }
            }
        }

        // Debug: List files in common directories
        debugListFiles();

        return null;
    }

    private void debugListFiles() {
        Log.d(TAG, "=== DEBUG: Listing files in app directories ===");

        // List files in app's files directory
        File filesDir = context.getFilesDir();
        if (filesDir.exists()) {
            Log.d(TAG, "Files directory: " + filesDir.getAbsolutePath());
            File[] files = filesDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    Log.d(TAG, "  - " + file.getName() + " (size: " + file.length() + ")");
                }
            }
        }

        // List files in app's cache directory
        File cacheDir = context.getCacheDir();
        if (cacheDir.exists()) {
            Log.d(TAG, "Cache directory: " + cacheDir.getAbsolutePath());
            File[] files = cacheDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    Log.d(TAG, "  - " + file.getName() + " (size: " + file.length() + ")");
                }
            }
        }

        // List files in external files directory
        File externalDir = context.getExternalFilesDir(null);
        if (externalDir != null && externalDir.exists()) {
            Log.d(TAG, "External files directory: " + externalDir.getAbsolutePath());
            File[] files = externalDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    Log.d(TAG, "  - " + file.getName() + " (size: " + file.length() + ")");
                }
            }
        }
    }

    private String getFileNameFromPath(String fullPath) {
        if (fullPath == null) return "";
        return fullPath.substring(fullPath.lastIndexOf('/') + 1);
    }

    private void setPlaceholderColor(ViewHolder holder, int position) {
        int colorPosition = position % 3;
        int color;

        switch (colorPosition) {
            case 0: color = 0xFFE0E0E0; break;
            case 1: color = 0xFFD0D0D0; break;
            default: color = 0xFFC0C0C0; break;
        }

        holder.ivWallpaper.setBackgroundColor(color);
        holder.ivWallpaper.setImageDrawable(null);
    }

    @Override
    public int getItemCount() {
        return wallpapers != null ? wallpapers.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivWallpaper;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivWallpaper = itemView.findViewById(R.id.iv_wallpaper);
        }
    }
}