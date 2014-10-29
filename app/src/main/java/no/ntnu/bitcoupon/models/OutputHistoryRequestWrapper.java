package no.ntnu.bitcoupon.models;

import com.google.gson.Gson;

import java.io.BufferedReader;

import bitcoupon.transaction.OutputHistoryRequest;


/**
 * Created by Patrick on 05.10.2014.
 */
public class OutputHistoryRequestWrapper {

  final OutputHistoryRequest output_history_request;

  public OutputHistoryRequestWrapper(OutputHistoryRequest req) {
    this.output_history_request = req;
  }

  public static OutputHistoryRequestWrapper fromJson(BufferedReader reader) {
    return new Gson().fromJson(reader, OutputHistoryRequestWrapper.class);
  }


  public static OutputHistoryRequestWrapper fromJson(String json) {
    return new Gson().fromJson(json, OutputHistoryRequestWrapper.class);
  }

  public static String toJson(OutputHistoryRequestWrapper item) {
    return new Gson().toJson(item, OutputHistoryRequestWrapper.class);
  }
}
