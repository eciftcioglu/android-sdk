package co.buybuddy.androidcore.networking.authentication.keystore;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;

import javax.crypto.NoSuchPaddingException;

public interface RSAGenerator {

    public byte[] encrypt(KeyStore keyStore, byte[] secret, String keyAlias, String transformation,
                          String provider) throws IOException, UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException;

    public byte[] decrypt(KeyStore keyStore, byte[] encrypted, String keyAlias, String transformation,
                          String provider) throws IOException, UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException;

}
