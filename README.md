[![Build Status](https://jenkins.teintel.com/job/storm-mqtt-to-timeseries/badge/icon)](https://jenkins.teintel.com/job/storm-mqtt-to-timeseries)

# storm-mqtt-to-timeseries
A storm topology to pull collectd and openhab data passing through MQTT and publish the metrics to one or more timeseries database.  
At the moment the only timeseries databases bolts available are:

* KairosDB
* InfluxDB


