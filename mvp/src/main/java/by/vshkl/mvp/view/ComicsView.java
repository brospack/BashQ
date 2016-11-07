package by.vshkl.mvp.view;

import java.util.List;

import by.vshkl.mvp.model.ComicsThumbnail;

public interface ComicsView extends View {

    void showComics(List<ComicsThumbnail> comics);
}
