package co.buybuddy.androidcore.authentication.keystore;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import co.buybuddy.networking.http.ua.CanonicalUserAgent;
import co.buybuddy.networking.http.ua.UserAgent;

/**
 * Created by Emir on 23.03.2018.
 */

public class PhoneUserAgent extends CanonicalUserAgent{

    @Inject private Context context;

    /**
     Information about the users carrier name.
     */
    private String carrierName;

    /**
     Information about the users carrier ISO country code.
     */
    private String countryIso;

    /**
     Information about the users carrier mobile country code.
     */
    private String countryCode;

    /**
     Information about the telephony services on the device.
     */
    private TelephonyManager manager;

    /**
     Initializes the 'PhoneUserAgent' class.
     */
    public PhoneUserAgent() {
        manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public String getCarrierName() {
        if  (carrierName == null) {
            carrierName = manager.getSimOperatorName();
        }
        return carrierName;
    }

    public String getCountryIso() {
        if (countryIso == null) {
            countryIso = manager.getSimCountryIso();
        }
        return countryIso;
    }

    public String getCountryCode() {
        if (countryCode == null) {
            countryCode = manager.getNetworkCountryIso();
        }
        return countryCode;
    }

    public String getAppName() {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;

        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    public String getAppVersion() {
        PackageInfo packageInfo = null;

        try {
             packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Integer version = null;
        if (packageInfo != null) {
            version = packageInfo.versionCode;
        }
        return version != null ? version.toString() : null;
    }

    @Override
    protected List<VersionPair> generateVersionPairs() {
        super.generateVersionPairs();
        List<VersionPair> versionPairs = new ArrayList<>();

        versionPairs.add(new VersionPair(getCarrierName(), getCountryCode()));
        versionPairs.add(new VersionPair(getAppName(), getAppVersion()));

        return versionPairs;
    }
}
