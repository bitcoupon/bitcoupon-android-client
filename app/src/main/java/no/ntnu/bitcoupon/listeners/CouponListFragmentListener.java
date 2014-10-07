package no.ntnu.bitcoupon.listeners;

import no.ntnu.bitcoupon.models.Coupon;

/**
 * Created by Patrick on 22.09.2014.
 *
 * Interface for anyone who wants to handle what the CouponListFragment does.
 *
 * Typically implemented by the mother activity, to handle communication between fragments.
 */
public interface CouponListFragmentListener {

  /**
   * Notifies the listener when the user clicks on a coupon in the list of coupons
   *
   * @param id - the internal ID for this coupon, NOT to be confused with transcation ID
   */
  void onCouponClicked(Coupon id);
}
