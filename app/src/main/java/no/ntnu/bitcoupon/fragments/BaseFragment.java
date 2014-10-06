package no.ntnu.bitcoupon.fragments;

import android.app.Fragment;
import android.content.DialogInterface;
import android.widget.EditText;

import no.ntnu.bitcoupon.activities.BaseActivity;
import no.ntnu.bitcoupon.interfaces.UIHelper;

/**
 * Created by Patrick on 22.09.2014.
 *
 * The main reason for the BaseFragment is to supply helper methods common to all fragments in the app.
 *
 * It also makes it easy to change the underlying Fragment implementation to one from the support library or similar, at
 * a later stage.
 */
public abstract class BaseFragment extends Fragment implements UIHelper {

  public BaseActivity getBaseActivity() {
    return (BaseActivity) getActivity();
  }

  @Override
  public void setLoading(boolean b) {
    getBaseActivity().setLoading(b);
  }

  @Override
  public void displayToast(String message) {
    getBaseActivity().displayToast(message);
  }

  @Override
  public void displayErrorDialog(final String title, final String message) {
    getBaseActivity().displayErrorDialog(title, message);
  }

  @Override
  public void displayPromptDialog(final String title, final String question,
                                  final DialogInterface.OnClickListener dialogClickListener) {
    getBaseActivity().displayPromptDialog(title, question, dialogClickListener);
  }

  @Override
  public void displayInputDialog(final String title, final String desc,
                                 final DialogInterface.OnClickListener listener) {
    getBaseActivity().displayInputDialog(title, desc, listener);
  }

  @Override
  public String getInputText() {
    return getBaseActivity().getInputText();
  }

  @Override
  public void showKeyboard(EditText edit) {
    getBaseActivity().showKeyboard(edit);
  }

  @Override
  public void hideKeyboard(EditText edit) {
    getBaseActivity().hideKeyboard(edit);
  }
}
