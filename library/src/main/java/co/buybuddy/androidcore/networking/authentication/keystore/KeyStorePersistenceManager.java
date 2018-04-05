package co.buybuddy.androidcore.networking.authentication.keystore;

import android.security.KeyPairGeneratorSpec;
import android.util.Base64;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import javax.security.auth.x500.X500Principal;

import co.buybuddy.androidcore.ContextProvider;
import co.buybuddy.networking.authentication.persistence.SecurePersistenceManager;
import co.buybuddy.networking.authentication.persistence.SecureType;

public final class KeyStorePersistenceManager extends SecurePersistenceManager {
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    private static final String KEY_ALIAS = "BuyBuddyProductionKeyAlias";
    private static final String ENCRYPTED_KEY_NAME = "BuyBuddy-AESENCRYPTOR";
    private static final String AES_MODE = "AES/ECB/PKCS7Padding";
    private static final String CIPHER_PROVIDER_NAME_AES = "BC";
    private static final String CIPHER_PROVIDER_NAME_RSA = "AndroidOpenSSL";
    private static final String RSA_MODE = "RSA/ECB/PKCS1Padding";

    private KeyStore keyStore;
    private ContextProvider contextProvider;
    private KeyStoreKeyPersistence keyStoreKeyPersistence;
    private RSAGenerator rsaGenerator;

    /**
     * Initializes the KeyStorePersistenceManager object.
     */
    @Inject
    public KeyStorePersistenceManager(ContextProvider contextProvider, KeyStoreKeyPersistence keyStoreKeyPersistence, RSAGenerator rsaGenerator) {
        this.contextProvider = contextProvider;
        this.keyStoreKeyPersistence = keyStoreKeyPersistence;
        this.rsaGenerator = rsaGenerator;

        try {
            this.keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
            this.keyStore.load(null);

            if (keyStore.containsAlias(KEY_ALIAS)) {
                KeyStore.Entry keyEntry = keyStore.getEntry(KEY_ALIAS, null);

                if (!(keyEntry instanceof KeyStore.PrivateKeyEntry)) {
                    keyStoreKeyPersistence.flush();
                    generateKeyPair();
                }
            } else {
                generateKeyPair();
            }
        } catch (Exception e) {
            throw new RuntimeException("unexpected keystore error", e);
        }
    }

    /**
     * Persists given data in secure keystore.
     *
     * @param bytes      Data to be stored by the coordinator.
     * @param secureType Type of the data going to be stored by the coordinator.
     * @param key        Key of the value.
     */
    @Override
    public void persistData(byte[] bytes, SecureType secureType, String key) throws IOException {
        try {
            final Cipher cipher = Cipher.getInstance(AES_MODE, CIPHER_PROVIDER_NAME_AES);
            cipher.init(Cipher.ENCRYPT_MODE, loadEncryptedKey());

            final byte[] encodedBytes = cipher.doFinal(bytes);

            keyStoreKeyPersistence.setStringForKey(key, Base64.encodeToString(encodedBytes, Base64.DEFAULT));
        } catch (Exception e) {
            throw new IOException("unexpected keystore error", e);
        }
    }

    /**
     * Loads data from secure keystore with given key.
     *
     * @param key        Key of the value.
     * @param secureType Type of the data going to be stored by the coordinator.
     */
    @Override
    public byte[] loadData(SecureType secureType, String key) {
        try {
            final String encryptedKeyBase64Encoded = keyStoreKeyPersistence.getStringForKey(key);

            if (encryptedKeyBase64Encoded == null) {
                return null;
            }

            final byte[] encryptedDecodedData = Base64.decode(encryptedKeyBase64Encoded, Base64.DEFAULT);

            final Cipher cipher = Cipher.getInstance(AES_MODE, "BC");
            cipher.init(Cipher.DECRYPT_MODE, loadEncryptedKey());

            return cipher.doFinal(encryptedDecodedData);
        } catch (Exception e) {
            throw new RuntimeException("unexpected keystore error", e);
        }
    }

    /**
     * Removes data from secure keystore with given key.
     *
     * @param key        Key of the value.
     * @param secureType Type of the data going to be stored by the coordinator.
     */
    @Override
    public void removeData(SecureType secureType, String key) throws IOException {
        keyStoreKeyPersistence.removeStringForKey(key);
    }

    private void generateKeyPair() {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                final Calendar start = Calendar.getInstance();
                final Calendar end = Calendar.getInstance();
                end.add(Calendar.YEAR, 30);

                final KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(contextProvider.getContext())
                        .setAlias(KEY_ALIAS)
                        .setSubject(new X500Principal("CN=" + KEY_ALIAS))
                        .setStartDate(start.getTime())
                        .setEndDate(end.getTime())
                        .setSerialNumber(BigInteger.TEN)
                        .build();

                final KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", ANDROID_KEY_STORE);

                kpg.initialize(spec);
                kpg.generateKeyPair();

                saveEncryptedKey();
            }
        } catch (Exception e) {
            throw new RuntimeException("unexpected keystore error", e);
        }
    }

    private void saveEncryptedKey() throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableEntryException, NoSuchProviderException, InvalidKeyException, NoSuchPaddingException {
        //  Generate random bytes
        final byte[] key = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(key);

        //  Encode it to Base64

        final byte[] encryptedKey = rsaGenerator.encrypt(keyStore, key, KEY_ALIAS, RSA_MODE, CIPHER_PROVIDER_NAME_RSA);
        final String base64EncodedKey = Base64.encodeToString(encryptedKey, Base64.DEFAULT);

        keyStoreKeyPersistence.setStringForKey(ENCRYPTED_KEY_NAME, base64EncodedKey);
    }

    private Key loadEncryptedKey() throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchPaddingException, UnrecoverableEntryException, InvalidKeyException, NoSuchProviderException {
        final String encryptedKeyBase64Encoded = keyStoreKeyPersistence.getStringForKey(ENCRYPTED_KEY_NAME);
        final byte[] encryptedKey = Base64.decode(encryptedKeyBase64Encoded, Base64.DEFAULT);
        final byte[] key = rsaGenerator.decrypt(keyStore, encryptedKey, KEY_ALIAS, RSA_MODE, CIPHER_PROVIDER_NAME_RSA);

        return new SecretKeySpec(key, "AES");
    }
}