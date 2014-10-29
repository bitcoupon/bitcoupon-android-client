package no.ntnu.bitcoupon;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import bitcoupon.BitCoupon;
import bitcoupon.transaction.OutputHistoryRequest;
import no.ntnu.bitcoupon.models.OutputHistoryRequestWrapper;

/**
 * Created by Patrick on 22.09.2014.
 *
 * A custom implementation of the Application class. The application class is initialized before any of the activities
 * are started, so in practise; before the actual app starts. It can therefore be used to initialize chunks of code
 * related to setup etc.
 */
public class BitCouponApplication extends Application {


  private static final String SECRET = "SECRET";
  private static final String PRIVATE_KEY = "PRIVATE_KEY";
  private static final String OUTPUT_HISTORY_REQUEST = "OUTPUT_HISTORY_REQUEST";
  private static final String ADDRESS = "ADDRESS";
  private static BitCouponApplication application;

  @Override
  public void onCreate() {
    super.onCreate();
    application = this;
  }

  public SharedPreferences getSecretPreferences() {
    return getSharedPreferences(BitCouponApplication.SECRET, Context.MODE_PRIVATE);
  }

  public String getPrivateKey() {
    SharedPreferences prefs = getSecretPreferences();
    String key = prefs.getString(PRIVATE_KEY, null);
    if (key == null) {
      key = BitCoupon.generatePrivateKey();
      SharedPreferences.Editor edit = prefs.edit();
      edit.putString(PRIVATE_KEY, key);
      edit.commit();
    }
    return key;
  }

  public String getAddress() {
    SharedPreferences prefs = getSecretPreferences();
    String address = prefs.getString(ADDRESS, null);
    if (address == null) {
      address = BitCoupon.generateAddress(getPrivateKey());
      SharedPreferences.Editor edit = prefs.edit();
      edit.putString(ADDRESS, address);
      edit.commit();
    }
    return address;
  }

  public String getOutputRequest() {
    SharedPreferences prefs = getSecretPreferences();
    String outputHistoryJson = prefs.getString(OUTPUT_HISTORY_REQUEST, null);
    if (outputHistoryJson == null) {
      OutputHistoryRequest outputHistoryRequest = BitCoupon.generateOutputHistoryRequest(getPrivateKey());
      String json = OutputHistoryRequestWrapper.toJson(new OutputHistoryRequestWrapper(outputHistoryRequest));
      SharedPreferences.Editor edit = prefs.edit();
      edit.putString(OUTPUT_HISTORY_REQUEST, json);
      edit.commit();
      outputHistoryJson = prefs.getString(OUTPUT_HISTORY_REQUEST, null);
    }
    return outputHistoryJson;
  }

  public static BitCouponApplication getApplication() {
    return application;
  }
}
