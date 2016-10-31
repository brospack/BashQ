package by.vshkl.mvp.view;

public interface View {

    void showEmpty();

    void showLoading();

    void hideLoading();

    void showError(String errorMessage);
}
