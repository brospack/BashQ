package by.vshkl.bashq.injection.component;

import android.content.Context;

import by.vshkl.bashq.injection.module.ActivityModule;
import by.vshkl.bashq.injection.module.NavigationModule;
import by.vshkl.bashq.injection.scope.PerActivity;
import by.vshkl.bashq.ui.activity.MainActivity;
import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class,
        modules = {ActivityModule.class, NavigationModule.class})
public interface MainActivityComponent {

    void inject(MainActivity mainActivity);

    Context context();
}
