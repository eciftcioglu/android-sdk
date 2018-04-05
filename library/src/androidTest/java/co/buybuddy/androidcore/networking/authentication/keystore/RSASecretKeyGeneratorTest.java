package co.buybuddy.androidcore.networking.authentication.keystore;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;

import javax.crypto.NoSuchPaddingException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class RSASecretKeyGeneratorTest {

    private static final String CIPHER_PROVIDER_NAME_RSA = "AndroidOpenSSL";
    private static final String RSA_MODE = "RSA/ECB/PKCS1Padding";
    private static final String KEY_ALIAS = "BuyBuddyProductionKeyAlias";
    private static final String FIRST_DATA = "FIRST_DATA";
    private RSASecretKeyGenerator rsaSecretKeyGenerator;
    private KeyStore keyStore;

    @Before
    public void setUp() {

        keyStore = Mockito.mock(KeyStore.class);
        rsaSecretKeyGenerator = new RSASecretKeyGenerator();
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testEncryptsAndDecryptsData()
            throws NoSuchPaddingException,
            InvalidKeyException,
            NoSuchAlgorithmException,
            IOException, KeyStoreException,
            NoSuchProviderException,
            UnrecoverableEntryException {

        byte[] encryptedData = rsaSecretKeyGenerator.encrypt(keyStore, FIRST_DATA.getBytes(), KEY_ALIAS, RSA_MODE, CIPHER_PROVIDER_NAME_RSA);
        assertNotNull(encryptedData);

        byte[] decryptedData = rsaSecretKeyGenerator.decrypt(keyStore, encryptedData, KEY_ALIAS, RSA_MODE, CIPHER_PROVIDER_NAME_RSA);
        assertNotNull(decryptedData);

        assertEquals(encryptedData, decryptedData);
    }
}
