package com.neotech.domain;

import com.neotech.util.Constants;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class Amount {
    private Currency currency = Currency.USD;
    @NotNull(message = Constants.ERR_REQUIRED)
    private BigDecimal value;

    public Amount() {

    }

    public Amount(BigDecimal val) {
        this.setValue(val);
    }
    public Amount(BigDecimal val, Currency currency) {
        this.setValue(val);
        this.setCurrency(currency);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Amount amount = (Amount) o;

        if (currency != amount.currency) return false;
        if (value != null ? !value.equals(amount.value) : amount.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = currency.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Amount{" +
                "currency='" + currency + '\'' +
                ", value=" + value +
                '}';
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
