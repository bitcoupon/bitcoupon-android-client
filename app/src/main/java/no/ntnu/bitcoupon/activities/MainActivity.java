package no.ntnu.bitcoupon.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import com.crashlytics.android.Crashlytics;

import bitcoupon.transaction.OutputHistory;
import bitcoupon.transaction.Transaction;
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
    getFragmentManager().beginTransaction().replace(R.id.container, CouponFragment.newInstance(coupon))
        .addToBackStack(CouponFragment.TAG).commit();
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public void onSpendCoupon(final CouponWrapper coupon) {
    setLoading(true);
    Network.fetchOutputHistory(new CouponCallback<OutputHistory>() {
      @Override
      public void onSuccess(int statusCode, OutputHistory outputHistory) {

        Network.spendCoupon(new CouponCallback<Transaction>() {
          @Override
          public void onSuccess(int statusCode, Transaction response) {
            displayToast("Successfully spend coupon: " + coupon.getTitle() + "!");
            couponListFragment.removeCoupon(response);
            couponListFragment.fetchAll();
            setLoading(false);
          }

          @Override
          public void onFail(int statusCode) {
            displayToast("Failed to spend coupon: " + coupon.getTitle());
            setLoading(false);
          }

        }, outputHistory, coupon);
        getFragmentManager().popBackStack();
        hideKeyboard();
      }

      @Override
      public void onFail(int statusCode) {
        displayToast("Failed to spend coupon with id " + coupon.getTitle());
        setLoading(false);
      }
    });
  }

}
