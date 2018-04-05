package co.buybuddy.androidcore.networking.authentication.keystore.di.component;

import co.buybuddy.androidcore.networking.authentication.keystore.KeyStorePersistenceManager;
import co.buybuddy.androidcore.networking.authentication.keystore.di.module.KeyStoreKeyPersistenceModule;
import dagger.Component;

@Component(modules = KeyStoreKeyPersistenceModule.class)
public interface KeyStoreKeyPersistenceComponent {
    void inject(KeyStorePersistenceManager keyStorePersistenceManager);
}
