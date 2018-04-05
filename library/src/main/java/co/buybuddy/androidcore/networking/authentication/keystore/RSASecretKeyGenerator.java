package co.buybuddy.androidcore.networking.authentication.keystore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;

public final class RSASecretKeyGenerator implements RSAGenerator {

    /**
     *Encrypts the given byte array using the keystore keypair before storing it in SharedPreferences.
     * @param keyStore The KeyStore object.
     * @param secret The byte array which will be encrypted.
     * @param keyAlias An alias to fetch or create a keystore keypair if it doesn't exist.
     * @param transformation The name of the transformation.
     * @param provider The name of the provider.
     */
    @Override
    public byte[] encrypt(KeyStore keyStore, byte[] secret, String keyAlias, String transformation,
                          String provider)
            throws IOException,
            UnrecoverableEntryException,
            NoSuchAlgorithmException,
            KeyStoreException,
            NoSuchProviderException,
            NoSuchPaddingException,
            InvalidKeyException {

        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(keyAlias, null);
        Cipher inputCipher = Cipher.getInstance(transformation, provider);
        inputCipher.init(Cipher.ENCRYPT_MODE, privateKeyEntry.getCertificate().getPublicKey());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, inputCipher);
        cipherOutputStream.write(secret);
        cipherOutputStream.close();

        return outputStream.toByteArray();
    }

    /**
     *Decrypts the given byte array using the keystore keypair before storing it in SharedPreferences.
     * @param keyStore The KeyStore object.
     * @param encrypted The encrypted byte array which will be decrypted.
     * @param keyAlias An alias to fetch or create a keystore keypair if it doesn't exist.
     * @param transformation The name of the transformation.
     * @param provider The name of the provider.
     */
    @Override
    public byte[] decrypt(KeyStore keyStore, byte[] encrypted, String keyAlias, String transformation,
                          String provider)
            throws IOException,
            UnrecoverableEntryException,
            NoSuchAlgorithmException,
            KeyStoreException,
            NoSuchProviderException,
            NoSuchPaddingException,
            InvalidKeyException {

        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(keyAlias, null);
        Cipher output = Cipher.getInstance(transformation, provider);
        output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());
        CipherInputStream cipherInputStream = new CipherInputStream(
                new ByteArrayInputStream(encrypted), output);

        ArrayList<Byte> values = new ArrayList<>();
        int nextByte;
        while ((nextByte = cipherInputStream.read()) != -1) {
            values.add((byte) nextByte);
        }

        byte[] decryptedKeyAsBytes = new byte[values.size()];
        for (int i = 0; i < decryptedKeyAsBytes.length; i++) {
            decryptedKeyAsBytes[i] = values.get(i);
        }
        return decryptedKeyAsBytes;
    }
}
