package co.buybuddy.androidcore.networking.authentication.keystore;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.security.KeyStore;
import java.util.Arrays;

import co.buybuddy.androidcore.ContextProvider;
import co.buybuddy.networking.authentication.persistence.SecureType;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

public class KeyStorePersistenceManagerIntegrationTest {
    private static final String KEY = "KEY";
    private static final String DECRYPTED_BASE64_ENCODED_KEY = "ENCRYPTED_BASE64_ENCODED_KEY";
    private static final String ENCRYPTED_BASE64_ENCODED_KEY = "TEVOR1RIU0lYVEVFTktFWQ==";
    private static final String SOME_KEY_THAT_DOES_NOT_EXIST = "SOME_KEY_THAT_DOES_NOT_EXIST";
    private static final String FIRST_DATA = "FIRST_DATA";
    private static final String SECOND_DATA = "SECOND_DATA";
    private static final String DECRYPTED_KEY = "LENGTHSIXTEENKEY";
    private static final String ENCRYPTED_KEY = "ENCRYPTED_KEY";

    private KeyStorePersistenceManager keyStorePersistenceManager;
    private ContextProvider contextProvider;
    private KeyStoreKeyPersistence keyStoreKeyPersistence;
    private RSAGenerator rsaGenerator;

    @Before
    public void setUp() throws Exception {
        contextProvider = Mockito.mock(ContextProvider.class);
        keyStoreKeyPersistence = Mockito.mock(KeyStoreKeyPersistence.class);
        rsaGenerator = Mockito.mock(RSAGenerator.class);

        keyStorePersistenceManager = new KeyStorePersistenceManager(contextProvider, keyStoreKeyPersistence, rsaGenerator);
    }

    @After
    public void tearDown() throws Exception {
        Mockito.reset(contextProvider, keyStoreKeyPersistence, rsaGenerator);
    }

    @Test
    public void doesNotOverwriteThePasswordWithCertificate() throws Exception {

        Mockito.when(keyStoreKeyPersistence.getStringForKey(Mockito.anyString())).thenReturn(ENCRYPTED_BASE64_ENCODED_KEY);
        Mockito.when(rsaGenerator.decrypt(Mockito.any(KeyStore.class), Mockito.any(byte[].class), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(DECRYPTED_KEY.getBytes());
        //  Firstly, persist a password on key store.
        keyStorePersistenceManager.persistData(FIRST_DATA.getBytes(), SecureType.PASSWORD, SOME_KEY_THAT_DOES_NOT_EXIST);
        //  Secondly, persist a certificate on the same key with different data.
        keyStorePersistenceManager.persistData(SECOND_DATA.getBytes(), SecureType.CERTIFICATE, SOME_KEY_THAT_DOES_NOT_EXIST);

        //  Fetch the persisted data with keys.
        final byte[] passwordData = keyStorePersistenceManager.loadData(SecureType.PASSWORD, SOME_KEY_THAT_DOES_NOT_EXIST);
        final byte[] certificateData = keyStorePersistenceManager.loadData(SecureType.CERTIFICATE, SOME_KEY_THAT_DOES_NOT_EXIST);

        assertFalse(Arrays.equals(passwordData, certificateData));
        assertEquals(FIRST_DATA.getBytes(), passwordData);
        assertEquals(SECOND_DATA.getBytes(), certificateData);
    }
}