package co.buybuddy.androidcore.networking.authentication.keystore;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public interface KeyStoreKeyPersistence {
    @Nullable
    public String getStringForKey(String key) throws IOException;

    public void setStringForKey(String key, String string) throws IOException;

    public void removeStringForKey(String key) throws IOException;

    public void flush() throws IOException;
}
