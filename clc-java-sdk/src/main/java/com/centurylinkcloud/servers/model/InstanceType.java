package com.centurylinkcloud.servers.model;

/**
 * @author ilya.drabenia
 */
public enum InstanceType {
    STANDARD("standard"),
    HYPERSCALE("hyperscale");

    private String code;

    private InstanceType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
