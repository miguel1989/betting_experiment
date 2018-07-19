package com.neotech.domain;

import com.neotech.util.Constants;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.NotNull;

@Document(collection = "bets")
public class Bet extends BaseObject {

    @NotBlank(message = Constants.ERR_REQUIRED)
    private String name;
    @NotNull(message = Constants.ERR_REQUIRED)
    private Odd odd;

    @Override
    public String toString() {
        return "Bet{" +
                ", name='" + name + '\'' +
                ", odd=" + odd +
                '}';
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Odd getOdd() {
        return odd;
    }

    public void setOdd(Odd odd) {
        this.odd = odd;
    }
}
