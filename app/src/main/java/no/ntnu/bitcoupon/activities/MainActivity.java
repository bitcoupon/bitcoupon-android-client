package no.ntnu.bitcoupon.activities;

import android.os.Bundle;

import com.crashlytics.android.Crashlytics;

import bitcoupon.BitCoupon;
import bitcoupon.transaction.OutputHistory;
import bitcoupon.transaction.Transaction;
import no.ntnu.bitcoupon.BitCouponApplication;
import no.ntnu.bitcoupon.R;
import no.ntnu.bitcoupon.callbacks.CouponCallback;
import no.ntnu.bitcoupon.fragments.CouponFragment;
import no.ntnu.bitcoupon.fragments.CouponListFragment;
import no.ntnu.bitcoupon.listeners.CouponFragmentListener;
import no.ntnu.bitcoupon.listeners.CouponListFragmentListener;
import no.ntnu.bitcoupon.models.CouponWrapper;
import no.ntnu.bitcoupon.network.Network;


/**
 * The MainActivity acts as an entry point for the application, and holds the fragments. It should handle all
 * communication between the fragments relevant to this activity.
 *
 * For the time being this is limited to the coupon couponListFragment and the list of coupons.
 */
public class MainActivity extends BaseActivity implements CouponListFragmentListener, CouponFragmentListener {

  private CouponListFragment couponListFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Crashlytics.start(this);
    setContentView(R.layout.activity_main);
    if (savedInstanceState == null) {
      couponListFragment = CouponListFragment.newInstance();
      getFragmentManager().beginTransaction().add(R.id.container, couponListFragment).commit();
    }
  }

  @Override
  public void onCouponClicked(CouponWrapper coupon) {
    coupon.setModified();
    getFragmentManager().beginTransaction().replace(R.id.container, CouponFragment.newInstance(coupon))
        .addToBackStack(CouponFragment.TAG).commit();
  }

  @Override
  public void onSpendCoupon(final CouponWrapper coupon) {
    setLoading(true);
    Network.fetchOutputHistory(new CouponCallback<OutputHistory>() {
      @Override
      public void onSuccess(int statusCode, OutputHistory outputHistory) {
        // check that the coupon is valid
//        if (!isCouponValid(outputHistory, coupon)) {
//          // if no coupon with that address was found, display an error and return
//          displayToast("Invalid coupon!");
//          getFragmentManager().popBackStack();
//          return;
//        }

        // Generate the send transaction object
        Transaction transaction = BitCoupon.generateSendTransaction(BitCouponApplication.getApplication().getPrivateKey(),  //
                                                                    coupon.getCoupon(), //
                                                                    BitCouponApplication.getApplication().getAddress(),  //
                                                                    outputHistory);

        Network.spendCoupon(new CouponCallback<Transaction>() {
          @Override
          public void onSuccess(int statusCode, Transaction response) {
            displayToast("Transaction: " + response.toString() + " spent!");
            getFragmentManager().popBackStack();
            couponListFragment.removeCoupon(response);
            couponListFragment.fetchAll();
          }

          @Override
          public void onFail(int statusCode) {
            displayToast("Failed to spend coupon with id " + coupon.getId());
            getFragmentManager().popBackStack();
          }

        }, transaction);
        setLoading(false);

      }

      @Override
      public void onFail(int statusCode) {
        displayToast("Failed to spend coupon with id " + coupon.getId());
        setLoading(false);
      }
    });
  }

  /**
   * Check whether this coupon is valid; ie. it exist in the transaction history received from the server If the coupon
   * is invalid, this probably means the coupon was spent elsewhere, or that it was invalidated by the beckend
   */
//  private boolean isCouponValid(OutputHistory OutputHistory, CouponWrapper coupon) {
//    List<String> creatorAddresses = BitCoupon.getCreatorAddresses(Network.PRIVATE_KEY,  //
//                                                                  OutputHistory);
//    for (String couponAddress : creatorAddresses) {
//      if (couponAddress.equalsIgnoreCase(coupon.getCouponAddress())) {
//        return true;
//      }
//    }
//    return false;
//  }
}
