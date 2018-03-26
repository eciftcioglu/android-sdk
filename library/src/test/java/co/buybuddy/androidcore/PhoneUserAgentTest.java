package co.buybuddy.androidcore;

import org.junit.Test;

import co.buybuddy.androidcore.authentication.keystore.networking.useragent.PhoneUserAgent;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by Emir on 23.03.2018.
 */

public class PhoneUserAgentTest {

    private PhoneUserAgent agent;

    @Test
     void constructsCarrier() {
        agent = new PhoneUserAgent();

        assertNotNull(agent);
    }

    @Test
     void testsGetCarrierName() {
        String carrierName = agent.getCarrierName();

        assertNotNull(carrierName);
    }

    @Test
     void testGetsCountryCode() {
        String countryCode = agent.getCountryCode();

        assertNotNull(countryCode);
    }

    @Test
     void testGetsCountryIso() {
        String countryIso = agent.getCountryIso();

        assertNotNull(countryIso);
    }

    @Test
     void testGetsAppName() {
        String appName = agent.getAppName();

        assertNotNull(appName);
    }

    @Test
    void testGetsAppVersion() {
        String appVersion = agent.getAppVersion();

        assertNotNull(appVersion);
    }
}
