package com.rilintech.new_wireless_monitor;


import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;

public class ParamsParser {
    private HashMap<String, List> data_map = new HashMap<String, List>(); //����ͼ
    public static List<String> tech_alarm = new ArrayList<String>();
    public static List<String> bio_alarm = new ArrayList<String>();

    public ParamsParser() {

    }

    //ͨ������У��Ͳ��� caculate_checksum tested and passed
    private String caculate_checksum(String s) {
        byte[] origin_msg = BytesUtil.hexStringToBytes(s);
        //int sumCheck = 0;
        byte sumCheck = 0x00;
        for (int i = 0; i < origin_msg.length; i++)
            sumCheck += origin_msg[i];//
        //String res = BytesUtil.byteToHexString((byte)(~(sumCheck & 0xff)));
        String res = BytesUtil.byteToHexString((byte) ~sumCheck);//~ȡ�������  ����У��λ
        return res;
    }

    //��֤������Դ   Validate origin data
    private boolean validate_data(byte[] str) {
        String temp_res = BytesUtil.bytesToHexString(str);
        if (temp_res.length() < 6)
            return false;
        String str_remove_checksum = temp_res.subSequence(0, temp_res.length() - 2).toString();
        String origin_checksum = BytesUtil.byteToHexString(str[str.length - 1]);//ȡ��ĩβУ��λ
        if (caculate_checksum(str_remove_checksum).equals(origin_checksum))
            return true;
        else
            return false;
    }

