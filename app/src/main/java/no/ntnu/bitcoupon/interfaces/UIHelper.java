package no.ntnu.bitcoupon.interfaces;

import android.content.DialogInterface;
import android.widget.EditText;

/**
 * Created by Patrick on 06.10.2014.
 */
public interface UIHelper {

  /**
   * Helper method to control the background activity indicator, commonly displayed as a spinning wheel in the
   * actionBar
   */
  void setLoading(boolean loading);

  /**
   * Helper metod to display a toast to the user. A tost is a message diaplayed on screen, that will automatically
   * disappear after a second or so
   *
   * @param toast - the message to display to the user
   */
  void displayToast(String toast);

  /**
   * Helper method to display an error dialog
   *
   * @param title   - title of the dialog
   * @param message - an error message displayed inside the error dialog
   */
  void displayErrorDialog(String title, String message);

  /**
   * Helper method to display a prompt dialog, with a yes and no button
   *
   * @param title               - title of the dialog
   * @param question            - a yes/no question displayed inside the prompt dialog
   * @param dialogClickListener - onClickListener to decide what happens when the user either clicks yes or no
   */
  void displayPromptDialog(String title, String question, DialogInterface.OnClickListener dialogClickListener);

  /**
   * Helper method to display an input dialog
   *
   * @param title    - title of the input dialog
   * @param desc     - a descriptive text displayed inside the input dialog
   * @param listener - onClickListener to decide what happens when the user clicks OK
   */
  void displayInputDialog(String title, String desc, DialogInterface.OnClickListener listener);

  /**
   * Helper method to display the software keyboard
   *
   * @param edit - the EditText field that should trigger the keyboard popup
   */
  void showKeyboard(EditText edit);

  /**
   * Helper method to dismiss the software keyboard
   *
   * @param edit - the EditText field that should trigger the keyboard popup
   */
  void hideKeyboard(EditText edit);

  String getInputText();
}
