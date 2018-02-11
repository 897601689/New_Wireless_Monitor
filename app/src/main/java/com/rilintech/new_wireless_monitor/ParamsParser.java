package com.rilintech.new_wireless_monitor;


import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;

public class ParamsParser {
    private HashMap<String, List> data_map = new HashMap<String, List>(); //数据图
    public static List<String> tech_alarm = new ArrayList<String>();
    public static List<String> bio_alarm = new ArrayList<String>();

    public ParamsParser() {

    }

    //通过计算校验和测试 caculate_checksum tested and passed
    private String caculate_checksum(String s) {
        byte[] origin_msg = BytesUtil.hexStringToBytes(s);
        //int sumCheck = 0;
        byte sumCheck = 0x00;
        for (int i = 0; i < origin_msg.length; i++)
            sumCheck += origin_msg[i];//
        //String res = BytesUtil.byteToHexString((byte)(~(sumCheck & 0xff)));
        String res = BytesUtil.byteToHexString((byte) ~sumCheck);//~取反运算符  计算校验位
        return res;
    }

    //验证的数据源   Validate origin data
    private boolean validate_data(byte[] str) {
        String temp_res = BytesUtil.bytesToHexString(str);
        if (temp_res.length() < 6)
            return false;
        String str_remove_checksum = temp_res.subSequence(0, temp_res.length() - 2).toString();
        String origin_checksum = BytesUtil.byteToHexString(str[str.length - 1]);//取出末尾校验位
        if (caculate_checksum(str_remove_checksum).equals(origin_checksum))
            return true;
        else
            return false;
    }

