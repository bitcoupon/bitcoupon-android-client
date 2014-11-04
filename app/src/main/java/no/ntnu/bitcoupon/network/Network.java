package no.ntnu.bitcoupon.network;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import bitcoupon.BitCoupon;
import bitcoupon.transaction.OutputHistory;
import bitcoupon.transaction.Transaction;
import no.ntnu.bitcoupon.BitCouponApplication;
import no.ntnu.bitcoupon.callbacks.CouponCallback;
import no.ntnu.bitcoupon.models.CouponWrapper;
import no.ntnu.bitcoupon.models.TransactionWrapper;

/**
 * Created by Patrick on 05.10.2014.
 */
public class Network {

  //  public static final String API_ROOT = "http://78.91.25.28:3002/backend/";
  public static final String API_OUTPUT_HISTORY = "output_history";
  public static final String TAG = Network.class.getSimpleName();
  public static final String API_VERIFY_TRANSACTION = "verify_transaction";
  public static final String API_TRANSLATE_WORD = "address";
  public static final String API_TRANSLATE_ADDRESS = "word";

  public static void fetchOutputHistory(final CouponCallback<OutputHistory> callback) {
    new AsyncTask<Void, Void, OutputHistory>() {

      @Override
      protected OutputHistory doInBackground(Void... params) {
        String url = getApiRoot() + API_OUTPUT_HISTORY;
        HttpResponse response = null;
        try {
          Log.v(TAG, "requesting ... " + url);
          HttpPost post = new HttpPost(new URI(url));
          String req = BitCouponApplication.getApplication().getOutputRequest();
          post.setEntity(new StringEntity(req, "UTF-8"));
          post.addHeader(getRequestTokenHeader());
          HttpClient httpClient = new DefaultHttpClient();
          response = httpClient.execute(post);

          if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            return OutputHistory.fromJson(getReader(response));
          }
          return null;


        } catch (URISyntaxException e) {
          Log.e(TAG, "URISyntaxException", e);
        } catch (IOException e) {
          Log.e(TAG, "IOException", e);
        }
        return null;
      }

      @Override
      protected void onPostExecute(OutputHistory OutputHistory) {
        if (OutputHistory != null) {
          callback.onSuccess(0, OutputHistory);
        } else {
          callback.onFail(-1);
        }
      }
    }.execute();
  }

  public static Header getRequestTokenHeader() {
    Header header = new BasicHeader("Token", "CREATOR_ADDRESS");
    header.toString();
    return header;
  }

  public static BufferedReader getReader(HttpResponse response) throws IOException {
    InputStream is = response.getEntity().getContent();
    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
    return reader;
  }

  public static void spendCoupon(final CouponCallback<Transaction> callback, final OutputHistory outputHistory, final CouponWrapper coupon) {
    new AsyncTask<Void, Void, Transaction>() {
      @Override
      protected Transaction doInBackground(Void... params) {
        // Generate the send transaction object
        Transaction transaction = BitCoupon
            .generateSendTransaction(BitCouponApplication.getApplication().getPrivateKey(),  //
                                     coupon.getCoupon(), //
                                     coupon.getReceiverAddress(),  //
                                     outputHistory);

        String url = getApiRoot() + API_VERIFY_TRANSACTION;
        HttpResponse response = null;
        try {
          Log.v(TAG, "requesting ... " + url);
          HttpPost post = new HttpPost(new URI(url));
          post.addHeader(getRequestTokenHeader());

          post.addHeader("Content-type", "application/json");

          TransactionWrapper wrapper = new TransactionWrapper(transaction);
          String json = TransactionWrapper.toJson(wrapper);
          post.setEntity(new StringEntity(json, "UTF-8"));
          HttpClient httpClient = new DefaultHttpClient();
          response = httpClient.execute(post);

          return Transaction.fromJson(getReader(response));

        } catch (Exception e) {
          Log.e(TAG, "Exception", e);
        }
        return null;
      }

      @Override
      protected void onPostExecute(Transaction transaction) {
        if (transaction != null) {
          callback.onSuccess(HttpStatus.SC_OK, transaction);
        } else {
          callback.onFail(HttpStatus.SC_NO_CONTENT);
        }
      }
    }.execute();
  }

  public static void fetchAddressWord(final CouponCallback<AddressTranslator> callback) {
    new AsyncTask<Void, Void, AddressTranslator>() {
      @Override
      protected AddressTranslator doInBackground(Void... params) {
        String url = getApiRoot() + API_TRANSLATE_WORD;
        HttpResponse response = null;
        try {
          Log.v(TAG, "requesting ... " + url);
          HttpPost post = new HttpPost(new URI(url));
          post.addHeader(getRequestTokenHeader());

          post.addHeader("Content-type", "application/json");

          AddressTranslator translator = new AddressTranslator();
          translator.setAddress(BitCouponApplication.getApplication().getAddress());

          String json = AddressTranslator.toJson(translator);
          post.setEntity(new StringEntity(json, "UTF-8"));
          HttpClient httpClient = new DefaultHttpClient();
          response = httpClient.execute(post);

          return AddressTranslator.fromJson(getReader(response));

        } catch (Exception e) {
          Log.e(TAG, "Exception", e);
        }
        return null;
      }

      @Override
      protected void onPostExecute(AddressTranslator transaction) {
        if (transaction != null) {
          callback.onSuccess(HttpStatus.SC_OK, transaction);
        } else {
          callback.onFail(HttpStatus.SC_NO_CONTENT);
        }
      }
    }.execute();
  }


  public static void fetchWordAddress(final CouponCallback<AddressTranslator> callback, final String receiverWord) {
    new AsyncTask<Void, Void, AddressTranslator>() {
      @Override
      protected AddressTranslator doInBackground(Void... params) {
        String url = getApiRoot() + API_TRANSLATE_ADDRESS;
        HttpResponse response = null;
        try {
          Log.v(TAG, "requesting ... " + url);
          HttpPost post = new HttpPost(new URI(url));
          post.addHeader(getRequestTokenHeader());

          post.addHeader("Content-type", "application/json");

          AddressTranslator translator = new AddressTranslator();
          translator.setWord(receiverWord);

          String json = AddressTranslator.toJson(translator);
          post.setEntity(new StringEntity(json, "UTF-8"));
          HttpClient httpClient = new DefaultHttpClient();
          response = httpClient.execute(post);

          if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            return null;
          }

          return AddressTranslator.fromJson(getReader(response));

        } catch (Exception e) {
          Log.e(TAG, "Exception", e);
        }
        return null;
      }

      @Override
      protected void onPostExecute(AddressTranslator translation) {
        if (translation != null) {
          callback.onSuccess(HttpStatus.SC_OK, translation);
        } else {
          callback.onFail(HttpStatus.SC_NO_CONTENT);
        }
      }
    }.execute();
  }

  public static String getApiRoot() {
return    BitCouponApplication.getApplication().getApiRoot();
  }
}
