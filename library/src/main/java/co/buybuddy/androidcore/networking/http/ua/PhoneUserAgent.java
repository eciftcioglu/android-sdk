package co.buybuddy.androidcore.networking.http.ua;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import co.buybuddy.androidcore.ContextProvider;
import co.buybuddy.networking.http.ua.CanonicalUserAgent;

public class PhoneUserAgent extends CanonicalUserAgent {

    private String carrierName;
    private String countryIso;
    private String countryCode;
    private TelephonyManager manager;
    private ContextProvider contextProvider;

    /**
     * Initializes the 'PhoneUserAgent' class.
     */
    @Inject
    public PhoneUserAgent(ContextProvider contextProvider) {
        this.contextProvider = contextProvider;

        try {
            manager = (TelephonyManager) contextProvider.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Information about the users carrier name.
     */
    public String getCarrierName() {
        if (carrierName == null) {
            carrierName = manager.getSimOperatorName();
        }

        return carrierName;
    }

    /**
     * Information about the users carrier ISO country code.
     */
    public String getCountryIso() {
        if (countryIso == null) {
            countryIso = manager.getSimCountryIso();
        }

        return countryIso;
    }

    /**
     * Information about the users carrier mobile country code.
     */
    public String getCountryCode() {
        if (countryCode == null) {
            countryCode = manager.getNetworkCountryIso();
        }

        return countryCode;
    }

    /**
     * Information about the users current app name.
     */
    public String getAppName() throws Exception {
        final ApplicationInfo applicationInfo = contextProvider.getContext().getApplicationInfo();
        final int stringId = applicationInfo.labelRes;

        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : contextProvider.getContext().getString(stringId);
    }

    /**
     * Information about the users current app version.
     */
    public String getAppVersion() throws Exception {
        PackageInfo packageInfo = null;

        try {
            packageInfo = contextProvider.getContext().getPackageManager().getPackageInfo(contextProvider.getContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Integer version = null;
        if (packageInfo != null) {
            version = packageInfo.versionCode;
        }
        return version != null ? version.toString() : null;
    }

    public ContextProvider getContextProvider() {
        return contextProvider;
    }

    @Override
    protected List<VersionPair> generateVersionPairs() {
        super.generateVersionPairs();
        List<VersionPair> versionPairs = new ArrayList<>();

        versionPairs.add(new VersionPair(getCarrierName(), getCountryCode()));
        try {
            versionPairs.add(new VersionPair(getAppName(), getAppVersion()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return versionPairs;
    }

}
