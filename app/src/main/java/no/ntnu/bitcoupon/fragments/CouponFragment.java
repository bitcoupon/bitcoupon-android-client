package no.ntnu.bitcoupon.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import no.ntnu.bitcoupon.R;
import no.ntnu.bitcoupon.callbacks.CouponCallback;
import no.ntnu.bitcoupon.listeners.CouponFragmentListener;
import no.ntnu.bitcoupon.models.CouponWrapper;
import no.ntnu.bitcoupon.network.AddressTranslator;
import no.ntnu.bitcoupon.network.Network;

/**
 * Created by Patrick on 22.09.2014.
 *
 * The CouponFragment holds information about a specific coupon
 */
public class CouponFragment extends BaseFragment {

  public static final String TAG = CouponFragment.class.getSimpleName();
  private CouponFragmentListener mListener;

  public CouponFragment() {
  }

  public static Fragment newInstance(CouponWrapper coupon) {
    CouponFragment fragment = new CouponFragment();
    Bundle args = new Bundle();
    args.putString(CouponWrapper.COUPON_JSON, CouponWrapper.toJson(coupon));
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      mListener = (CouponFragmentListener) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(
          activity.toString() + " must implement " + CouponFragmentListener.class.getSimpleName());
    }
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_coupon, container, false);

    TextView title = (TextView) view.findViewById(R.id.tv_coupon_title);
    TextView spendButton = (TextView) view.findViewById(R.id.b_spend_coupon);
    final EditText receiver = (EditText) view.findViewById(R.id.tv_receiver);
    spendButton.setOnClickListener(getSpendCouponListener(receiver));

    CouponWrapper coupon = CouponWrapper.fromJson(getArguments().getString(CouponWrapper.COUPON_JSON));
    title.setText(coupon.getTitle());
    return view;
  }

  private View.OnClickListener getSpendCouponListener(final EditText receiver) {
    return new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String receiverWord = receiver.getText().toString().trim();
        if (receiverWord.length() == 0) {
          displayToast("Please fill in a receiver!");
          return;
        } else {
          Network.fetchWordAddress(getWordAddressTranslationCallback());
        }
      }
    };
  }

  private CouponCallback<AddressTranslator> getWordAddressTranslationCallback() {
    return new CouponCallback<AddressTranslator>() {
      @Override
      public void onSuccess(int statusCode, final AddressTranslator response) {
        if(response.getAddress() == null){
          displayToast("Invalid receiver ID!");
          return;
        }
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            switch (which) {
              case DialogInterface.BUTTON_POSITIVE:
                CouponWrapper coupon = CouponWrapper.fromJson(getArguments().getString(CouponWrapper.COUPON_JSON));
                coupon.setReceiverAddress(response.getAddress());
                mListener.onSpendCoupon(coupon);
                break;
            }
          }
        };
        displayPromptDialog("Spend Coupon Confirmation", "Are you sure you want to spend this coupon?",
                            dialogClickListener);
      }

      @Override
      public void onFail(int statusCode) {

      }
    };
  }
}

