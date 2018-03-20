package co.buybuddy.androidcore.authentication.keystore;

import java.io.IOError;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;

import co.buybuddy.networking.authentication.persistence.SecurePersistenceManager;
import co.buybuddy.networking.authentication.persistence.SecureType;

public final class KeystorePersistenceManager extends SecurePersistenceManager {
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";

    @Override
    public void persistData(byte[] bytes, SecureType secureType, String s) {
        try {
            KeyStore ks = KeyStore.getInstance(ANDROID_KEY_STORE);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] loadData(SecureType secureType, String s) {
        return new byte[0];
    }

    @Override
    public void removeData(SecureType secureType, String s) {

    }
}
