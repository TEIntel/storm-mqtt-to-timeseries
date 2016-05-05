package com.teintel.storm.mqtt;

import java.io.Serializable;

/**
 * Created by teveritt on March 06, 2016.
 */
public interface DBMessage extends Serializable {

    public TimeSeriesDBMetricBuilder createDBMetricBuilder();
}
