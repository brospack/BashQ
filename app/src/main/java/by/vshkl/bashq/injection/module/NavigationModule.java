package by.vshkl.bashq.injection.module;

import by.vshkl.bashq.common.Navigator;
import by.vshkl.bashq.injection.scope.PerActivity;
import dagger.Module;
import dagger.Provides;

@Module
public class NavigationModule {

    @PerActivity
    @Provides
    public Navigator provideNavigator() {
        return new Navigator();
    }
}