    //Parse 解析
    public HashMap<String, List> parser(byte[] str) {
        //HashMap<String , List>  = new  HashMap<String , List>();
        data_map.clear();
        if (validate_data(str)) {
            switch (str[1] & 0xFF) {
                case 1://str[1]=0x01
                    List<Integer> h_curve_data = new ArrayList<Integer>();
                    for (int i = 2; i < str.length - 1; i++)
                        h_curve_data.add(str[i] & 0xFF);
                    data_map.put("h_curve", h_curve_data);
                    break;
                case 2://str[1]=0x10
                    List<String> h_curve_params = new ArrayList<String>();
                    //for(int i = 4;i < str.length-1;i++)
                    //	;//h_curve_params.add((int)str[i]);
                    String h_curve_param = "";
                    if ((str[2] & 0x01) == 1) {
                        if (!tech_alarm.contains("心电信号太弱"))//判断有没有对应的value值
                            tech_alarm.add("心电信号太弱");
                        h_curve_param = h_curve_param + "心电信号太弱";
                    } else {
                        if (tech_alarm.contains("心电信号太弱"))
                            tech_alarm.remove("心电信号太弱");
                        h_curve_param = h_curve_param + "正常";
                    }
                    if (((str[2] >> 1) & 0x01) == 1) {
                        if (!tech_alarm.contains("心电导联脱落"))
                            tech_alarm.add("心电导联脱落");
                        h_curve_param = h_curve_param + ",心电导联脱落";
                    } else {
                        if (tech_alarm.contains("心电导联脱落"))
                            tech_alarm.remove("心电导联脱落");
                        h_curve_param = h_curve_param + ",正常";
                    }

                    switch (((str[2] >> 2) & 0x02))//
                    {
                        case 0:
                            h_curve_param = h_curve_param + ",x0.25";
                            break;
                        case 1:
                            h_curve_param = h_curve_param + ",x0.5";
                            break;
                        case 2:
                            h_curve_param = h_curve_param + ",x1";
                            break;
                        case 3:
                            h_curve_param = h_curve_param + ",x2";
                            break;
                    }

                    switch (((str[2] >> 4) & 0x02))//心电滤波模式设置
                    {
                        case 0:
                            h_curve_param = h_curve_param + ",手术模式";
                            break;
                        case 1:
                            h_curve_param = h_curve_param + ",监护模式";
                            break;
                        case 2:
                            h_curve_param = h_curve_param + ",诊断模式";
                            break;

                    }


                    switch (((str[2] >> 6) & 0x02))//心电导联切换设置
                    {
                        case 0:
                            h_curve_param = h_curve_param + ",3导联模式下对通道I采样";
                            break;
                        case 1:
                            h_curve_param = h_curve_param + ",3导联模式下对通道II采样";
                            break;
                        case 2:
                            h_curve_param = h_curve_param + ",3导联模式下对通道III采样";
                            break;
                        case 3:
                            h_curve_param = h_curve_param + ",5导联模式";
                            break;
                    }

                    h_curve_params.add(h_curve_param);

                    if (((str[2] >> 1) & 0x01) == 0) {
                        h_curve_params.add(
                                String.valueOf(str[3] & 0xFF));
                        h_curve_params.add(
                                String.valueOf(str[4] & 0xFF));
                    } else {
                        h_curve_params.add(
                                String.valueOf(255));
                        h_curve_params.add(
                                String.valueOf(255));
                    }
                    if (((str[5] >> 7) & 0x01) == 1)
                        h_curve_params.add(
                                String.valueOf(-(str[5] & 0x7f) / 100));
                    else
                        h_curve_params.add(
                                String.valueOf((str[5] & 0x7f) / 100));


                    switch ((str[6] & 0x02)) {
                        case 0:
                            h_curve_params.add(",正在分析");
                            break;
                        case 1:
                            h_curve_params.add(",正常");
                            break;
                        case 2:
                            h_curve_params.add(",停搏");
                            break;
                        case 3:
                            h_curve_params.add(",室颤/室速");
                        case 4:
                            h_curve_params.add(",R ON T");
                        case 5:
                            h_curve_params.add(",多个室早");
                        case 6:
                            h_curve_params.add(",成对室早");
                        case 7:
                            h_curve_params.add(",单个室早");
                        case 8:
                            h_curve_params.add(",二联律");
                        case 9:
                            h_curve_params.add(",三联律");
                        case 10:
                            h_curve_params.add(",心动过速");
                        case 11:
                            h_curve_params.add(",心动过缓");
                        case 12:
                            h_curve_params.add(",漏搏");
                            break;
                    }


                    data_map.put("h_curve_params", h_curve_params);
                    break;
                case 3://str[1]=0x11
                    List<String> bp_data = new ArrayList<String>();


                    switch ((str[2] & 0x02)) {
                        case 0:
                            bp_data.add("成人模式");
                            break;
                        case 1:
                            bp_data.add("儿童模式");
                            break;
                        case 2:
                            bp_data.add("新生儿模式");
                            break;

                    }

                    //String bp_data_param = "";
                    switch (((str[2] >> 2) & 0x0f)) {
                        case 0:
                            bp_data.add("测量完成");
                            break;
                        case 1:
                            bp_data.add("正在测量");
                            break;
                        case 2:
                            bp_data.add("测量终止");
                            break;
                        case 3:
                            if (!tech_alarm.contains("过压保护"))
                                tech_alarm.add("过压保护");
                            bp_data.add("过压保护");
                            break;
                        case 4:
                            if (!tech_alarm.contains("袖带太松或未接"))
                                tech_alarm.add("袖带太松或未接");
                            bp_data.add("袖带太松或未接");
                            break;
                        case 5:
                            if (!tech_alarm.contains("测量时间过长"))
                                tech_alarm.add("测量时间过长");
                            bp_data.add("测量时间过长");
                            break;
                        case 6:
                            if (!tech_alarm.contains("测量出错"))
                                tech_alarm.add("测量出错");
                            bp_data.add("测量出错");
                            break;
                        case 7:
                            if (!tech_alarm.contains("过压保护"))
                                tech_alarm.add("测量中有干扰");
                            bp_data.add("测量中有干扰");
                            break;
                        case 8:
                            if (!tech_alarm.contains("过压保护"))
                                tech_alarm.add("测量超出范围");
                            bp_data.add("测量超出范围");
                            break;
                        case 9:
                            bp_data.add("正在初始化");
                            break;
                        case 10:
                            bp_data.add("初始化完成");
                            break;

                    }
                    if (((str[2] >> 2) & 0x0f) == 0) {
                        bp_data.add(
                                String.valueOf((str[3] & 0xFF) * 2));

                        bp_data.add(
                                String.valueOf(str[4] & 0xFF));

                        bp_data.add(
                                String.valueOf(str[5] & 0xFF));

                        bp_data.add(
                                String.valueOf(str[6] & 0xFF));
                    } else {
                        bp_data.add(
                                String.valueOf(str[3] * 2));

                        bp_data.add(
                                String.valueOf(-1));

                        bp_data.add(
                                String.valueOf(-1));

                        bp_data.add(
                                String.valueOf(-1));
                    }

                    data_map.put("bp_data", bp_data);
                    break;
                case 4:
                    List<String> spo2_data = new ArrayList<String>();
                    tech_alarm.remove("血氧探头脱落");
                    tech_alarm.remove("血氧指夹空");
                    tech_alarm.remove("血氧探头脱落");
                    tech_alarm.remove("脉搏搜索时间过长");
                    switch ((str[2] & 0x02)) {
                        case 0:
                            spo2_data.add("正常");
                            break;
                        case 1:
                            if (!tech_alarm.contains("血氧探头脱落"))
                                tech_alarm.add("血氧探头脱落");
                            spo2_data.add("血氧探头脱落");
                            break;
                        case 2:
                            if (!tech_alarm.contains("血氧指夹空"))
                                tech_alarm.add("血氧指夹空");
                            spo2_data.add("血氧指夹空");
                            break;
                        case 3:
                            spo2_data.add("正在搜索脉搏");
                            break;
                        case 4:
                            if (!tech_alarm.contains("脉搏搜索时间过长"))
                                tech_alarm.add("脉搏搜索时间过长");
                            spo2_data.add("脉搏搜索时间过长");
                            break;

                    }

                    spo2_data.add(
                            String.valueOf(str[3] & 0xFF));

                    spo2_data.add(
                            String.valueOf(str[4] & 0xFF));


                    data_map.put("spo2_data", spo2_data);
                    break;
                case 5:
                    List<String> temp_data = new ArrayList<String>();
                    tech_alarm.remove("体温1探头脱落");
                    tech_alarm.remove("体温2探头脱落");
                    tech_alarm.remove("体温1、体温2探头均脱落");

                    switch ((str[2] & 0x02)) {
                        case 0:
                            temp_data.add("正常");

                            break;
                        case 1:
                            if (!tech_alarm.contains("体温1探头脱落"))
                                tech_alarm.add("体温1探头脱落");
                            temp_data.add("体温1探头脱落");

                            break;
                        case 2:

                            if (!tech_alarm.contains("体温2探头脱落"))
                                //tech_alarm.add("体温2探头脱落");
                                temp_data.add("体温2探头脱落");

                            break;

                        case 3:

                            if (!tech_alarm.contains("体温1、体温2探头均脱落"))
                                tech_alarm.add("体温1探头脱落");
                            temp_data.add("体温1、体温2探头均脱落");

                            break;


                    }

                    if (((str[3] & 0xff) == 0) && ((str[4] & 0xff) == 0)) {
                        if (!tech_alarm.contains("体温1探头脱落"))
                            tech_alarm.add("体温1探头脱落");
                        temp_data.add(
                                String.valueOf(-1));
                    } else
                        temp_data.add(
                                String.valueOf(str[3]) + "." + String.valueOf(str[4]));


                    data_map.put("temp_data", temp_data);
                    break;

                case 254:
                    List<Integer> spo2_curve = new ArrayList<Integer>();
                    for (int i = 2; i < str.length - 1; i++)
                        spo2_curve.add(str[i] & 0xFF);
                    data_map.put("spo2_curve", spo2_curve);
                    break;
                case 255:
                    List<Integer> resp_curve = new ArrayList<Integer>();
                    for (int i = 2; i < str.length - 1; i++)
                        resp_curve.add(str[i] & 0xFF);
                    data_map.put("resp_curve", resp_curve);
                    break;

            }
        }
        return data_map;
    }

