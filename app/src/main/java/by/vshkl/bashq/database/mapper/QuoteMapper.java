package by.vshkl.bashq.database.mapper;

import java.util.ArrayList;
import java.util.List;

import by.vshkl.bashq.database.model.QuoteEntity;
import by.vshkl.mvp.model.Quote;

public class QuoteMapper {

    public static QuoteEntity transform(Quote quote) {
        QuoteEntity quoteEntity = null;

        if (quote != null) {
            quoteEntity = new QuoteEntity();
            quoteEntity.setId(quote.getId());
            quoteEntity.setDate(quote.getDate());
            quoteEntity.setLink(quote.getLink());
            quoteEntity.setContent(quote.getContent());
        }

        return quoteEntity;
    }

    public static List<QuoteEntity> transform(List<Quote> quotes) {
        List<QuoteEntity> quoteEntities = new ArrayList<>();
        QuoteEntity quoteEntity;

        for (Quote quote : quotes) {
            quoteEntity = transform(quote);
            if (quoteEntity != null) {
                quoteEntities.add(quoteEntity);
            }
        }

        return quoteEntities;
    }
}
