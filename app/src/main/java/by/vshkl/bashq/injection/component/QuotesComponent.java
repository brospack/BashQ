package by.vshkl.bashq.injection.component;

import android.content.Context;

import by.vshkl.bashq.injection.module.ActivityModule;
import by.vshkl.bashq.injection.module.NavigationModule;
import by.vshkl.bashq.injection.module.QuotesModule;
import by.vshkl.bashq.injection.scope.PerActivity;
import by.vshkl.bashq.ui.activity.QuotesActivity;
import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class,
        modules = {ActivityModule.class, QuotesModule.class, NavigationModule.class})
public interface QuotesComponent {

    void inject(QuotesActivity quotesActivity);

    Context context();
}
