package com.neotech.domain;


import com.neotech.util.Constants;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Odd {
    @NotBlank(message = Constants.ERR_REQUIRED)
    @Size(max = Constants.MAX_NAME_LENGTH, message = Constants.ERR_MAX_LENGTH)
    private String name;
    @NotNull(message = Constants.ERR_REQUIRED)
    @Min(value = 1, message = Constants.ERR_MIN_LENGTH)
    private double win;
    @NotNull(message = Constants.ERR_REQUIRED)
    @Min(value = 1, message = Constants.ERR_MIN_LENGTH)
    private double draw;
    @NotNull(message = Constants.ERR_REQUIRED)
    @Min(value = 1, message = Constants.ERR_MIN_LENGTH)
    private double lose;

    public Odd(){

    }

    public Odd(String name, double win, double draw, double lose){
        this.setName(name);
        this.setWin(win);
        this.setDraw(draw);
        this.setLose(lose);
    }

    @Override
    public String toString() {
        return "Odd{" +
                "name='" + name + '\'' +
                ", win=" + win +
                ", draw=" + draw +
                ", lose=" + lose +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWin() {
        return win;
    }

    public void setWin(double win) {
        this.win = win;
    }

    public double getDraw() {
        return draw;
    }

    public void setDraw(double draw) {
        this.draw = draw;
    }

    public double getLose() {
        return lose;
    }

    public void setLose(double lose) {
        this.lose = lose;
    }
}
