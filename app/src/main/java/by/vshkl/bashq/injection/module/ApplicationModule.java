package by.vshkl.bashq.injection.module;

import android.app.Application;

import by.vshkl.bashq.BashqApplication;
import by.vshkl.bashq.injection.scope.PerApplication;
import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final BashqApplication application;

    public ApplicationModule(BashqApplication application) {
        this.application = application;
    }

    @Provides
    @PerApplication
    public BashqApplication provideBashqApplication() {
        return application;
    }

    @Provides
    @PerApplication
    public Application provideApplication() {
        return application;
    }
}
