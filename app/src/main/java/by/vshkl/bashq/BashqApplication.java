package by.vshkl.bashq;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import by.vshkl.bashq.injection.component.ApplicationComponent;
import by.vshkl.bashq.injection.component.DaggerApplicationComponent;
import by.vshkl.bashq.injection.module.ApplicationModule;

public class BashqApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        setupInjector();
        Fresco.initialize(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    private void setupInjector() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }
}
