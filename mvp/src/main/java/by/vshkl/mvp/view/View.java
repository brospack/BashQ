package by.vshkl.mvp.view;

import by.vshkl.mvp.model.Errors;

public interface View {

    void showEmpty();

    void showLoading();

    void hideLoading();

    void showError(Errors errorType);
}
