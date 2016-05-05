package com.teintel.storm.mqtt2kairosdb;

import java.util.List;
import java.util.Map;

import backtype.storm.mqtt.MqttMessage;
import com.teintel.storm.mqtt.collectd.CollectdMqttMessage;
import org.junit.Test;
import org.kairosdb.client.builder.Metric;

import static org.junit.Assert.assertEquals;

/**
 * Created by teveritt on March 07, 2016.
 */
public class TestMqtt2CollectdMessageMapper {

    @Test
    public void testTime() {

        String payloadTime = "1457282892.887";
        String topic = "collectd/carlingford/ping/ping_stddev-10.0.0.8";
        String payload = payloadTime + ":3.67431847195277";

        CollectdMqttMessage message = new KairosCollectdMqttMessage(new MqttMessage(topic, payload.getBytes()));


        Long expectedTS = 1457282892887l;

        assertEquals(expectedTS, message.getTime());
    }


    @Test
    public void testInterfacePackets() {
        String topic = "collectd/carlingford/interface-eth0/if_packets";
        String payload = "1457282892.877:9.10000771906882:10.9000092459176";

        CollectdMqttMessage message = new KairosCollectdMqttMessage(new MqttMessage(topic, payload.getBytes()));

        assertEquals("carlingford", message.getHost());
        assertEquals("interface", message.getPlugin());
        assertEquals("eth0", message.getPluginInstance());
        assertEquals("if_packets", message.getType());
        assertEquals("", message.getTypeInstance());


        Map<String, Double> values = message.getValues();

        assertEquals(new Long(1457282892877l), message.getTime());
        assertEquals(new Double(9.10000771906882), values.get("rx"));
        assertEquals(new Double(10.9000092459176), values.get("tx"));

    }

    @Test
    public void testInterfacePackets2() {
        String topic = "collectd/carlingford/interface-eth0/if_packets";
        String payload = "1459362240.409:6.89932181993374:9.79903678773197";

        CollectdMqttMessage message = new KairosCollectdMqttMessage(new MqttMessage(topic, payload.getBytes()));

        assertEquals("carlingford", message.getHost());
        assertEquals("interface", message.getPlugin());
        assertEquals("eth0", message.getPluginInstance());
        assertEquals("if_packets", message.getType());
        assertEquals("", message.getTypeInstance());


        Map<String, Double> values = message.getValues();

        assertEquals(new Long(1459362240409l), message.getTime());
        assertEquals(new Double(6.89932181993374), values.get("rx"));
        assertEquals(new Double(9.79903678773197), values.get("tx"));

    }


    @Test
    public void testDfComplex() {
        String topic = "collectd/ree/df-DATA/df_complex-used";
        String payload = "1457426441.825:18570461184";

        CollectdMqttMessage message = new KairosCollectdMqttMessage(new MqttMessage(topic, payload.getBytes()));

        assertEquals("ree", message.getHost());
        assertEquals("df", message.getPlugin());
        assertEquals("data", message.getPluginInstance());
        assertEquals("complex", message.getType());
        assertEquals("used", message.getTypeInstance());

        Map<String, Double> values = message.getValues();

        assertEquals(new Long(1457426441825l), message.getTime());
        assertEquals(new Double(18570461184.0), values.get("default"));

    }

    @Test
    public void testDiskOctets() {
        String topic = "collectd/ree/disk-xvda/disk_octets";
        String payload = "1457426441.825:0:38092.4040807676";

        KairosCollectdMqttMessage message = new KairosCollectdMqttMessage(new MqttMessage(topic, payload.getBytes()));

        assertEquals("ree", message.getHost());
        assertEquals("disk", message.getPlugin());
        assertEquals("xvda", message.getPluginInstance());
        assertEquals("octets", message.getType());
        assertEquals("", message.getTypeInstance());

        Map<String, Double> values = message.getValues();

        assertEquals(new Long(1457426441825l), message.getTime());
        assertEquals(new Double(0.0), values.get("read"));
        assertEquals(new Double(38092.4040807676), values.get("write"));


        List<Metric> metrics = message.createDBMetricBuilder().build().getMetrics();
        assertEquals(2, metrics.size());
        assertEquals("ree.disk.xvda.octets.read", metrics.get(0).getName());
        assertEquals("ree.disk.xvda.octets.write", metrics.get(1).getName());

        assertEquals(new Double(0.0), metrics.get(0).getDataPoints().get(0).getValue());
        assertEquals(new Double(38092.4040807676), metrics.get(1).getDataPoints().get(0).getValue());
    }

    @Test
    public void testCpuUser_1() throws Exception {
        String topic = "collectd/carlingford/cpu-0/cpu-user";
        String payload = "1457282892.877:1.2999956387614";

        CollectdMqttMessage message = new KairosCollectdMqttMessage(new MqttMessage(topic, payload.getBytes()));

        assertEquals("carlingford", message.getHost());
        assertEquals("cpu", message.getPlugin());
        assertEquals("0", message.getPluginInstance());
        assertEquals("cpu", message.getType());
        assertEquals("user", message.getTypeInstance());

        Map<String, Double> values = message.getValues();

        assertEquals(new Long(1457282892877l), message.getTime());
        assertEquals(new Double(1.2999956387614), values.get("default"));

    }

    @Test
    public void testPing1() throws Exception {
        String topic = "collectd/axiom/ping/ping-10.0.0.4";
        String payload = "1457282892.887:6.0163";

        CollectdMqttMessage message = new KairosCollectdMqttMessage(new MqttMessage(topic, payload.getBytes()));

        assertEquals("axiom", message.getHost());
        assertEquals("ping", message.getPlugin());
        assertEquals("", message.getPluginInstance());
        assertEquals("ping", message.getType());
        assertEquals("10_0_0_4", message.getTypeInstance());

        Map<String, Double> values = message.getValues();

        assertEquals(new Long(1457282892887l), message.getTime());
        assertEquals(new Double(6.0163), values.get("default"));

    }

