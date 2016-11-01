package by.vshkl.bashq.injection.component;

import android.app.Application;

import by.vshkl.bashq.BashqApplication;
import by.vshkl.bashq.injection.module.ApplicationModule;
import by.vshkl.bashq.injection.module.RepositoryModule;
import by.vshkl.bashq.injection.scope.PerApplication;
import by.vshkl.repository.Repository;
import dagger.Component;

@PerApplication
@Component(modules = {ApplicationModule.class, RepositoryModule.class})
public interface ApplicationComponent {

    Application application();

    BashqApplication bashqApplication();

    Repository repository();
}
