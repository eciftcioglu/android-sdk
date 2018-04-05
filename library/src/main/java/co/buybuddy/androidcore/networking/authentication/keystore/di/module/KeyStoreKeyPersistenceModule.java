package co.buybuddy.androidcore.networking.authentication.keystore.di.module;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

import co.buybuddy.androidcore.ContextProvider;
import co.buybuddy.androidcore.networking.authentication.keystore.KeyStoreKeyPersistence;
import co.buybuddy.androidcore.networking.authentication.keystore.KeyStoreKeySharedPreferencesPersistence;
import dagger.Module;
import dagger.Provides;

@Module
public class KeyStoreKeyPersistenceModule {
    private static final String KEYSTORE_SHARED_PREFERENCES_NAME = "BuyBuddy.KeyStore";
    private final SharedPreferences sharedPreferences;

    @Inject
    public KeyStoreKeyPersistenceModule(ContextProvider contextProvider) {
        this.sharedPreferences = contextProvider.getContext().getSharedPreferences(KEYSTORE_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    @Provides
    public KeyStoreKeyPersistence provideKeyStoreKeyPersistence() {
        return new KeyStoreKeySharedPreferencesPersistence(sharedPreferences);
    }
}
