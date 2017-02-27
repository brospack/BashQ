package by.vshkl.bashq;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import by.vshkl.bashq.injection.component.ApplicationComponent;
import by.vshkl.bashq.injection.component.DaggerApplicationComponent;
import by.vshkl.bashq.injection.module.ApplicationModule;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

public class BashqApplication extends Application {

    private ApplicationComponent applicationComponent;
    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        BashqApplication application = (BashqApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        refWatcher = LeakCanary.install(this);
        setupInjector();
        Fresco.initialize(this);
        FlowManager.init(new FlowConfig.Builder(this).build());
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
