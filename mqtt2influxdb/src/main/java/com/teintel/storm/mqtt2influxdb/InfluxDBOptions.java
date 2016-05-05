package com.teintel.storm.mqtt2influxdb;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by teveritt on March 03, 2016.
 */
public class InfluxDBOptions implements Serializable {
    public String getUrl() {

        if (StringUtils.isBlank(url)) {
            url = "http://localhost:8086";
        }
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRetentionPolicy() {
        if (StringUtils.isBlank(retentionPolicy)) {
            retentionPolicy = "default";
        }
        return retentionPolicy;
    }

    public void setRetentionPolicy(String retentionPolicy) {
        this.retentionPolicy = retentionPolicy;
    }

    public String getAsync() {
        if (StringUtils.isBlank(async)) {
            async = "true";
        }
        return async;
    }

    public void setAsync(String async) {
        this.async = async;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String url;

    private String database;

    private String username;

    private String password;

    private String async;

    private String retentionPolicy;
}