    public String hr_control(boolean s) {
        if (s)
            return "55AA040101F9";
        else
            return "55AA040100FA";
    }

    public String bp_control(boolean s) {
        if (s)
            return "55AA040201F8";
        else
            return "55AA040200F9";
    }

    public String spo2_control(boolean s) {
        if (s)
            return "55AA040301F7";
        else
            return "55AA040300F8";
    }

    public String temp_control(boolean s) {
        if (s)
            return "55AA040401F6";
        else
            return "55AA040400F7";
    }

    public String h_3_5_switch_control(int s) {
        switch (s) {
            case 1:
                return "55AA040501F5";

            case 2:
                return "55AA040502" + caculate_checksum("040502");

            case 3:
                return "55AA040503" + caculate_checksum("040503");
            case 4:
                return "55AA040504" + caculate_checksum("040504");
        }
        return "";
    }

    public String h_mag_control(int s) {
        switch (s) {
            case 1:
                return "55AA040701" + caculate_checksum("040701");

            case 2:
                return "55AA040702" + caculate_checksum("040702");

            case 3:
                return "55AA040703" + caculate_checksum("040703");
            case 4:
                return "55AA040704" + caculate_checksum("040704");
        }
        return "";
    }

    public String h_filter_control(int s)//心电滤波
    {
        switch (s) {
            case 1:
                return "55AA040801" + caculate_checksum("040801");

            case 2:
                return "55AA040802" + caculate_checksum("040802");

            case 3:
                return "55AA040803" + caculate_checksum("040803");

        }
        return "";
    }

