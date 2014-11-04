package no.ntnu.bitcoupon.models;

import com.google.gson.Gson;

import org.joda.time.DateTime;

/**
 * Created by Patrick on 04.11.2014.
 */
public class Payload {

  private String title;
  private String timestamp;
  private String returnWord;
  private String description;

  public String getReturnWord() {
    return returnWord;
  }

  public DateTime getTimestamp() {
    return new DateTime(Integer.parseInt(timestamp));
  }

  public String getTitle() {
    return title;
  }

  public static Payload fromJson(String json) {
    return new Gson().fromJson(json, Payload.class);
  }

  public static String toJson(CouponWrapper item) {
    return new Gson().toJson(item, Payload.class);
  }

  public String getDescription() {
    return description;
  }
}
