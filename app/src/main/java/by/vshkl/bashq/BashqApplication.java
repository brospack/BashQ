package by.vshkl.bashq;

import android.app.Application;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.NoEncryption;

import by.vshkl.bashq.injection.component.ApplicationComponent;
import by.vshkl.bashq.injection.component.DaggerApplicationComponent;
import by.vshkl.bashq.injection.module.ApplicationModule;

public class BashqApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        setupInjector();
        initializeHawk();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    private void setupInjector() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    private void initializeHawk() {
        Hawk.init(this)
                .setEncryption(new NoEncryption())
                .build();
    }
}
