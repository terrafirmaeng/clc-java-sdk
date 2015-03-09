package com.centurylinkcloud.sample.port.adapter.web.beans;

/**
 * @author ilya.drabenia
 */
public class DataCenter {
    private String id;
    private String name;

    public DataCenter(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
