package by.vshkl.view;

import java.util.List;

import by.vshkl.model.Quote;

public interface QuotesView extends View {

    void showQuotes(List<Quote> quotes);
}
