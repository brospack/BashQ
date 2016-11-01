package by.vshkl.mvp.view;

import java.util.List;

import by.vshkl.mvp.model.Comic;

public interface ComicsView extends View {

    void showComics(List<Comic> comics);
}
