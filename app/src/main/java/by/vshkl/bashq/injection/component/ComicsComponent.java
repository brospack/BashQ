package by.vshkl.bashq.injection.component;

import by.vshkl.bashq.injection.module.ComicsModule;
import by.vshkl.bashq.injection.scope.PerActivity;
import by.vshkl.bashq.ui.fragment.ComicsFragment;
import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class,
        modules = {ComicsModule.class})
public interface ComicsComponent {

    void inject(ComicsFragment comicsFragment);
}
