package by.vshkl.mvp.presenter;

import by.vshkl.mvp.view.View;

public interface Presenter<T extends View> {

    void onCreate();

    void onStop();

    void attachView(T view);
}
