package by.vshkl.bashq.database.mapper;

import java.util.ArrayList;
import java.util.List;

import by.vshkl.bashq.database.model.QuoteEntity;
import by.vshkl.mvp.model.Quote;

public class QuoteEntityMapper {

    public static Quote transform(QuoteEntity quoteEntity) {
        Quote quote = null;

        if (quoteEntity != null) {
            quote = new Quote();
            quote.setId(quoteEntity.getId());
            quote.setDate(quoteEntity.getDate());
            quote.setLink(quoteEntity.getLink());
            quote.setContent(quoteEntity.getContent());
            quote.setVoteCount(-1);
        }

        return quote;
    }

    public static List<Quote> transform(List<QuoteEntity> quoteEntities) {
        List<Quote> quotes = new ArrayList<>();
        Quote quote;

        for (QuoteEntity quoteEntity : quoteEntities) {
            quote = transform(quoteEntity);
            if (quote != null) {
                quotes.add(quote);
            }
        }

        return quotes;
    }
}
