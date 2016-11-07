package by.vshkl.bashq.injection.component;

import android.content.Context;

import by.vshkl.bashq.injection.module.ActivityModule;
import by.vshkl.bashq.injection.module.ComicsModule;
import by.vshkl.bashq.injection.module.NavigationModule;
import by.vshkl.bashq.injection.scope.PerActivity;
import by.vshkl.bashq.ui.acticity.ComicsActivity;
import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class,
        modules = {ActivityModule.class, ComicsModule.class, NavigationModule.class})
public interface ComicsComponent {

    void inject(ComicsActivity ComicsActivity);

    Context context();
}
