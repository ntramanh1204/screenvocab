package com.ntramanh1204.screenvocab.presentation.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.ntramanh1204.screenvocab.R;
import java.util.List;

public class WallpaperGridAdapter extends RecyclerView.Adapter<WallpaperGridAdapter.ViewHolder> {
    private List<String> wallpapers; // For now, use String URLs or resource names
    private OnWallpaperClickListener clickListener;

    public interface OnWallpaperClickListener {
        void onWallpaperClick(int position);
        void onWallpaperLongClick(int position);
    }

    public WallpaperGridAdapter(List<String> wallpapers, OnWallpaperClickListener listener) {
        this.wallpapers = wallpapers;
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wallpaper, parent, false);
        return new ViewHolder(view);
    }

    // @Override
    // public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    //     // For now, just set a placeholder background
    //     holder.ivWallpaper.setImageResource(R.color.gray_light);
    //     holder.itemView.setOnClickListener(v -> clickListener.onWallpaperClick(position));
    //     holder.itemView.setOnLongClickListener(v -> {
    //         clickListener.onWallpaperLongClick(position);
    //         return true;
    //     });
    // }

//     @Override
// public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//     String url = wallpapers.get(position);
//   Glide.with(holder.itemView.getContext())
//     .load(url)
//     .placeholder(R.color.gray_light) // Use a color resource as a placeholder
//     .into(holder.ivWallpaper);
// }
@Override
public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    String url = wallpapers.get(position);
    
    if (url.equals("demo_placeholder")) {
        // For demo placeholders, just show a colored rectangle
        // Use position to vary the colors slightly
        int colorPosition = position % 3;
        int color;
        
        switch (colorPosition) {
            case 0:
                color = 0xFFE0E0E0; // Light gray
                break;
            case 1:
                color = 0xFFD0D0D0; // Slightly darker gray
                break;
            default:
                color = 0xFFC0C0C0; // Even darker gray
                break;
        }
        
        holder.ivWallpaper.setBackgroundColor(color);
        // Clear any previous image that might have been loaded
        holder.ivWallpaper.setImageDrawable(null);
    } else {
        // Normal case - load image from URL using Glide
        try {
            Glide.with(holder.itemView.getContext())
                 .load(url)
                 .centerCrop()
                 .placeholder(R.color.gray_light)
                 .into(holder.ivWallpaper);
        } catch (Exception e) {
            // If Glide fails, at least show a colored background
            holder.ivWallpaper.setBackgroundColor(0xFFE0E0E0);
            holder.ivWallpaper.setImageDrawable(null);
        }
    }
    
    holder.itemView.setOnClickListener(v -> clickListener.onWallpaperClick(position));
    holder.itemView.setOnLongClickListener(v -> {
        clickListener.onWallpaperLongClick(position);
        return true;
    });
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
