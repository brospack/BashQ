package by.vshkl.bashq.injection.module;

import by.vshkl.bashq.injection.scope.PerActivity;
import by.vshkl.mvp.domain.FetchQuoteComicImageUsecase;
import by.vshkl.mvp.domain.FetchQuotesUsecase;
import by.vshkl.mvp.domain.VoteQuoteUsecase;
import by.vshkl.mvp.presenter.QuotesPresenter;
import by.vshkl.repository.Repository;
import dagger.Module;
import dagger.Provides;

@Module
public class QuotesModule {

    @PerActivity
    @Provides
    public FetchQuotesUsecase provideFetchQuotesUsecase(Repository repository) {
        return new FetchQuotesUsecase(repository);
    }

    @PerActivity
    @Provides
    public VoteQuoteUsecase provideVoteQuoteUsecase(Repository repository) {
        return new VoteQuoteUsecase(repository);
    }

    @PerActivity
    @Provides
    public FetchQuoteComicImageUsecase provideQuoteComicImageUsecase(Repository repository) {
        return new FetchQuoteComicImageUsecase(repository);
    }

    @PerActivity
    @Provides
    public QuotesPresenter provideQuotesPresenter(FetchQuotesUsecase fetchQuotesUsecase,
                                                  VoteQuoteUsecase voteQuoteUsecase,
                                                  FetchQuoteComicImageUsecase fetchQuoteComicImageUsecase) {
        return new QuotesPresenter(fetchQuotesUsecase, voteQuoteUsecase, fetchQuoteComicImageUsecase);
    }
}
