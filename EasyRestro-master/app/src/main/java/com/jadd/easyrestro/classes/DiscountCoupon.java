package com.jadd.easyrestro.classes;

public class DiscountCoupon {
    String couponCode;
    String type;
    int num;

    public DiscountCoupon() {
    }

    public DiscountCoupon(String couponCode, String type, int num) {
        this.couponCode = couponCode;
        this.type = type;
        this.num = num;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
