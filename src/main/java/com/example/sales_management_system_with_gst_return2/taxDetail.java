package com.example.sales_management_system_with_gst_return2;

import java.math.BigDecimal;

public class taxDetail {

    private String GstName;
    private BigDecimal Amount;
    private BigDecimal GstRate;
    private BigDecimal TaxAmount;

    public taxDetail(String GstName, BigDecimal Amount, BigDecimal GstRate, BigDecimal TaxAmount)
    {
        this.Amount=Amount;
        this.GstName=GstName;
        this.TaxAmount=TaxAmount;
        this.GstRate=GstRate;

    }

    public String getGstName() {
        return GstName;
    }

    public void setGstName(String gstName) {
        GstName = gstName;
    }

    public BigDecimal getAmount() {
        return Amount;
    }

    public void setAmount(BigDecimal amount) {
        Amount = amount;
    }

    public BigDecimal getGstRate() {
        return GstRate;
    }

    public void setGstRate(BigDecimal gstRate) {
        GstRate = gstRate;
    }

    public BigDecimal getTaxAmount() {
        return TaxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        TaxAmount = taxAmount;
    }
}
