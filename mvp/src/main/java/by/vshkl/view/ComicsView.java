package by.vshkl.view;

import java.util.List;

import by.vshkl.model.Comic;

public interface ComicsView extends View {

    void showComics(List<Comic> comics);
}
