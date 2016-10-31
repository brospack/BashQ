package by.vshkl.view;

public interface View {

    void showEmpty();

    void showLoading();

    void hideLoading();

    void showError(String errorMessage);
}
