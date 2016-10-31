package by.vshkl.mvp.presenter;

import by.vshkl.mvp.view.View;

public interface Presenter<T extends View> {

    void onCreate();

    void onStart();

    void onStop();

    void onPause();

    void attachView(T view);
}
