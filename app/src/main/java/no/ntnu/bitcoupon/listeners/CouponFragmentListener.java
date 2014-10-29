package no.ntnu.bitcoupon.listeners;

import no.ntnu.bitcoupon.models.CouponWrapper;

/**
 * Created by Patrick on 29.09.2014.
 */
public interface CouponFragmentListener {

  /**
   * Notifies the listener when then user initiates the spend coupon action
   * @param coupon
   */
  public void onSpendCoupon(CouponWrapper coupon);

}
