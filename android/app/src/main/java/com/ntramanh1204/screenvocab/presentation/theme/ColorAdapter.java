package com.ntramanh1204.screenvocab.presentation.theme;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ntramanh1204.screenvocab.R;

import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {

    private List<Integer> colors;
    private int selectedColor = -1;
    private OnColorClickListener listener;

    public interface OnColorClickListener {
        void onColorClick(int color);
    }

    public ColorAdapter(List<Integer> colors, OnColorClickListener listener) {
        this.colors = colors;
        this.listener = listener;
    }

    public void updateColors(List<Integer> newColors) {
        this.colors = newColors;
        notifyDataSetChanged();
    }

    public void setSelectedColor(int color) {
        selectedColor = color;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_circle_shape, parent, false);
        return new ColorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
        int color = colors.get(position);
        holder.colorCircle.setBackgroundColor(color);

        // Highlight if selected
        if (color == selectedColor) {
            holder.colorCircle.setAlpha(1f);
            holder.colorCircle.setElevation(8f);
        } else {
            holder.colorCircle.setAlpha(0.5f);
            holder.colorCircle.setElevation(0f);
        }

        holder.colorCircle.setOnClickListener(v -> {
            if (listener != null) {
                listener.onColorClick(color);
            }
        });
    }

    @Override
    public int getItemCount() {
        return colors == null ? 0 : colors.size();
    }

    static class ColorViewHolder extends RecyclerView.ViewHolder {
        View colorCircle;

        public ColorViewHolder(@NonNull View itemView) {
            super(itemView);
            colorCircle = itemView.findViewById(R.id.color_circle);
        }
    }
}
