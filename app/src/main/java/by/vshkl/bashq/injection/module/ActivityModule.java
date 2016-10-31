package by.vshkl.bashq.injection.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import by.vshkl.bashq.injection.scope.PerActivity;
import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private final AppCompatActivity activity;

    public ActivityModule(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Provides
    @PerActivity
    public Context context() {
        return this.activity;
    }
}
