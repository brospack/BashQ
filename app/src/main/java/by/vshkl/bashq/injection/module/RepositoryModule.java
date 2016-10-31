package by.vshkl.bashq.injection.module;

import by.vshkl.bashq.injection.scope.PerApplication;
import by.vshkl.bashq.network.NetworkRepository;
import by.vshkl.repository.Repository;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class RepositoryModule {

    @Provides
    @PerApplication
    public OkHttpClient provideOkHttpClient() {
        return new OkHttpClient();
    }

    @Provides
    @PerApplication
    public Repository provideNetworkRepository(OkHttpClient okHttpClient) {
        return new NetworkRepository(okHttpClient);
    }
}
