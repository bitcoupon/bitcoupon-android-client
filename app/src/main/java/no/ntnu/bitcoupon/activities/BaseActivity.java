package no.ntnu.bitcoupon.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.net.Socket;
import java.net.URI;

import no.ntnu.bitcoupon.BitCouponApplication;
import no.ntnu.bitcoupon.R;
import no.ntnu.bitcoupon.interfaces.UIHelper;
import no.ntnu.bitcoupon.network.Network;

/**
 * Created by Patrick on 22.09.2014.
 *
 * The main reason for the BaseActivity is to supply helper methods common to all activities in the app.
 *
 * It also makes it easy to change the underlying activity implementation to one from the support library or similar, at
 * a later stage.
 */
public abstract class BaseActivity extends Activity implements UIHelper {

  private static final String TAG = BaseActivity.class.getSimpleName();

  private int runningJobs;
  private EditText input;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
    super.onCreate(savedInstanceState);
  }

  @Override
  public void setLoading(final boolean loading) {
    if (loading) {
      ++runningJobs;
    } else {
      --runningJobs;
    }
    Log.d(TAG, loading ? " started" : " finished - Running jobs left: " + runningJobs);
    updateProgressbarState();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle item selection
    switch (item.getItemId()) {
      case R.id.action_change_url:
        displayUrlDialog();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void displayUrlDialog() {
    displayInputDialog("Backend URL", "Enter the URL of the location of the backend server",
                       new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                           if (which != DialogInterface.BUTTON_POSITIVE) {
                             return;
                           }
                           final String url = getInputText() + "/backend/";
                           final Socket socket = null;
                           new AsyncTask<Void, Void, Boolean>() {
                             @Override
                             protected Boolean doInBackground(Void... params) {
                               boolean reachable = false;
                               try {
                                 HttpGet get = new HttpGet(new URI(url));
                                 HttpClient httpClient = new DefaultHttpClient();
                                 get.addHeader(Network.getRequestTokenHeader());
                                 HttpResponse response = httpClient.execute(get);
                                 reachable = true;
                               } catch (Exception e) {
                               }
                               return reachable;
                             }

                             @Override
                             protected void onPostExecute(Boolean reachable) {
                               if (reachable) {
                                 SharedPreferences.Editor
                                     editor =
                                     BitCouponApplication.getApplication().getSecretPreferences().edit();
                                 editor.putString(Network.getApiRoot(), url);
                                 editor.commit();
                                 displayToast("Successfully connected to " + url + "!");
                               } else {
                                 displayToast("Invalid URL!");
                                 displayUrlDialog();
                               }
                             }
                           }.execute();
                         }
                       });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main, menu);
    return true;
  }

  private void updateProgressbarState() {
    boolean shouldLoad = runningJobs > 0;
    setProgressBarIndeterminateVisibility(shouldLoad);
  }

  @Override
  public void displayToast(final String toast) {
    Toast.makeText(BaseActivity.this, toast, Toast.LENGTH_SHORT).show();
  }


  @Override
  public void displayErrorDialog(final String title, final String message) {

    AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
    builder.setMessage(message)//
        .setTitle(title) //
        .setPositiveButton(android.R.string.ok, null)//
        .create()//
        .show();//
  }

  @Override
  public void displayPromptDialog(final String title, final String question,
                                  final DialogInterface.OnClickListener dialogClickListener) {
    AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
    builder.setTitle(title) //
        .setMessage(question)//
        .setPositiveButton(android.R.string.yes, dialogClickListener)//
        .setNegativeButton(android.R.string.no, dialogClickListener)//
        .create() //
        .show();
  }

  @Override
  public void displayInputDialog(final String title, final String desc,
                                 final DialogInterface.OnClickListener listener) {
    if (input != null) {
      // empty the input field from the last dialog
      input.setText("");
    }

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    input = new EditText(this);
    builder.setView(input);
    builder.setTitle(title) //
        .setMessage(desc)//
        .setPositiveButton(android.R.string.yes, listener) //
        .create() //
        .show();
    showKeyboard(input);
    input.setOnKeyListener(new View.OnKeyListener() {
      @Override
      public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
          hideKeyboard(input);
          return true;
        }
        return false;
      }
    });
  }

  @Override
  public void showKeyboard(EditText edit) {
    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.showSoftInput(edit, InputMethodManager.SHOW_IMPLICIT);

    edit.dispatchTouchEvent(
        MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
    edit.dispatchTouchEvent(
        MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
  }

  @Override
  public void hideKeyboard(EditText edit) {
    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
  }

  @Override
  public String getInputText() {
    return input.getText().toString().trim();
  }
}
