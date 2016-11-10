package by.vshkl.mvp.view;

import java.util.List;

import by.vshkl.mvp.model.Quote;

public interface QuotesView extends View {

    void showQuotes(List<Quote> quotes);

    void showQuoteComicImageDialog(String imageUrl);

    void showMessage(String message);

    void notifyDataSetChanged(int position);
}