    //Parse ����
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
                        if (!tech_alarm.contains("�ĵ��ź�̫��"))//�ж���û�ж�Ӧ��valueֵ
                            tech_alarm.add("�ĵ��ź�̫��");
                        h_curve_param = h_curve_param + "�ĵ��ź�̫��";
                    } else {
                        if (tech_alarm.contains("�ĵ��ź�̫��"))
                            tech_alarm.remove("�ĵ��ź�̫��");
                        h_curve_param = h_curve_param + "����";
                    }
                    if (((str[2] >> 1) & 0x01) == 1) {
                        if (!tech_alarm.contains("�ĵ絼������"))
                            tech_alarm.add("�ĵ絼������");
                        h_curve_param = h_curve_param + ",�ĵ絼������";
                    } else {
                        if (tech_alarm.contains("�ĵ絼������"))
                            tech_alarm.remove("�ĵ絼������");
                        h_curve_param = h_curve_param + ",����";
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

                    switch (((str[2] >> 4) & 0x02))//�ĵ��˲�ģʽ����
                    {
                        case 0:
                            h_curve_param = h_curve_param + ",����ģʽ";
                            break;
                        case 1:
                            h_curve_param = h_curve_param + ",�໤ģʽ";
                            break;
                        case 2:
                            h_curve_param = h_curve_param + ",���ģʽ";
                            break;

                    }


                    switch (((str[2] >> 6) & 0x02))//�ĵ絼���л�����
                    {
                        case 0:
                            h_curve_param = h_curve_param + ",3����ģʽ�¶�ͨ��I����";
                            break;
                        case 1:
                            h_curve_param = h_curve_param + ",3����ģʽ�¶�ͨ��II����";
                            break;
                        case 2:
                            h_curve_param = h_curve_param + ",3����ģʽ�¶�ͨ��III����";
                            break;
                        case 3:
                            h_curve_param = h_curve_param + ",5����ģʽ";
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
                            h_curve_params.add(",���ڷ���");
                            break;
                        case 1:
                            h_curve_params.add(",����");
                            break;
                        case 2:
                            h_curve_params.add(",ͣ��");
                            break;
                        case 3:
                            h_curve_params.add(",�Ҳ�/����");
                        case 4:
                            h_curve_params.add(",R ON T");
                        case 5:
                            h_curve_params.add(",�������");
                        case 6:
                            h_curve_params.add(",�ɶ�����");
                        case 7:
                            h_curve_params.add(",��������");
                        case 8:
                            h_curve_params.add(",������");
                        case 9:
                            h_curve_params.add(",������");
                        case 10:
                            h_curve_params.add(",�Ķ�����");
                        case 11:
                            h_curve_params.add(",�Ķ�����");
                        case 12:
                            h_curve_params.add(",©��");
                            break;
                    }


                    data_map.put("h_curve_params", h_curve_params);
                    break;
                case 3://str[1]=0x11
                    List<String> bp_data = new ArrayList<String>();


                    switch ((str[2] & 0x02)) {
                        case 0:
                            bp_data.add("����ģʽ");
                            break;
                        case 1:
                            bp_data.add("��ͯģʽ");
                            break;
                        case 2:
                            bp_data.add("������ģʽ");
                            break;

                    }

                    //String bp_data_param = "";
                    switch (((str[2] >> 2) & 0x0f)) {
                        case 0:
                            bp_data.add("�������");
                            break;
                        case 1:
                            bp_data.add("���ڲ���");
                            break;
                        case 2:
                            bp_data.add("������ֹ");
                            break;
                        case 3:
                            if (!tech_alarm.contains("��ѹ����"))
                                tech_alarm.add("��ѹ����");
                            bp_data.add("��ѹ����");
                            break;
                        case 4:
                            if (!tech_alarm.contains("���̫�ɻ�δ��"))
                                tech_alarm.add("���̫�ɻ�δ��");
                            bp_data.add("���̫�ɻ�δ��");
                            break;
                        case 5:
                            if (!tech_alarm.contains("����ʱ�����"))
                                tech_alarm.add("����ʱ�����");
                            bp_data.add("����ʱ�����");
                            break;
                        case 6:
                            if (!tech_alarm.contains("��������"))
                                tech_alarm.add("��������");
                            bp_data.add("��������");
                            break;
                        case 7:
                            if (!tech_alarm.contains("��ѹ����"))
                                tech_alarm.add("�������и���");
                            bp_data.add("�������и���");
                            break;
                        case 8:
                            if (!tech_alarm.contains("��ѹ����"))
                                tech_alarm.add("����������Χ");
                            bp_data.add("����������Χ");
                            break;
                        case 9:
                            bp_data.add("���ڳ�ʼ��");
                            break;
                        case 10:
                            bp_data.add("��ʼ�����");
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
                    tech_alarm.remove("Ѫ��̽ͷ����");
                    tech_alarm.remove("Ѫ��ָ�п�");
                    tech_alarm.remove("Ѫ��̽ͷ����");
                    tech_alarm.remove("��������ʱ�����");
                    switch ((str[2] & 0x02)) {
                        case 0:
                            spo2_data.add("����");
                            break;
                        case 1:
                            if (!tech_alarm.contains("Ѫ��̽ͷ����"))
                                tech_alarm.add("Ѫ��̽ͷ����");
                            spo2_data.add("Ѫ��̽ͷ����");
                            break;
                        case 2:
                            if (!tech_alarm.contains("Ѫ��ָ�п�"))
                                tech_alarm.add("Ѫ��ָ�п�");
                            spo2_data.add("Ѫ��ָ�п�");
                            break;
                        case 3:
                            spo2_data.add("������������");
                            break;
                        case 4:
                            if (!tech_alarm.contains("��������ʱ�����"))
                                tech_alarm.add("��������ʱ�����");
                            spo2_data.add("��������ʱ�����");
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
                    tech_alarm.remove("����1̽ͷ����");
                    tech_alarm.remove("����2̽ͷ����");
                    tech_alarm.remove("����1������2̽ͷ������");

                    switch ((str[2] & 0x02)) {
                        case 0:
                            temp_data.add("����");

                            break;
                        case 1:
                            if (!tech_alarm.contains("����1̽ͷ����"))
                                tech_alarm.add("����1̽ͷ����");
                            temp_data.add("����1̽ͷ����");

                            break;
                        case 2:

                            if (!tech_alarm.contains("����2̽ͷ����"))
                                //tech_alarm.add("����2̽ͷ����");
                                temp_data.add("����2̽ͷ����");

                            break;

                        case 3:

                            if (!tech_alarm.contains("����1������2̽ͷ������"))
                                tech_alarm.add("����1̽ͷ����");
                            temp_data.add("����1������2̽ͷ������");

                            break;


                    }

                    if (((str[3] & 0xff) == 0) && ((str[4] & 0xff) == 0)) {
                        if (!tech_alarm.contains("����1̽ͷ����"))
                            tech_alarm.add("����1̽ͷ����");
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

    public String h_filter_control(int s)//�ĵ��˲�
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
