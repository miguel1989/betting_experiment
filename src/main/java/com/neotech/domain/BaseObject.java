package com.neotech.domain;

import com.neotech.util.CoreUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class BaseObject {

    @Id
    private String id;
    @Indexed
    private long timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void updateTimeStamp() {
        this.setTimestamp(CoreUtils.now());
    }
}
