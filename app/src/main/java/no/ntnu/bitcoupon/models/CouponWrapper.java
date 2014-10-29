package no.ntnu.bitcoupon.models;

import com.google.gson.Gson;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.DateTime;

import bitcoupon.transaction.Coupon;

/**
 * Created by Patrick on 22.09.2014.
 */
public class CouponWrapper {

  public static final String COUPON_JSON = "coupon_id";
  private static final String TAG = CouponWrapper.class.getSimpleName();
  private final int couponNumber;
  private final Coupon coupon;
  private String title;
  private String description;
  private String id;
  private long created;
  private long modified;

  public CouponWrapper(int couponNumber, Coupon coupon) {
    this.couponNumber = couponNumber;
    this.coupon = coupon;
    this.created = System.currentTimeMillis();
  }

  public String getCouponAddress() {
    return coupon.getCreatorAddress();
  }

  public String getDescription() {
    return coupon.getPayload();
  }

  public DateTime getCreated() {
    return new DateTime(created);
  }

  public DateTime getModified() {
    return new DateTime(modified);
  }

  public void setModified() {
    modified = new DateTime().getMillis();
  }

  public String getTitle() {
    return String.valueOf(couponNumber);
  }

  public String getId() {
    return id;
  }


//  public static CouponWrapper createDummy() {
//    CouponWrapper dummy = new CouponWrapper(999, "1Kau4L6BM1h6QzLYubq1qWrQSjWdZFQgMb");
//    dummy.id = String.valueOf((int) (Math.random() * 1000));
//    dummy.description = "This is the dummy coupons' description!";
//    dummy.title = "Dummy coupon";
//    return dummy;
//  }

  public static CouponWrapper fromJson(String json) {
    return new Gson().fromJson(json, CouponWrapper.class);
  }

  public static String toJson(CouponWrapper item) {
    return new Gson().toJson(item, CouponWrapper.class);
  }

  @Override
  public String toString() {
    return "ID: " + getId() //
           + " Title: " + getTitle() //
           + " Created: " + getCreated() //
        ;
  }

  // Due to the object being serializable, override equals and hashcode to make sure an object remains the same before and after the serialization
  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 31). // two random primes
        append(id.toCharArray()).
        toHashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof CouponWrapper)) {
      return false;
    }
    return id.equals(((CouponWrapper) o).getId());
  }

  public Coupon getCoupon() {
    return coupon;
  }
}
