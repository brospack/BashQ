package by.vshkl.bashq.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import by.vshkl.bashq.R;
import by.vshkl.mvp.model.ComicsThumbnail;

public class ComicsAdapter extends RecyclerView.Adapter<ComicsViewHolder> {

    public interface OnComicItemClickListener {
        void onComicItemClicked(int position, String comicsLink, String comicImageLink);
    }

    private OnComicItemClickListener onComicItemClickListener;

    private List<ComicsThumbnail> comics = new ArrayList<>();

    @Override
    public ComicsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comic, parent, false);
        return new ComicsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ComicsViewHolder holder, int position) {
        final ComicsThumbnail comicsThumbnail = comics.get(position);
        final int currentPosition = position;

        holder.imgThumbnail.setImageURI(comicsThumbnail.getThumbLink());
        holder.imgThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onComicItemClickListener != null) {
                    onComicItemClickListener.onComicItemClicked(
                            currentPosition, comicsThumbnail.getComicsLink(), comicsThumbnail.getImageLink());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (this.comics != null) ? this.comics.size() : 0;
    }

    public void setComics(List<ComicsThumbnail> comics) {
        this.comics = comics;
    }

    public void setOnComicItemClickListener(OnComicItemClickListener onComicItemClickListener) {
        this.onComicItemClickListener = onComicItemClickListener;
    }

    public List<String> getComicsImageUrls() {
        List<String> images = new ArrayList<>();

        for (ComicsThumbnail comicsThumbnail : comics) {
            images.add(comicsThumbnail.getImageLink());
        }

        return images;
    }
}
