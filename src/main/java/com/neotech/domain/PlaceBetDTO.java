package com.neotech.domain;

import com.neotech.util.Constants;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class PlaceBetDTO {
    @NotBlank(message = Constants.ERR_REQUIRED)
    private String betId;
    private long timestamp;
    @NotNull(message = Constants.ERR_REQUIRED)
    private OddType type;
    @Min(value = 0, message = Constants.ERR_MIN_LENGTH)
    private double odd;
    @NotNull(message = Constants.ERR_REQUIRED)
    private Amount amount;
    private String ip;

    @Override
    public String toString() {
        return "PlaceBetDTO{" +
                "betId='" + betId + '\'' +
                ", timestamp=" + timestamp +
                ", type=" + type +
                ", odd=" + odd +
                ", amount=" + amount +
                ", ip='" + ip + '\'' +
                '}';
    }

    public String getBetId() {
        return betId;
    }

    public void setBetId(String betId) {
        this.betId = betId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getOdd() {
        return odd;
    }

    public void setOdd(double odd) {
        this.odd = odd;
    }

    public OddType getType() {
        return type;
    }

    public void setType(OddType type) {
        this.type = type;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
