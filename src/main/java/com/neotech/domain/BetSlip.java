package com.neotech.domain;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "betSlips")
public class BetSlip extends BaseObject {

    @Indexed
    private String betId;
    private String name;
    @Indexed
    private OddType type;
    private double odd;
    private Amount amount;
    @Indexed
    private String ip;

    @Override
    public String toString() {
        return "BetSlip{" +
                "betId='" + betId + '\'' +
                ", name='" + name + '\'' +
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OddType getType() {
        return type;
    }

    public void setType(OddType type) {
        this.type = type;
    }

    public double getOdd() {
        return odd;
    }

    public void setOdd(double odd) {
        this.odd = odd;
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
