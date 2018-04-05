package co.buybuddy.androidcore.networking.http.ua;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.support.test.runner.AndroidJUnit4;
import android.telephony.TelephonyManager;
import android.test.mock.MockContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;

import co.buybuddy.androidcore.ContextProvider;
import co.buybuddy.androidcore.networking.ua.PhoneUserAgent;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by Emir on 23.03.2018.
 */

@RunWith(AndroidJUnit4.class)
public class PhoneUserAgentTest {
    private PhoneUserAgent agent;
    private ContextProviderImpl contextProvider;
    private Context fakeContext;

    @Before
    public void setUp() throws Exception {
        this.contextProvider = new ContextProviderImpl();
        this.agent = new PhoneUserAgent(this.contextProvider);
        this.fakeContext = this.contextProvider.getContext();
    }

    @After
    public void tearDown() throws Exception {
        Mockito.reset(this.contextProvider.getContext());
    }

    @Test
    public void constructsUserAgent() throws Exception {
        final PhoneUserAgent phoneUserAgent = new PhoneUserAgent(contextProvider);

        Mockito.verify(contextProvider.getContext(), VerificationModeFactory.times(1));
    }

    @Test
    public void testsGetCarrierName() {
        final String carrierName = agent.getCarrierName();

        assertEquals(carrierName, ContextProviderImpl.SIM_OPERATOR_NAME);
    }

    @Test
    public void testGetsCountryCode() {
        final String countryCode = agent.getCountryCode();

        assertEquals(countryCode, ContextProviderImpl.NETWORK_COUNTRY_ISO);
    }

    @Test
    public void testGetsCountryIso() {
        final String countryIso = agent.getCountryIso();

        assertEquals(countryIso, ContextProviderImpl.SIM_COUNTRY_ISO);
    }

    @Test
    public void testGetsAppName() throws Exception {
        String appName = agent.getAppName();

        assertNotNull(appName);
    }

    @Test
    public void testGetsAppVersion() throws Exception {
        String appVersion = agent.getAppVersion();

        assertNotNull(appVersion);
    }

    static class ContextProviderImpl implements ContextProvider {
        public static final String SIM_OPERATOR_NAME = "SIM_OPERATOR_NAME";
        public static final String SIM_COUNTRY_ISO = "SIM_COUNTRY_ISO";
        public static final String NETWORK_COUNTRY_ISO = "NETWORK_COUNTRY_ISO";

        private Context context;
        private TelephonyManager telephonyManager;
        private ApplicationInfo applicationInfo;

        @Override
        public Context getContext() {
            if (context == null) {
                context = Mockito.mock(MockContext.class);
                telephonyManager = Mockito.mock(TelephonyManager.class);
                applicationInfo = Mockito.mock(ApplicationInfo.class);

                Mockito.when(context.getSystemService(Context.TELEPHONY_SERVICE)).thenReturn(telephonyManager);
                Mockito.when(context.getApplicationInfo()).thenReturn(applicationInfo);

                Mockito.when(telephonyManager.getSimOperatorName()).thenReturn(SIM_OPERATOR_NAME);
                Mockito.when(telephonyManager.getSimCountryIso()).thenReturn(SIM_COUNTRY_ISO);
                Mockito.when(telephonyManager.getNetworkCountryIso()).thenReturn(NETWORK_COUNTRY_ISO);

                applicationInfo.nonLocalizedLabel = "";
            }
            return context;
        }
    }
}