    public String bp_measure_control(int s) {
        switch (s) {
            case 1:
                return "55AA040901" + caculate_checksum("040901");

            case 2:
                return "55AA040902" + caculate_checksum("040902");

            case 3:
                return "55AA040903" + caculate_checksum("040903");

        }
        return "";
    }

    public String bp_prefill_control(int s) {
        switch (s) {
            case 1:
                return "55AA040A4B" + caculate_checksum("040A4B");

            case 2:
                return "55AA040A32" + caculate_checksum("040A32");

            case 3:
                return "55AA040A23" + caculate_checksum("040A23");

        }
        return "";
    }

    public String resp_mag_control(int s) {
        switch (s) {
            case 1:
                return "55AA040F01" + caculate_checksum("040F01");

            case 2:
                return "55AA040F02" + caculate_checksum("040F02");

            case 3:
                return "55AA040F03" + caculate_checksum("040F03");
            case 4:
                return "55AA040F04" + caculate_checksum("040F04");
        }
        return "";
    }

    public String h_out_control(boolean s) {
        if (s)
            return "55AA04FB00" + caculate_checksum("04FB00");
        else
            return "55AA04FB01" + caculate_checksum("04FB01");
    }

    public String spo2_out_control(boolean s) {
        if (s)
            return "55AA04FE01" + caculate_checksum("04FE01");
        else
            return "55AA04FE00" + caculate_checksum("04FE00");

    }

    public String resp_out_control(boolean s) {
        if (s)
            return "55AA04FF01FB";
        else
            return "55AA04FF00FC";

    }

    public String bp_leak_detect(String s) {
        if (s.equals("00"))
            return "55AA041000EB";
        else
            return "55AA0410" + s + caculate_checksum("0410" + s);
    }

    public String bp_static_calib(boolean s) {
        if (s)
            return "55AA040B01EF";
        else
            return "55AA040B00F0";
    }

    public String bp_static_adj_set(String s) {

        return "55AA040C" + s + caculate_checksum("040C" + s);
    }

    public String temp_calib(String s) {

        return "55AA040D" + s + caculate_checksum("040D" + s);
    }
}
