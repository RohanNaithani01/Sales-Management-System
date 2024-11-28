package com.example.sales_management_system_with_gst_return2;

import java.math.BigDecimal;

public class productDetail {
    private String ProductName;
    private String hsn;
    private String unit;
    private BigDecimal amount;
    private int ProductId;
    private BigDecimal gst;
    private BigDecimal qty;
    private BigDecimal rate;

    //constructor
    public productDetail(int ProductId, String hsn, String unit, BigDecimal amount, BigDecimal gst, BigDecimal qty, BigDecimal rate, String ProductName )
    {
        this.amount=amount;
        this.ProductId=ProductId;
        this.ProductName=ProductName;
        this.rate=rate;
        this.gst=gst;
        this.qty=qty;
        this.hsn=hsn;
        this.unit=unit;

    }

    public int getProductId() {
        return ProductId;
    }
    public void setProductId(int productId) {
        ProductId = productId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getGst() {
        return gst;
    }

    public void setGst(BigDecimal gst) {
        this.gst = gst;
    }

    public String getHsn() {
        return hsn;
    }

    public void setHsn(String hsn) {
        this.hsn = hsn;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
