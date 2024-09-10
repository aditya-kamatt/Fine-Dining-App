package com.jadd.easyrestro.classes;

public class Tax {
    public boolean inUse;
    int percentTax,percentOrder;

    public Tax(boolean inUse, int percentTax, int percentOrder) {
        this.inUse = inUse;
        this.percentTax = percentTax;
        this.percentOrder = percentOrder;
    }

    public Tax(boolean inUse) {
        this.inUse = inUse;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    public int getPercentTax() {
        return percentTax;
    }

    public void setPercentTax(int percentTax) {
        this.percentTax = percentTax;
    }

    public int getPercentOrder() {
        return percentOrder;
    }

    public void setPercentOrder(int percentOrder) {
        this.percentOrder = percentOrder;
    }

    public Tax() {
    }
}
