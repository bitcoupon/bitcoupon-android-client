package no.ntnu.bitcoupon.network;

import com.google.gson.Gson;

import java.io.BufferedReader;

/**
 * Created by Patrick on 02.11.2014.
 */
public class AddressTranslator {

  private String word;
  private String address;

  public String getAddress() {
    return address;
  }

  public String getWord() {
    return word;
  }

  public static AddressTranslator fromJson(BufferedReader reader) {
    return new Gson().fromJson(reader, AddressTranslator.class);
  }


  public static AddressTranslator fromJson(String json) {
    return new Gson().fromJson(json, AddressTranslator.class);
  }

  public static String toJson(AddressTranslator item) {
    return new Gson().toJson(item, AddressTranslator.class);
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public void setWord(String word) {
    this.word = word;
  }
}