      /*
      Other possible scenarios

collectd/axiom/cpu-0/cpu-user 1457426548.115:0.0999999000133084
collectd/axiom/cpu-2/cpu-user 1457426548.115:0
collectd/axiom/cpu-3/cpu-user 1457426548.115:0.0999998894055666
collectd/axiom/cpu-3/cpu-system 1457426548.115:0.0999999031984253
collectd/axiom/cpu-1/cpu-system 1457426548.115:0.299999714652348
collectd/axiom/cpu-1/cpu-wait 1457426548.115:0.0999999051355725
collectd/axiom/cpu-2/cpu-wait 1457426548.115:0.399999621734381
collectd/axiom/cpu-3/cpu-wait 1457426548.115:0
collectd/axiom/cpu-0/cpu-nice 1457426548.115:0.0999999055081009
collectd/axiom/cpu-1/cpu-nice 1457426548.115:0.199999812767085
collectd/axiom/cpu-2/cpu-nice 1457426548.115:0
collectd/axiom/cpu-3/cpu-nice 1457426548.115:0.0999999109842676
collectd/axiom/cpu-0/cpu-interrupt 1457426548.115:0
collectd/axiom/cpu-1/cpu-interrupt 1457426548.115:0
collectd/axiom/cpu-2/cpu-interrupt 1457426548.115:0
collectd/axiom/cpu-3/cpu-interrupt 1457426548.115:0
collectd/axiom/cpu-0/cpu-softirq 1457426548.115:0.099999916022714
collectd/axiom/cpu-1/cpu-softirq 1457426548.115:0.0999999151100194
collectd/axiom/cpu-2/cpu-softirq 1457426548.115:0
collectd/axiom/cpu-3/cpu-softirq 1457426548.115:0
collectd/axiom/cpu-0/cpu-steal 1457426548.115:0
collectd/axiom/cpu-1/cpu-steal 1457426548.115:0
collectd/axiom/cpu-2/cpu-steal 1457426548.115:0
collectd/axiom/cpu-3/cpu-steal 1457426548.115:0
collectd/axiom/cpu-0/cpu-idle 1457426548.115:99.2034691220711
collectd/axiom/cpu-1/cpu-idle 1457426548.115:99.4034750238256
collectd/axiom/cpu-2/cpu-idle 1457426548.115:99.3034718885243
collectd/axiom/cpu-3/cpu-idle 1457426548.115:99.7034931262926
collectd/axiom/df-boot/df_complex-free 1457426548.116:197390336
collectd/axiom/df-boot/df_complex-reserved 1457426548.116:12739584
collectd/axiom/df-boot/df_complex-used 1457426548.116:36625408
collectd/axiom/disk-sda/disk_octets 1457426548.116:0:548454.603906631
collectd/axiom/disk-sda/disk_ops 1457426548.116:0:6.60000247405935
collectd/axiom/disk-sda/disk_time 1457426548.116:0:5.20000198073761
collectd/axiom/disk-sda/disk_merged 1457426548.116:0:0
collectd/axiom/disk-sda/pending_operations 1457426548.116:0
collectd/axiom/disk-sda/disk_io_time 1457426548.116:14.800007313568:34.4000169991041
collectd/axiom/disk-sda1/disk_octets 1457426548.116:0:0
collectd/axiom/disk-sda1/disk_ops 1457426548.116:0:0
collectd/axiom/disk-sda1/disk_time 1457426548.116:0:0
collectd/axiom/disk-sda1/disk_merged 1457426548.116:0:0
collectd/axiom/disk-sda1/pending_operations 1457426548.116:0
collectd/axiom/disk-sda1/disk_io_time 1457426548.116:0:0
collectd/axiom/disk-sda2/disk_octets 1457426548.116:0:0
collectd/axiom/disk-sda2/disk_ops 1457426548.116:0:0
collectd/axiom/disk-sda2/disk_time 1457426548.116:0:0
collectd/axiom/disk-sda2/disk_merged 1457426548.116:0:0
collectd/axiom/disk-sda2/pending_operations 1457426548.116:0
collectd/axiom/disk-sda2/disk_io_time 1457426548.116:0:0
collectd/axiom/disk-sda5/disk_octets 1457426548.116:0:548454.687369459
collectd/axiom/disk-sda5/disk_ops 1457426548.116:0:6.50000324957235
collectd/axiom/disk-sda5/disk_time 1457426548.116:0:3.90000162902356
collectd/axiom/disk-sda5/disk_merged 1457426548.116:0:0
collectd/axiom/disk-sda5/pending_operations 1457426548.116:0
collectd/axiom/disk-sda5/disk_io_time 1457426548.116:5.60000147804657:25.2000066512096
collectd/axiom/disk-dm-0/disk_octets 1457426548.116:0:548454.389784241
collectd/axiom/disk-dm-0/disk_ops 1457426548.116:0:6.59999969450758
collectd/axiom/disk-dm-0/disk_time 1457426548.116:0:5.4999995902181
collectd/axiom/disk-dm-0/disk_merged 1457426548.116:0:0
collectd/axiom/disk-dm-0/pending_operations 1457426548.116:0
collectd/axiom/disk-dm-0/disk_io_time 1457426548.116:15.5999973862995:35.9999939683835
collectd/axiom/disk-dm-1/disk_octets 1457426548.116:0:548454.209322919
collectd/axiom/disk-dm-1/disk_ops 1457426548.116:0:6.59999758618041
collectd/axiom/disk-dm-1/disk_time 1457426548.116:0:5.49999788501381
collectd/axiom/disk-dm-1/disk_merged 1457426548.116:0:0
collectd/axiom/disk-dm-1/pending_operations 1457426548.116:0
collectd/axiom/disk-dm-1/disk_io_time 1457426548.116:15.5999925439095:35.9999827936374
collectd/axiom/disk-dm-2/disk_octets 1457426548.116:0:0
collectd/axiom/disk-dm-2/disk_ops 1457426548.116:0:0
collectd/axiom/disk-dm-2/disk_time 1457426548.116:0:0
collectd/axiom/disk-dm-2/disk_merged 1457426548.116:0:0
collectd/axiom/disk-dm-2/pending_operations 1457426548.116:0
collectd/axiom/disk-dm-2/disk_io_time 1457426548.116:0:0
collectd/axiom/entropy/entropy 1457426548.116:780
collectd/axiom/hddtemp/temperature-sda 1457426548.116:33
collectd/axiom/interface-eth0/if_octets 1457426548.116:3826.09845672097:4859.59803985291
collectd/axiom/interface-eth0/if_packets 1457426548.116:31.3999850126986:31.4999849649684
collectd/axiom/interface-eth0/if_errors 1457426548.116:0:0
collectd/axiom/interface-lo/if_octets 1457426548.116:180.699869827787:180.699869827787
collectd/axiom/interface-lo/if_packets 1457426548.116:1.59999877333735:1.59999877333735
collectd/axiom/interface-lo/if_errors 1457426548.116:0:0
collectd/axiom/interface-tun0/if_octets 1457426548.116:788.09925905261:651.899387103663
collectd/axiom/interface-tun0/if_packets 1457426548.116:12.4999876087654:10.6999893931032
collectd/axiom/interface-tun0/if_errors 1457426548.116:0:0
collectd/axiom/load/load 1457426548.116:0.02:0.02:0.05
collectd/axiom/cpu-1/cpu-user 1457426548.115:0
collectd/axiom/cpu-2/cpu-system 1457426548.115:0.199999810215266
collectd/axiom/cpu-0/cpu-system 1457426548.115:0.199999781437457
collectd/axiom/cpu-0/cpu-wait 1457426548.115:0.0999999043532631
collectd/axiom/memory/memory-used 1457426548.117:288026624
collectd/axiom/memory/memory-slab_recl 1457426548.117:165543936
collectd/axiom/memory/memory-cached 1457426548.117:2698977280
collectd/axiom/memory/memory-free 1457426548.117:13546684416
collectd/axiom/memory/memory-slab_unrecl 1457426548.117:20164608
collectd/axiom/memory/memory-buffered 1457426548.117:1179648
collectd/axiom/ping/ping-10.0.0.4 1457426548.118:57.9632
collectd/axiom/ping/ping-nano.teintel.com 1457426548.118:57.9337
collectd/axiom/ping/ping_droprate-10.0.0.4 1457426548.118:0
collectd/axiom/ping/ping_stddev-nano.teintel.com 1457426548.118:155.345096135489
collectd/axiom/ping/ping_droprate-10.0.0.2 1457426548.118:0
collectd/axiom/ping/ping_droprate-nano.teintel.com 1457426548.118:0
collectd/axiom/ping/ping-10.0.0.2 1457426548.118:57.9049
collectd/axiom/ping/ping_stddev-10.0.0.2 1457426548.118:155.34574744918
collectd/axiom/ping/ping_stddev-10.0.0.4 1457426548.118:155.344188774333
collectd/axiom/ping/ping-10.0.0.1 1457426548.118:0.646
collectd/axiom/ping/ping_stddev-10.0.0.1 1457426548.118:0.043168918860165
collectd/axiom/ping/ping_droprate-10.0.0.1 1457426548.118:0
collectd/axiom/openvpn-openvpn-status.log/users-openvpn-status.log 1457426548.123:1
collectd/axiom/processes/ps_state-running 1457426548.130:0
collectd/axiom/processes/ps_state-sleeping 1457426548.130:155
collectd/axiom/processes/ps_state-zombies 1457426548.130:0
collectd/axiom/processes/ps_state-stopped 1457426548.130:0
collectd/axiom/processes/ps_state-paging 1457426548.130:0
collectd/axiom/processes/ps_state-blocked 1457426548.130:0
collectd/axiom/processes/fork_rate 1457426548.130:0
collectd/ree/cpu-0/cpu-user 1457426431.824:3.79998729352431
collectd/ree/cpu-1/cpu-user 1457426431.824:3.19998954537946
collectd/ree/cpu-0/cpu-system 1457426431.824:0.699997736651318
collectd/ree/cpu-1/cpu-system 1457426431.824:0.899997100206122
collectd/ree/cpu-0/cpu-wait 1457426431.824:0.29999904566748
collectd/ree/cpu-1/cpu-wait 1457426431.824:0.0999996807995197
collectd/ree/cpu-0/cpu-nice 1457426431.824:0
collectd/ree/cpu-1/cpu-nice 1457426431.824:0
collectd/ree/cpu-0/cpu-interrupt 1457426431.824:0
collectd/ree/cpu-1/cpu-interrupt 1457426431.824:0
collectd/ree/cpu-0/cpu-softirq 1457426431.824:0.0999995455445886
collectd/ree/cpu-1/cpu-softirq 1457426431.824:0.0999995466807918
collectd/ree/cpu-0/cpu-steal 1457426431.824:0.299998641886377
collectd/ree/cpu-1/cpu-steal 1457426431.824:0.199999101389512
collectd/ree/cpu-0/cpu-idle 1457426431.824:91.999589432373
collectd/ree/cpu-1/cpu-idle 1457426431.824:92.7995883599302
collectd/ree/df-DATA/df_complex-free 1457426431.825:2415374336
collectd/ree/df-DATA/df_complex-reserved 1457426431.825:16777216
collectd/ree/df-DATA/df_complex-used 1457426431.825:18569379840
collectd/ree/entropy/entropy 1457426431.825:885
collectd/ree/disk-xvda/disk_octets 1457426431.825:18841.9085762206:130664.539909008
collectd/ree/disk-xvda/disk_ops 1457426431.825:1.20001991986767:7.80012947913985
collectd/ree/disk-xvda/disk_time 1457426431.825:0.300005002738512:5.40009004929322
collectd/ree/disk-xvda/disk_merged 1457426431.825:0:12.1002031576191
collectd/ree/disk-xvda/pending_operations 1457426431.825:0
collectd/ree/disk-xvda/disk_io_time 1457426431.825:4.40007456641349:42.8007253278403
collectd/ree/disk-xvda1/disk_octets 1457426431.825:18841.919435042:130664.615212574
collectd/ree/disk-xvda1/disk_ops 1457426431.825:1.20002037898786:7.80013246342112
collectd/ree/disk-xvda1/disk_time 1457426431.825:0.300005100195388:5.40009180351699
collectd/ree/disk-xvda1/disk_merged 1457426431.825:0:12.1002054272729
collectd/ree/disk-xvda1/pending_operations 1457426431.825:0
collectd/ree/disk-xvda1/disk_io_time 1457426431.825:4.40007472705346:42.8007268904291
collectd/ree/disk-xvdf/disk_octets 1457426431.825:0:1228.82090401924
collectd/ree/disk-xvdf/disk_ops 1457426431.825:0:0.300005108381992
collectd/ree/disk-xvdf/disk_time 1457426431.825:0:0
collectd/ree/disk-xvdf/disk_merged 1457426431.825:0:0
collectd/ree/disk-xvdf/pending_operations 1457426431.825:0
collectd/ree/disk-xvdf/disk_io_time 1457426431.825:0:0
collectd/ree/disk-dm-0/disk_octets 1457426431.825:0:1228.82095654941
collectd/ree/disk-dm-0/disk_ops 1457426431.825:0:0.300005110980471
collectd/ree/disk-dm-0/disk_time 1457426431.825:0:0
collectd/ree/disk-dm-0/disk_merged 1457426431.825:0:0
collectd/ree/disk-dm-0/pending_operations 1457426431.825:0
collectd/ree/disk-dm-0/disk_io_time 1457426431.825:0:0
collectd/ree/interface-eth0/if_octets 1457426431.825:2184.22931171602:1301.8174700082
collectd/ree/interface-eth0/if_packets 1457426431.825:6.40008599754493:5.70007659156346
collectd/ree/interface-eth0/if_errors 1457426431.825:0:0
collectd/ree/interface-lo/if_octets 1457426431.825:78239.0481452681:78239.0481452681
collectd/ree/interface-lo/if_packets 1457426431.825:471.106307454002:471.106307454002
collectd/ree/interface-lo/if_errors 1457426431.825:0:0
collectd/ree/irq/irq-0 1457426431.825:0
collectd/ree/irq/irq-1 1457426431.825:0
collectd/ree/irq/irq-4 1457426431.825:0
collectd/ree/irq/irq-6 1457426431.825:0
collectd/ree/irq/irq-8 1457426431.825:0
collectd/ree/irq/irq-9 1457426431.825:0
collectd/ree/irq/irq-12 1457426431.825:0
collectd/ree/irq/irq-14 1457426431.825:0
collectd/ree/irq/irq-15 1457426431.825:0
collectd/ree/irq/irq-64 1457426431.825:1167.82782284644
collectd/ree/irq/irq-65 1457426431.825:356.808494516035
collectd/ree/irq/irq-66 1457426431.825:0
collectd/ree/irq/irq-67 1457426431.825:0
collectd/ree/irq/irq-68 1457426431.825:0.800019036984446
collectd/ree/irq/irq-69 1457426431.825:1163.32765886064
collectd/ree/irq/irq-70 1457426431.825:352.808385715423
collectd/ree/irq/irq-71 1457426431.825:0
collectd/ree/irq/irq-72 1457426431.825:0
collectd/ree/irq/irq-73 1457426431.825:2.60005854637876
collectd/ree/irq/irq-74 1457426431.825:0
collectd/ree/irq/irq-75 1457426431.825:0
collectd/ree/irq/irq-76 1457426431.825:9.00020269574745
collectd/ree/irq/irq-77 1457426431.825:0.300006737664785
collectd/ree/irq/irq-78 1457426431.825:12.1002719125067
collectd/ree/irq/irq-NMI 1457426431.825:0
collectd/ree/irq/irq-LOC 1457426431.825:0
collectd/ree/irq/irq-SPU 1457426431.825:0
collectd/ree/irq/irq-PMI 1457426431.825:0
collectd/ree/irq/irq-IWI 1457426431.825:0
collectd/ree/irq/irq-RTR 1457426431.825:0
collectd/ree/irq/irq-RES 1457426431.825:709.614994518576
collectd/ree/irq/irq-CAL 1457426431.825:0
collectd/ree/irq/irq-TLB 1457426431.825:3.40007202317767
collectd/ree/irq/irq-TRM 1457426431.825:0
collectd/ree/irq/irq-THR 1457426431.825:0
collectd/ree/irq/irq-MCE 1457426431.825:0
collectd/ree/irq/irq-MCP 1457426431.825:0
collectd/ree/irq/irq-HYP 1457426431.825:3020.96217110118
collectd/ree/irq/irq-ERR 1457426431.825:0
collectd/ree/irq/irq-MIS 1457426431.825:0
collectd/ree/load/load 1457426431.825:1.54:0.46:0.32
collectd/ree/memory/memory-used 1457426431.826:3127140352
collectd/ree/memory/memory-buffered 1457426431.826:32739328
collectd/ree/memory/memory-cached 1457426431.826:797646848
collectd/ree/memory/memory-free 1457426431.826:115974144
collectd/ree/memory/memory-slab_unrecl 1457426431.826:20537344
collectd/ree/memory/memory-slab_recl 1457426431.826:60928000
collectd/ree/ping/ping-botharbrugha.teintel.com 1457426431.827:nan
collectd/ree/ping/ping_stddev-botharbrugha.teintel.com 1457426431.827:nan
collectd/ree/ping/ping_droprate-botharbrugha.teintel.com 1457426431.827:1
collectd/ree/users/users 1457426431.828:1
collectd/ree/swap/swap-used 1457426431.831:0
collectd/ree/swap/swap-free 1457426431.831:0
collectd/ree/swap/swap-cached 1457426431.831:0
collectd/ree/swap/swap_io-in 1457426431.832:0
collectd/ree/swap/swap_io-out 1457426431.832:0
collectd/ree/processes/ps_state-running 1457426431.842:0
collectd/ree/processes/ps_state-sleeping 1457426431.842:129
collectd/ree/processes/ps_state-zombies 1457426431.842:0
collectd/ree/processes/ps_state-stopped 1457426431.843:0
collectd/ree/processes/ps_state-paging 1457426431.843:0
collectd/ree/processes/ps_state-blocked 1457426431.843:0
collectd/ree/processes/fork_rate 1457426431.843:0.0999869444131983
collectd/carlingford/cpu-0/cpu-user 1457426432.876:0.700000024707989
collectd/carlingford/cpu-2/cpu-user 1457426432.877:2.8999998457823
collectd/carlingford/cpu-3/cpu-user 1457426432.877:0
collectd/carlingford/cpu-0/cpu-system 1457426432.877:0.400000191219242
collectd/carlingford/cpu-2/cpu-system 1457426432.877:7.00000684653147
collectd/carlingford/cpu-3/cpu-system 1457426432.877:0.100000031655664
collectd/carlingford/cpu-2/cpu-wait 1457426432.877:0
collectd/carlingford/cpu-3/cpu-wait 1457426432.877:0
collectd/carlingford/cpu-0/cpu-nice 1457426432.877:0
collectd/carlingford/cpu-1/cpu-nice 1457426432.877:0
collectd/carlingford/cpu-2/cpu-nice 1457426432.877:0
collectd/carlingford/cpu-3/cpu-nice 1457426432.877:0
collectd/carlingford/cpu-0/cpu-interrupt 1457426432.877:0
collectd/carlingford/cpu-1/cpu-interrupt 1457426432.877:0
collectd/carlingford/cpu-2/cpu-interrupt 1457426432.877:0
collectd/carlingford/cpu-3/cpu-interrupt 1457426432.877:0
collectd/carlingford/cpu-0/cpu-softirq 1457426432.877:0
collectd/carlingford/cpu-1/cpu-softirq 1457426432.877:0
collectd/carlingford/cpu-2/cpu-softirq 1457426432.877:0
collectd/carlingford/cpu-3/cpu-softirq 1457426432.877:0.100000046240187
collectd/carlingford/cpu-0/cpu-steal 1457426432.877:0
collectd/carlingford/cpu-1/cpu-steal 1457426432.877:0
collectd/carlingford/cpu-2/cpu-steal 1457426432.877:0
collectd/carlingford/cpu-3/cpu-steal 1457426432.877:0
collectd/carlingford/cpu-1/cpu-user 1457426432.877:0.299999927803891
collectd/carlingford/cpu-1/cpu-idle 1457426432.877:99.6000512405003
collectd/carlingford/cpu-2/cpu-idle 1457426432.877:93.1000498387112
collectd/carlingford/cpu-3/cpu-idle 1457426432.877:99.8000570595777
collectd/carlingford/interface-lo/if_octets 1457426432.877:781.473969812824:781.473969812824
collectd/carlingford/interface-lo/if_packets 1457426432.878:5.40051132153908:5.40051132153908
collectd/carlingford/interface-lo/if_errors 1457426432.878:0:0
collectd/carlingford/interface-eth0/if_octets 1457426432.878:795.694942784144:1879.72428979738
collectd/carlingford/interface-eth0/if_packets 1457426432.878:8.40100460036638:9.80117203376077
collectd/carlingford/interface-eth0/if_errors 1457426432.878:0:0
collectd/carlingford/load/load 1457426432.879:0.15:0.13:0.14
collectd/carlingford/memory/memory-used 1457426432.880:171114496
collectd/carlingford/memory/memory-buffered 1457426432.880:52183040
collectd/carlingford/memory/memory-cached 1457426432.880:648093696
collectd/carlingford/memory/memory-free 1457426432.880:66183168
collectd/carlingford/memory/memory-slab_unrecl 1457426432.880:8925184
collectd/carlingford/memory/memory-slab_recl 1457426432.880:24313856
collectd/carlingford/cpu-1/cpu-system 1457426432.877:0
collectd/carlingford/cpu-0/cpu-wait 1457426432.877:0
collectd/carlingford/cpu-1/cpu-wait 1457426432.877:0
collectd/carlingford/cpu-0/cpu-idle 1457426432.877:98.6000507260375
collectd/carlingford/ping/ping-ree.teintel.com 1457426432.886:nan
collectd/carlingford/ping/ping_stddev-ree.teintel.com 1457426432.887:nan
collectd/carlingford/ping/ping-parkvale.mine.nu 1457426432.887:nan
collectd/carlingford/ping/ping_stddev-parkvale.mine.nu 1457426432.887:nan
collectd/carlingford/ping/ping_droprate-ree.teintel.com 1457426432.887:1
collectd/carlingford/ping/ping_droprate-parkvale.mine.nu 1457426432.887:1
collectd/carlingford/ping/ping-10.0.0.9 1457426432.887:6.624
collectd/carlingford/ping/ping_stddev-10.0.0.9 1457426432.887:4.68403681549248
collectd/carlingford/ping/ping_droprate-10.0.0.9 1457426432.888:0
collectd/carlingford/ping/ping-10.0.0.8 1457426432.888:6.5175
collectd/carlingford/ping/ping_stddev-10.0.0.8 1457426432.888:4.68401535366115
collectd/carlingford/ping/ping_droprate-10.0.0.8 1457426432.888:0
collectd/carlingford/ping/ping-10.0.0.4 1457426432.888:6.4205
collectd/carlingford/ping/ping_stddev-10.0.0.4 1457426432.888:4.6843153715351
collectd/carlingford/ping/ping_droprate-10.0.0.4 1457426432.888:0
collectd/carlingford/ping/ping-10.0.0.3 1457426432.888:0.9998
collectd/carlingford/ping/ping_stddev-10.0.0.3 1457426432.888:0.594519563270303
collectd/carlingford/ping/ping_droprate-10.0.0.3 1457426432.888:0
collectd/carlingford/ping/ping-10.0.0.2 1457426432.888:6.2164
collectd/carlingford/ping/ping-10.0.0.1 1457426432.888:0.7614
collectd/carlingford/ping/ping_droprate-10.0.0.2 1457426432.888:0
collectd/carlingford/ping/ping_droprate-10.0.0.1 1457426432.888:0
collectd/carlingford/ping/ping_stddev-10.0.0.1 1457426432.888:0.616843974509672
collectd/carlingford/ping/ping_stddev-10.0.0.2 1457426432.888:4.68405610555638
collectd/axiom/cpu-0/cpu-user 1457426558.115:0
collectd/axiom/cpu-2/cpu-user 1457426558.115:0.100000068852725
collectd/axiom/cpu-1/cpu-user 1457426558.115:0
collectd/axiom/cpu-0/cpu-system 1457426558.115:0
collectd/axiom/cpu-1/cpu-system 1457426558.115:0.100000062473157
collectd/axiom/cpu-3/cpu-user 1457426558.115:0
collectd/axiom/cpu-2/cpu-system 1457426558.115:0.10000006351624
collectd/axiom/cpu-3/cpu-wait 1457426558.115:0
collectd/axiom/cpu-0/cpu-nice 1457426558.115:0.200000140126893
collectd/axiom/cpu-1/cpu-nice 1457426558.115:0.200000137053524
collectd/axiom/cpu-2/cpu-nice 1457426558.115:0.100000069839929
collectd/axiom/cpu-3/cpu-nice 1457426558.115:0
collectd/axiom/cpu-0/cpu-interrupt 1457426558.115:0
collectd/axiom/cpu-1/cpu-interrupt 1457426558.115:0
collectd/axiom/cpu-2/cpu-interrupt 1457426558.115:0
collectd/axiom/cpu-3/cpu-interrupt 1457426558.115:0
collectd/axiom/cpu-0/cpu-softirq 1457426558.115:0
collectd/axiom/cpu-1/cpu-softirq 1457426558.115:0
collectd/axiom/cpu-2/cpu-softirq 1457426558.115:0
collectd/axiom/cpu-3/cpu-softirq 1457426558.115:0
collectd/axiom/cpu-0/cpu-steal 1457426558.115:0
collectd/axiom/cpu-1/cpu-steal 1457426558.115:0
collectd/axiom/cpu-2/cpu-steal 1457426558.115:0
collectd/axiom/cpu-3/cpu-steal 1457426558.115:0
collectd/axiom/cpu-0/cpu-idle 1457426558.115:99.2000887657006
collectd/axiom/cpu-1/cpu-idle 1457426558.115:99.5000936860169
collectd/axiom/cpu-2/cpu-idle 1457426558.115:99.9000952070276
collectd/axiom/cpu-3/cpu-idle 1457426558.115:99.8000942566205
collectd/axiom/disk-sda/disk_octets 1457426558.116:0:0
collectd/axiom/disk-sda/disk_ops 1457426558.116:0:0
collectd/axiom/disk-sda/disk_time 1457426558.116:0:0
collectd/axiom/df-boot/df_complex-free 1457426558.116:197390336
collectd/axiom/disk-sda/disk_merged 1457426558.116:0:0
collectd/axiom/disk-sda/pending_operations 1457426558.116:0
collectd/axiom/df-boot/df_complex-reserved 1457426558.116:12739584
collectd/axiom/disk-sda/disk_io_time 1457426558.116:0:0
collectd/axiom/df-boot/df_complex-used 1457426558.116:36625408
collectd/axiom/disk-sda1/disk_octets 1457426558.116:0:0
collectd/axiom/disk-sda1/disk_ops 1457426558.116:0:0
collectd/axiom/disk-sda1/disk_time 1457426558.116:0:0
collectd/axiom/disk-sda1/disk_merged 1457426558.116:0:0
collectd/axiom/disk-sda1/pending_operations 1457426558.116:0
collectd/axiom/disk-sda1/disk_io_time 1457426558.116:0:0
collectd/axiom/disk-sda2/disk_octets 1457426558.116:0:0
collectd/axiom/disk-sda2/disk_ops 1457426558.116:0:0
collectd/axiom/disk-sda2/disk_time 1457426558.116:0:0
collectd/axiom/disk-sda2/disk_merged 1457426558.116:0:0
collectd/axiom/disk-sda2/pending_operations 1457426558.116:0
collectd/axiom/disk-sda2/disk_io_time 1457426558.116:0:0
collectd/axiom/disk-sda5/disk_octets 1457426558.116:0:0
collectd/axiom/disk-sda5/disk_ops 1457426558.116:0:0
collectd/axiom/disk-sda5/disk_time 1457426558.116:0:0
collectd/axiom/disk-sda5/disk_merged 1457426558.116:0:0
collectd/axiom/disk-sda5/pending_operations 1457426558.116:0
collectd/axiom/disk-sda5/disk_io_time 1457426558.116:0:0
collectd/axiom/disk-dm-0/disk_octets 1457426558.116:0:0
collectd/axiom/disk-dm-0/disk_ops 1457426558.116:0:0
collectd/axiom/disk-dm-0/disk_time 1457426558.116:0:0
collectd/axiom/disk-dm-0/disk_merged 1457426558.116:0:0
collectd/axiom/disk-dm-0/pending_operations 1457426558.116:0
collectd/axiom/disk-dm-0/disk_io_time 1457426558.116:0:0
collectd/axiom/disk-dm-1/disk_octets 1457426558.116:0:0
collectd/axiom/disk-dm-1/disk_ops 1457426558.116:0:0
collectd/axiom/disk-dm-1/disk_time 1457426558.116:0:0
collectd/axiom/disk-dm-1/disk_merged 1457426558.116:0:0
collectd/axiom/disk-dm-1/pending_operations 1457426558.116:0
collectd/axiom/disk-dm-1/disk_io_time 1457426558.116:0:0
collectd/axiom/disk-dm-2/disk_octets 1457426558.116:0:0
collectd/axiom/disk-dm-2/disk_ops 1457426558.116:0:0
collectd/axiom/disk-dm-2/disk_time 1457426558.116:0:0
collectd/axiom/disk-dm-2/disk_merged 1457426558.116:0:0
collectd/axiom/disk-dm-2/pending_operations 1457426558.116:0
collectd/axiom/disk-dm-2/disk_io_time 1457426558.116:0:0
collectd/axiom/entropy/entropy 1457426558.116:797
collectd/axiom/hddtemp/temperature-sda 1457426558.116:33
collectd/axiom/interface-eth0/if_octets 1457426558.116:4892.70573960333:5837.30684770914
collectd/axiom/interface-eth0/if_packets 1457426558.116:18.5000228859302:18.4000227622224
collectd/axiom/interface-eth0/if_errors 1457426558.116:0:0
collectd/axiom/interface-lo/if_octets 1457426558.116:180.700264972977:180.700264972977
collectd/axiom/interface-lo/if_packets 1457426558.116:1.60000238106009:1.60000238106009
collectd/axiom/interface-lo/if_errors 1457426558.116:0:0
collectd/axiom/interface-tun0/if_octets 1457426558.116:477.900765581715:2922.90468239965
collectd/axiom/interface-tun0/if_packets 1457426558.116:4.80000792281147:4.7000077577529
collectd/axiom/interface-tun0/if_errors 1457426558.116:0:0
collectd/axiom/load/load 1457426558.116:0.01:0.02:0.05
collectd/axiom/cpu-3/cpu-system 1457426558.115:0.100000062808434
collectd/axiom/cpu-0/cpu-wait 1457426558.115:0.100000065490646
collectd/axiom/cpu-1/cpu-wait 1457426558.115:0
collectd/axiom/cpu-2/cpu-wait 1457426558.115:0
collectd/axiom/memory/memory-cached 1457426558.117:2698977280
collectd/axiom/memory/memory-buffered 1457426558.117:1179648
collectd/axiom/memory/memory-used 1457426558.117:288071680
collectd/axiom/memory/memory-slab_unrecl 1457426558.117:20033536
collectd/axiom/memory/memory-slab_recl 1457426558.117:165502976
collectd/axiom/memory/memory-free 1457426558.117:13546811392
collectd/axiom/ping/ping-10.0.0.4 1457426558.118:4.6936
collectd/axiom/ping/ping_stddev-10.0.0.4 1457426558.118:1.05449263208005
collectd/axiom/ping/ping_droprate-10.0.0.4 1457426558.118:0
collectd/axiom/ping/ping-nano.teintel.com 1457426558.118:4.6657
collectd/axiom/ping/ping_droprate-10.0.0.2 1457426558.118:0
collectd/axiom/ping/ping-10.0.0.1 1457426558.118:0.6518
collectd/axiom/ping/ping_droprate-nano.teintel.com 1457426558.118:0
collectd/axiom/ping/ping-10.0.0.2 1457426558.118:4.6356
collectd/axiom/ping/ping_droprate-10.0.0.1 1457426558.118:0
collectd/axiom/ping/ping_stddev-nano.teintel.com 1457426558.118:1.05420049221094
collectd/axiom/ping/ping_stddev-10.0.0.1 1457426558.118:0.0329504678773051
collectd/axiom/ping/ping_stddev-10.0.0.2 1457426558.118:1.05242779641487
collectd/axiom/openvpn-openvpn-status.log/users-openvpn-status.log 1457426558.122:1
collectd/axiom/processes/ps_state-running 1457426558.131:0
collectd/axiom/processes/ps_state-sleeping 1457426558.131:155
collectd/axiom/processes/ps_state-paging 1457426558.131:0
collectd/axiom/processes/ps_state-zombies 1457426558.131:0
collectd/axiom/processes/ps_state-blocked 1457426558.131:0
collectd/axiom/processes/ps_state-stopped 1457426558.131:0
collectd/axiom/processes/fork_rate 1457426558.131:0
collectd/ree/cpu-0/cpu-user 1457426441.824:3.49999716608083
collectd/ree/cpu-1/cpu-user 1457426441.824:3.69999678188283
collectd/ree/cpu-0/cpu-system 1457426441.824:0.499999556504583
collectd/ree/cpu-1/cpu-system 1457426441.824:0.599999462273454
collectd/ree/cpu-0/cpu-wait 1457426441.824:0.099999902555814
collectd/ree/cpu-1/cpu-wait 1457426441.824:0.0999999054987877
collectd/ree/cpu-0/cpu-nice 1457426441.824:0.100000034095731
collectd/ree/cpu-1/cpu-nice 1457426441.824:0.100000037280857
collectd/ree/cpu-0/cpu-interrupt 1457426441.824:0
collectd/ree/cpu-1/cpu-interrupt 1457426441.824:0
collectd/ree/cpu-0/cpu-softirq 1457426441.824:0.200000064428916
collectd/ree/cpu-1/cpu-softirq 1457426441.824:0.20000006351622
collectd/ree/cpu-0/cpu-steal 1457426441.824:0.300000094156742
collectd/ree/cpu-1/cpu-steal 1457426441.824:0.300000092899456
collectd/ree/cpu-0/cpu-idle 1457426441.824:92.6000287267096
collectd/ree/cpu-1/cpu-idle 1457426441.824:92.8000285208313
collectd/ree/df-DATA/df_complex-free 1457426441.825:2414292992
collectd/ree/df-DATA/df_complex-reserved 1457426441.825:16777216
collectd/ree/df-DATA/df_complex-used 1457426441.825:18570461184
collectd/ree/disk-xvda/disk_octets 1457426441.825:0:38092.4040807676
collectd/ree/disk-xvda/disk_ops 1457426441.825:0:2.59997246635433
collectd/ree/disk-xvda/disk_time 1457426441.825:0:0.599993138820162
collectd/ree/disk-xvda/disk_merged 1457426441.825:0:5.99993062881866
collectd/ree/disk-xvda/pending_operations 1457426441.825:0
collectd/ree/disk-xvda/disk_io_time 1457426441.825:1.59998135618239:1.59998135618239
collectd/ree/disk-xvda1/disk_octets 1457426441.825:0:38092.3251327585
collectd/ree/disk-xvda1/disk_ops 1457426441.825:0:2.59996755821132
collectd/ree/disk-xvda1/disk_time 1457426441.825:0:0.59999250611337
collectd/ree/disk-xvda1/disk_merged 1457426441.825:0:5.99992518462399
collectd/ree/disk-xvda1/pending_operations 1457426441.825:0
collectd/ree/disk-xvda1/disk_io_time 1457426441.825:1.59998003701442:1.59998003701442
collectd/ree/disk-xvdf/disk_octets 1457426441.825:0:2457.56933937178
collectd/ree/disk-xvdf/disk_ops 1457426441.825:0:0.599992522709124
collectd/ree/disk-xvdf/disk_time 1457426441.825:0:0
collectd/ree/disk-xvdf/disk_merged 1457426441.825:0:0
collectd/ree/disk-xvdf/pending_operations 1457426441.825:0
collectd/ree/disk-xvdf/disk_io_time 1457426441.825:0:0
collectd/ree/disk-dm-0/disk_octets 1457426441.825:0:2457.56933502313
collectd/ree/disk-dm-0/disk_ops 1457426441.825:0:0.599992533214181
collectd/ree/disk-dm-0/disk_time 1457426441.825:0:0
collectd/ree/disk-dm-0/disk_merged 1457426441.825:0:0
collectd/ree/disk-dm-0/pending_operations 1457426441.825:0
collectd/ree/disk-dm-0/disk_io_time 1457426441.825:0:0
collectd/ree/entropy/entropy 1457426441.825:780
collectd/ree/irq/irq-0 1457426441.826:0
collectd/ree/irq/irq-1 1457426441.826:0
collectd/ree/irq/irq-4 1457426441.826:0
collectd/ree/irq/irq-6 1457426441.826:0
collectd/ree/irq/irq-8 1457426441.826:0
collectd/ree/irq/irq-9 1457426441.826:0
collectd/ree/irq/irq-12 1457426441.826:0
collectd/ree/irq/irq-14 1457426441.826:0
collectd/ree/irq/irq-15 1457426441.826:0
collectd/ree/irq/irq-64 1457426441.826:1219.43310244111
collectd/ree/irq/irq-65 1457426441.826:335.78158436245
collectd/ree/irq/irq-66 1457426441.826:0
collectd/ree/irq/irq-67 1457426441.826:0
collectd/ree/irq/irq-68 1457426441.826:1.09993969487966
collectd/ree/irq/irq-69 1457426441.826:1105.1394202986
collectd/ree/irq/irq-70 1457426441.826:324.882191237177
collectd/ree/irq/irq-71 1457426441.826:0
collectd/ree/irq/irq-72 1457426441.826:0
collectd/ree/irq/irq-73 1457426441.826:1.69990848097997
collectd/ree/irq/irq-74 1457426441.826:0
collectd/ree/irq/irq-75 1457426441.826:0
collectd/ree/irq/irq-76 1457426441.826:2.49986523883448
collectd/ree/irq/irq-77 1457426441.826:0.599967689670934
collectd/ree/irq/irq-78 1457426441.826:10.6994240502264
collectd/ree/irq/irq-NMI 1457426441.826:0
collectd/ree/irq/irq-LOC 1457426441.826:0
collectd/ree/irq/irq-SPU 1457426441.826:0
collectd/ree/irq/irq-PMI 1457426441.826:0
collectd/ree/irq/irq-IWI 1457426441.826:0
collectd/ree/irq/irq-RTR 1457426441.826:0
collectd/ree/irq/irq-RES 1457426441.826:660.665319467461
collectd/ree/irq/irq-CAL 1457426441.826:0
collectd/ree/irq/irq-TLB 1457426441.826:2.79985310691873
collectd/ree/irq/irq-TRM 1457426441.826:0
collectd/ree/irq/irq-THR 1457426441.826:0
collectd/ree/irq/irq-MCE 1457426441.826:0
collectd/ree/irq/irq-MCP 1457426441.826:0
collectd/ree/irq/irq-HYP 1457426441.826:2958.44671092879
collectd/ree/irq/irq-ERR 1457426441.826:0
collectd/ree/irq/irq-MIS 1457426441.826:0
collectd/ree/interface-eth0/if_octets 1457426441.825:2017.59753733787:2388.2970848652
collectd/ree/interface-eth0/if_packets 1457426441.826:5.49951228903691:5.79948568662074
collectd/ree/interface-eth0/if_errors 1457426441.826:0:0
collectd/ree/interface-lo/if_octets 1457426441.826:79325.3567042787:79325.3567042787
collectd/ree/interface-lo/if_packets 1457426441.826:475.357781311198:475.357781311198
collectd/ree/interface-lo/if_errors 1457426441.826:0:0
collectd/ree/load/load 1457426441.826:1.63:0.52:0.33
collectd/ree/memory/memory-used 1457426441.827:3126816768
collectd/ree/memory/memory-buffered 1457426441.827:32780288
collectd/ree/memory/memory-cached 1457426441.827:798887936
collectd/ree/memory/memory-free 1457426441.827:115011584
collectd/ree/memory/memory-slab_unrecl 1457426441.827:20541440
collectd/ree/memory/memory-slab_recl 1457426441.827:60928000
collectd/ree/ping/ping-botharbrugha.teintel.com 1457426441.827:nan
collectd/ree/ping/ping_stddev-botharbrugha.teintel.com 1457426441.827:nan
collectd/ree/ping/ping_droprate-botharbrugha.teintel.com 1457426441.827:1
collectd/ree/swap/swap-used 1457426441.828:0
collectd/ree/swap/swap-free 1457426441.828:0
collectd/ree/swap/swap-cached 1457426441.828:0
collectd/ree/users/users 1457426441.828:1
collectd/ree/swap/swap_io-in 1457426441.829:0
collectd/ree/swap/swap_io-out 1457426441.829:0
collectd/ree/processes/ps_state-running 1457426441.842:0
collectd/ree/processes/ps_state-sleeping 1457426441.842:129
collectd/ree/processes/ps_state-zombies 1457426441.842:0
collectd/ree/processes/ps_state-stopped 1457426441.842:0
collectd/ree/processes/ps_state-paging 1457426441.842:0
collectd/ree/processes/ps_state-blocked 1457426441.842:0
collectd/ree/processes/fork_rate 1457426441.842:0
collectd/carlingford/cpu-0/cpu-user 1457426442.877:2.99998434212781
collectd/carlingford/cpu-1/cpu-user 1457426442.877:0.199998979070779
collectd/carlingford/cpu-3/cpu-user 1457426442.877:0.199998944686706
collectd/carlingford/cpu-0/cpu-system 1457426442.877:5.19996950167424
collectd/carlingford/cpu-1/cpu-system 1457426442.877:0
collectd/carlingford/cpu-2/cpu-system 1457426442.877:1.29999196232609
collectd/carlingford/cpu-3/cpu-system 1457426442.877:0.0999994468253904
collectd/carlingford/cpu-1/cpu-wait 1457426442.877:0
collectd/carlingford/cpu-2/cpu-wait 1457426442.877:0
collectd/carlingford/cpu-0/cpu-wait 1457426442.877:0
collectd/carlingford/cpu-2/cpu-nice 1457426442.877:0
collectd/carlingford/cpu-3/cpu-nice 1457426442.877:0
collectd/carlingford/cpu-0/cpu-interrupt 1457426442.877:0
collectd/carlingford/cpu-1/cpu-interrupt 1457426442.877:0
collectd/carlingford/cpu-2/cpu-interrupt 1457426442.877:0
collectd/carlingford/cpu-3/cpu-interrupt 1457426442.877:0
collectd/carlingford/cpu-0/cpu-softirq 1457426442.877:0
collectd/carlingford/cpu-1/cpu-softirq 1457426442.877:0
collectd/carlingford/cpu-2/cpu-softirq 1457426442.877:0
collectd/carlingford/cpu-3/cpu-softirq 1457426442.877:0
collectd/carlingford/cpu-0/cpu-steal 1457426442.877:0
collectd/carlingford/cpu-1/cpu-steal 1457426442.877:0
collectd/carlingford/cpu-2/cpu-steal 1457426442.877:0
collectd/carlingford/cpu-3/cpu-steal 1457426442.877:0
collectd/carlingford/cpu-0/cpu-idle 1457426442.877:92.799465384583
collectd/carlingford/cpu-1/cpu-idle 1457426442.877:99.599427768523
collectd/carlingford/cpu-2/cpu-idle 1457426442.877:98.8994266414634
collectd/carlingford/cpu-3/cpu-idle 1457426442.877:99.7994198623682
collectd/carlingford/interface-lo/if_octets 1457426442.878:818.267639868987:818.267639868987
collectd/carlingford/interface-lo/if_packets 1457426442.878:6.19975501095397:6.19975501095397
collectd/carlingford/interface-lo/if_errors 1457426442.878:0:0
collectd/carlingford/interface-eth0/if_octets 1457426442.878:766.469840083346:1892.12554651756
collectd/carlingford/interface-eth0/if_packets 1457426442.878:7.89968886576394:9.79961403601097
collectd/carlingford/interface-eth0/if_errors 1457426442.878:0:0
collectd/carlingford/load/load 1457426442.879:0.13:0.12:0.14
collectd/carlingford/memory/memory-used 1457426442.880:171114496
collectd/carlingford/memory/memory-buffered 1457426442.880:52183040
collectd/carlingford/memory/memory-cached 1457426442.880:648093696
collectd/carlingford/memory/memory-free 1457426442.880:66183168
collectd/carlingford/memory/memory-slab_unrecl 1457426442.880:8925184
collectd/carlingford/memory/memory-slab_recl 1457426442.880:24313856
collectd/carlingford/cpu-0/cpu-nice 1457426442.877:0
collectd/carlingford/cpu-2/cpu-user 1457426442.877:0.399997951845881
collectd/carlingford/cpu-1/cpu-nice 1457426442.877:0
collectd/carlingford/cpu-3/cpu-wait 1457426442.877:0
collectd/carlingford/ping/ping-ree.teintel.com 1457426442.886:nan
collectd/carlingford/ping/ping_stddev-ree.teintel.com 1457426442.886:nan
collectd/carlingford/ping/ping_droprate-ree.teintel.com 1457426442.886:1
collectd/carlingford/ping/ping_stddev-10.0.0.9 1457426442.887:1.0710251786645
collectd/carlingford/ping/ping_droprate-10.0.0.9 1457426442.887:0
collectd/carlingford/ping/ping-10.0.0.8 1457426442.887:5.633
collectd/carlingford/ping/ping_stddev-10.0.0.8 1457426442.887:1.06702629354253
collectd/carlingford/ping/ping_droprate-10.0.0.8 1457426442.887:0
collectd/carlingford/ping/ping-10.0.0.4 1457426442.887:5.5371
collectd/carlingford/ping/ping_stddev-10.0.0.4 1457426442.887:1.06614887953481
collectd/carlingford/ping/ping_droprate-10.0.0.4 1457426442.887:0
collectd/carlingford/ping/ping-10.0.0.3 1457426442.887:0.8873
collectd/carlingford/ping/ping_stddev-10.0.0.3 1457426442.887:0.715952209764497
collectd/carlingford/ping/ping_droprate-10.0.0.3 1457426442.887:0
collectd/carlingford/ping/ping-10.0.0.2 1457426442.887:5.4706
collectd/carlingford/ping/ping_stddev-10.0.0.2 1457426442.887:0.995523670124312
collectd/carlingford/ping/ping_droprate-10.0.0.2 1457426442.887:0
collectd/carlingford/ping/ping-10.0.0.1 1457426442.887:0.565
collectd/carlingford/ping/ping_stddev-10.0.0.1 1457426442.887:0.487876350455044
collectd/carlingford/ping/ping_droprate-10.0.0.1 1457426442.887:0
collectd/carlingford/ping/ping-parkvale.mine.nu 1457426442.886:nan
collectd/carlingford/ping/ping_stddev-parkvale.mine.nu 1457426442.887:nan
collectd/carlingford/ping/ping_droprate-parkvale.mine.nu 1457426442.887:1
collectd/carlingford/ping/ping-10.0.0.9 1457426442.887:5.7446
     */
}
