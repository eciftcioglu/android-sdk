package co.buybuddy.androidcore.networking.authentication.keystore;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;

import java.io.IOException;

import co.buybuddy.androidcore.ContextProvider;
import co.buybuddy.networking.authentication.persistence.SecureType;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

public class KeyStorePersistenceManagerTest {
    private static final String LOAD_KEY = "LOAD_KEY";
    private static final String SOME_KEY_THAT_DOES_NOT_EXIST = "WEIRD_KEY";
    private static final String FIRST_DATA = "FIRST_DATA";
    private static final String SECOND_DATA = "SECOND_DATA";
    private static final String ENCRYPTED_BASE64_ENCODED_KEY = "";

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
        Mockito.reset(contextProvider, keyStoreKeyPersistence);
    }

    @Test
    public void persistsPasswordDataWithGivenKey() throws Exception {
        keyStorePersistenceManager.persistData(LOAD_KEY.getBytes(), SecureType.PASSWORD, LOAD_KEY);

        Mockito.verify(keyStoreKeyPersistence, VerificationModeFactory.times(1)).setStringForKey(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void persistsCertificateDataWithGivenKey() throws Exception {
        keyStorePersistenceManager.persistData(LOAD_KEY.getBytes(), SecureType.CERTIFICATE, LOAD_KEY);

        Mockito.verify(keyStoreKeyPersistence, VerificationModeFactory.times(1)).setStringForKey(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void persistsCryptographicKeyDataWithGivenKey() throws Exception {
        keyStorePersistenceManager.persistData(LOAD_KEY.getBytes(), SecureType.CRYPTOGRAPHIC_KEY, LOAD_KEY);

        Mockito.verify(keyStoreKeyPersistence, VerificationModeFactory.times(1)).setStringForKey(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void loadsPasswordDataWithGivenKey() throws Exception {
        Mockito.when(keyStoreKeyPersistence.getStringForKey(Mockito.anyString())).thenReturn(ENCRYPTED_BASE64_ENCODED_KEY);

        final byte[] result = keyStorePersistenceManager.loadData(SecureType.PASSWORD, LOAD_KEY);

        Mockito.verify(keyStoreKeyPersistence, VerificationModeFactory.times(1)).getStringForKey(Mockito.anyString());
        assertNotNull(result);
    }

    @Test
    public void loadsCertificateDataWithGivenKey() throws Exception {
        Mockito.when(keyStoreKeyPersistence.getStringForKey(Mockito.anyString())).thenReturn(ENCRYPTED_BASE64_ENCODED_KEY);

        final byte[] result = keyStorePersistenceManager.loadData(SecureType.CERTIFICATE, LOAD_KEY);

        Mockito.verify(keyStoreKeyPersistence, VerificationModeFactory.times(1)).getStringForKey(Mockito.anyString());
    }

    @Test
    public void loadsCryptographicKeyDataWithGivenKey() throws Exception {
        Mockito.when(keyStoreKeyPersistence.getStringForKey(Mockito.anyString())).thenReturn(ENCRYPTED_BASE64_ENCODED_KEY);

        final byte[] result = keyStorePersistenceManager.loadData(SecureType.CRYPTOGRAPHIC_KEY, LOAD_KEY);

        Mockito.verify(keyStoreKeyPersistence, VerificationModeFactory.times(1)).getStringForKey(Mockito.anyString());
    }

    @Test
    public void returnsNullOfPasswordWithGivenKeyDoesNotExist() throws Exception {
        final byte[] result = keyStorePersistenceManager.loadData(SecureType.PASSWORD, SOME_KEY_THAT_DOES_NOT_EXIST);

        Mockito.verify(keyStoreKeyPersistence, VerificationModeFactory.times(1)).getStringForKey(Mockito.anyString());
        assertNull(result);
    }

    @Test
    public void returnsNullOfCertificateWithGivenKeyDoesNotExist() throws Exception {
        final byte[] result = keyStorePersistenceManager.loadData(SecureType.CERTIFICATE, SOME_KEY_THAT_DOES_NOT_EXIST);

        Mockito.verify(keyStoreKeyPersistence, VerificationModeFactory.times(1)).getStringForKey(Mockito.anyString());
        assertNull(result);
    }

    @Test
    public void returnsNullOfCryptographicKeyWithGivenKeyDoesNotExist() throws Exception {
        final byte[] result = keyStorePersistenceManager.loadData(SecureType.CRYPTOGRAPHIC_KEY, SOME_KEY_THAT_DOES_NOT_EXIST);

        Mockito.verify(keyStoreKeyPersistence, VerificationModeFactory.times(1)).getStringForKey(Mockito.anyString());
        assertNull(result);
    }

    @Test
    public void removesPasswordDataWithGivenKey() throws Exception {
        Mockito.when(keyStoreKeyPersistence.getStringForKey(Mockito.anyString())).thenReturn(ENCRYPTED_BASE64_ENCODED_KEY);

        keyStoreKeyPersistence.removeStringForKey(LOAD_KEY);

        Mockito.verify(keyStoreKeyPersistence, VerificationModeFactory.times(1)).removeStringForKey(Mockito.anyString());
    }

    @Test(expected = IOException.class)
    public void throwsIfPasswordDataWithGivenKeyToRemoveDoesNotExist() throws Exception {
        keyStoreKeyPersistence.removeStringForKey(SOME_KEY_THAT_DOES_NOT_EXIST);

        Mockito.verify(keyStoreKeyPersistence, VerificationModeFactory.times(1)).removeStringForKey(Mockito.anyString());
    }
}
