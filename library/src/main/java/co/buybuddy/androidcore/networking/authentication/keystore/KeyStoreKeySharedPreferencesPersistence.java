package co.buybuddy.androidcore.networking.authentication.keystore;

import android.content.SharedPreferences;

import java.io.IOException;

import javax.inject.Singleton;

@Singleton
public final class KeyStoreKeySharedPreferencesPersistence implements KeyStoreKeyPersistence {
    private SharedPreferences sharedPreferences;

    /**
     * Initializes the 'KeyStoreKeySharedPreferencesPersistence' class.
     * @param sharedPreferences The sharedPreference object provided by the user.
     */
    public KeyStoreKeySharedPreferencesPersistence(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    /**
     * Gets the object corresponding to the given key.
     * @param key The key value corresponding to the object.
     */
    @Override
    public String getStringForKey(String key) {
        return sharedPreferences.getString(key, null);
    }

    /**
     * Stores the given String value with the provided key in shared preferences.
     * @param key The key value which will correspond to the object.
     * @param value The String value which will be stored with the key value.
     */
    @Override
    public void setStringForKey(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    /**
     * Removes the object corresponding to the provided key value.
     * @param key The key value corresponding to the object.
     */
    @Override
    public void removeStringForKey(String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    /**
     * Completely clears shared preferences.
     */
    @Override
    public void flush() throws IOException {
        sharedPreferences.edit().clear().apply();
    }
}
