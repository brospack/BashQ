package by.vshkl.bashq.injection.component;

import by.vshkl.bashq.injection.module.QuotesModule;
import by.vshkl.bashq.injection.scope.PerActivity;
import by.vshkl.bashq.ui.fragment.QuotesFragment;
import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class,
        modules = {QuotesModule.class})
public interface QuotesComponent {

    void inject(QuotesFragment quotesFragment);
}
