package no.ntnu.bitcoupon.models;

import com.google.gson.Gson;

import bitcoupon.transaction.Coupon;

/**
 * Created by Patrick on 22.09.2014.
 */
public class CouponWrapper {

  public static final String COUPON_JSON = "coupon_id";
  private static final String TAG = CouponWrapper.class.getSimpleName();
  private final int couponNumber;
  private final Coupon coupon;
  private String receiverAddress;
  private Payload payload;

  public CouponWrapper(int couponNumber, Coupon coupon) {
    this.couponNumber = couponNumber;
    this.coupon = coupon;

    this.payload = Payload.fromJson(coupon.getPayload());
  }

  public String getTitle() {
    return payload.getTitle();
  }

  public String getDescription() {
    return payload.getDescription();
  }

  public static CouponWrapper fromJson(String json) {
    return new Gson().fromJson(json, CouponWrapper.class);
  }

  public static String toJson(CouponWrapper item) {
    return new Gson().toJson(item, CouponWrapper.class);
  }

  public String getReturnWord(){
    return payload.getReturnWord();
  }

  @Override
  public String toString() {
    return " Title: " + getTitle() //
        ;
  }

  public Coupon getCoupon() {
    return coupon;
  }

  public void setReceiverAddress(String receiverAddress) {
    this.receiverAddress = receiverAddress;
  }

  public String getReceiverAddress() {
    return receiverAddress;
  }
}
