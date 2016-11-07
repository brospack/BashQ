package by.vshkl.bashq.injection.module;

import by.vshkl.bashq.injection.scope.PerActivity;
import by.vshkl.mvp.domain.FetchComicsUsecase;
import by.vshkl.mvp.presenter.ComicsPresenter;
import by.vshkl.repository.Repository;
import dagger.Module;
import dagger.Provides;

@Module
public class ComicsModule {

    @PerActivity
    @Provides
    public FetchComicsUsecase provideFetchComicsUsecase(Repository repository) {
        return new FetchComicsUsecase(repository);
    }

    @PerActivity
    @Provides
    public ComicsPresenter provideComicsPresenter(FetchComicsUsecase fetchComicsUsecase) {
        return new ComicsPresenter(fetchComicsUsecase);
    }
}
