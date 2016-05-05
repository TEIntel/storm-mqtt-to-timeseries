package com.teintel.storm.mqtt2kairosdb;

import java.io.Serializable;

/**
 * Created by teveritt on March 03, 2016.
 */
public class KairosDBOptions implements Serializable {
    public String getUrl() {

        if (url == null) {
            url = "http://localhost:9090";
        }
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;
}
