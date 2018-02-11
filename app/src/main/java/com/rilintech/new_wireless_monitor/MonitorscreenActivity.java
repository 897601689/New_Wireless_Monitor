package com.rilintech.new_wireless_monitor;


import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.rilintech.new_wireless_monitor.util.SystemUiHider;


import android.animation.AnimatorSet.Builder;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.DateFormat;

import android.util.Log;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.graphics.*;
import android.graphics.Paint.Style;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Message;
import android.graphics.drawable.BitmapDrawable;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class MonitorscreenActivity extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     * ϵͳ�Ƿ��Զ����ز˵�
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     * �Զ������ӳٵ�ʱ��
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 10000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     * ����л�������ʾ����
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     * ����ģʽ ����ȫ��
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */

    //�������˿�
    private static final int SERVER_PORT = 8899;

    private SystemUiHider mSystemUiHider;


    private SurfaceView mSurfaceView = null;
    private SurfaceHolder mSurfaceHolder = null;


    private SurfaceView mSurfaceView_params = null;
    private SurfaceHolder mSurfaceHolder_params = null;


    private AlertDialog selfdialog;
    private View view;

    private View bp_stat_view;
    private LayoutInflater bp_stat_inflater;
    private TextView bp_stat_value_text;
    //Params ����
    public static String ip_addr = "10.10.100.254";    //������IP
    public static String hr_alert_switch;            //���ʱ�������
    public static int hr_alert_high, hr_alert_low;    //���ʱ������� /���ʱ�������
    public static String bp_alert_switch;            //Ѫѹ��������
    public static int bp_alert_high, bp_alert_low;    //Ѫѹ�������� /Ѫѹ��������
    public static String spo2_alert_switch;            //Ѫ����������
    public static int spo2_alert_high, spo2_alert_low;//Ѫ���������� /Ѫ����������
    public static String resp_alert_switch;            //�����ʱ�������
    public static int resp_alert_high, resp_alert_low;//�����ʱ������� /�����ʱ�������
    public static float temp_alert_high, temp_alert_low;//���±������� /���±�������
    public static String temp_alert_switch;            //���±�������
    public static float sys_volume;
    public static String ecg_filter_mode;    //�ĵ��˲�ģʽ
    public static String ecg_mag;            //�ĵ�ͼ
    public static String resp_curve_mag;    //����������ͼ
    public static String ecg_channel;        //�ĵ�ͨ��
    public static String bp_auto_switch;    //Ѫѹ�Զ���������
    public static String bp_auto_inter;        //Ѫѹ�Զ�����ʱ��
    public static String bp_mode;            //Ѫѹ����ģʽ
    //recieved data�յ�������
    public static boolean read_data_cache_lock = false;
    public static boolean write_data_cache_lock = false;
    public static int data_hr = -1;        //����
    public static int data_bp_high = -1;//��Ѫѹ
    public static int data_bp_low = -1;    //��Ѫѹ
    public static int data_bp_mean = -1;//ƽ��Ѫѹ
    public static int data_bp_belt = -1;//
    public static int data_spo2 = -1;    //Ѫ��
    public static int data_pulse = -1;    //����
    public static int data_resp = -1;    //������
    public static float data_temp = -1;    //����
    public static List<String> data_tech_alarm = new ArrayList<String>();    //������������
    public static List<String> data_bio_alarm = new ArrayList<String>();    //���ﱨ������
    public static List<Integer> hr_I = new ArrayList<Integer>();    //����I
    public static int hr_I_last_point_x = 0;    //����I X
    public static int hr_I_last_point_y = 0;    //����I Y
    public static List<Integer> hr_II = new ArrayList<Integer>();    //����II X
    public static int hr_II_last_point_x = 0;    //����II X
    public static int hr_II_last_point_y = 0;    //����II Y
    public static List<Integer> hr_III = new ArrayList<Integer>();    //����III X
    public static int hr_III_last_point_x = 0;    //����III X
    public static int hr_III_last_point_y = 0;    //����III Y
    public static List<Integer> hr_avr = new ArrayList<Integer>();//����aVR
    public static int hr_avr_last_point_x = 0;    //����aVR X
    public static int hr_avr_last_point_y = 0;    //����aVR Y
    public static List<Integer> hr_avl = new ArrayList<Integer>();//����aVL
    public static int hr_avl_last_point_x = 0;    //����aVL X
    public static int hr_avl_last_point_y = 0;    //����aVL Y
    public static List<Integer> hr_avf = new ArrayList<Integer>();//����aVF
    public static int hr_avf_last_point_x = 0;    //����aVF X
    public static int hr_avf_last_point_y = 0;    //����aVF Y
    public static List<Integer> hr_v = new ArrayList<Integer>();    //����V
    public static int hr_v_last_point_x = 0;    //����V X
    public static int hr_v_last_point_y = 0;    //����V Y
    //flag
    //Display Data
    List<Integer> hr_I_disp = new ArrayList<Integer>();//����I������ʾ
    List<Integer> hr_II_disp = new ArrayList<Integer>();//����II������ʾ
    List<Integer> hr_III_disp = new ArrayList<Integer>();//����III������ʾ

    List<Integer> spo2_curve_disp = new ArrayList<Integer>();//Ѫ�����Ͷ�������ʾ
    List<Integer> resp_curve_disp = new ArrayList<Integer>();//������������ʾ
    //private boolean data_tech_alarm_changed = false;
    //private boolean data_bio_alarm_changed = false;
    //private String hr_stat = "";
    //private String bp_stat = "";
    public static List<Integer> spo2_curve = new ArrayList<Integer>();//Ѫ�����Ͷ�����
    public static int spo2_curve_last_point_x = 0;
    public static int spo2_curve_last_point_y = 0;
    public static List<Integer> resp_curve = new ArrayList<Integer>();//����������
    public static int resp_curve_last_point_x = 0;
    public static int resp_curve_last_point_y = 0;

    private String curr_tech_alarm = "";    //��������
    private String curr_bio_alarm = "";    //���ﱨ��

    private static String cache_data = "";//��������

    public static List<String> cmd_list = new ArrayList<String>();
    //Protocol ParserЭ�������
    public static ParamsParser pp = new ParamsParser();
    //counter������
    private int alarm_counter;
    private int alarm_tech_bio_counter = 0;
    private long last_bp_time = 0;
    //Socket�׽���
    //private TcpClient tcpclient = null;
    private Socket socket = null;
    private ClientThread ct = null;


    private int count = 0;
    private int spo2_count = 0;
    private int resp_count = 0;
    private boolean run = true;
    private boolean network_avl = false;
    //private ParamsParser pp;

    //Alarm Sound
    SoundPool soundPool;
    int loadId;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 1) {
                //int protocol_seg = 0;
            }
        }
    };


    private DatabaseHelper databaseHelper = null;
    private Dao<ParamsSettingData, Integer> ps_data = null;

    HashMap<String, List> temp_map = null;
    private Runnable task = new Runnable() {

        public void run() {
            // TODO Auto-generated method stub
            //Socket Comm
            String[] temp_str = null;
            String string_data = "";
            string_data = data_process(false, "");
            temp_str = string_data.split("55aa");

            for (int i = 0; i < temp_str.length - 1; i++) {

                //Get Information��ȡ��Ϣ
                if (temp_str[i].length() > 0) {
                    //��ý�������
                    temp_map = pp.parser(BytesUtil.hexStringToBytes(temp_str[i]));
                    if (temp_map.containsKey("h_curve"))// ��������
                    {
                        //Log.e("h_curve", "" + temp_map.get("h_curve").size());
                        //if (temp_map.get("h_curve").size() >= 7) {
                        // Integer.parseInt(String)�ַ���ת����
                        hr_I.add(Integer.parseInt(temp_map.get("h_curve").get(0).toString()));
                        hr_II.add(Integer.parseInt(temp_map.get("h_curve").get(1).toString()));
                        hr_III.add(Integer.parseInt(temp_map.get("h_curve").get(2).toString()));
                        hr_avr.add(Integer.parseInt(temp_map.get("h_curve").get(3).toString()));
                        hr_avl.add(Integer.parseInt(temp_map.get("h_curve").get(4).toString()));
                         hr_avf.add(Integer.parseInt(temp_map.get("h_curve").get(5).toString()));
                        //hr_v.add(Integer.parseInt(temp_map.get("h_curve").get(6).toString()));
                        //}
                    }
                    if (temp_map.containsKey("h_curve_params"))// �������߲���
                    {
                        //if(temp_map.get("h_curve_params").size()>=5) {
                        data_hr = Integer.parseInt(temp_map.get("h_curve_params").get(1).toString());
                        data_resp = Integer.parseInt(temp_map.get("h_curve_params").get(2).toString());
                        // data_st;
                        //}
                    }
                    if (temp_map.containsKey("bp_data"))// Ѫѹ����
                    {
                        //if(temp_map.get("bp_data").size()>=6) {
                        data_bp_belt = Integer.parseInt(temp_map.get("bp_data").get(2).toString()); // �޴�Ѫѹ
                        data_bp_high = Integer.parseInt(temp_map.get("bp_data").get(3).toString()); // ��ѹ
                        data_bp_mean = Integer.parseInt(temp_map.get("bp_data").get(4).toString()); // Ԥ��Ѫѹ
                        data_bp_low = Integer.parseInt(temp_map.get("bp_data").get(5).toString()); // ��ѹ
                        bp_stat_value_text.setText(data_bp_belt + "");
                        //}
                    }
                    if (temp_map.containsKey("spo2_data"))// Ѫ������
                    {
                        data_spo2 = Integer.parseInt(temp_map.get("spo2_data").get(1).toString());// Ѫ��
                        if (data_spo2 == 127)
                            data_spo2 = -1;
                        data_pulse = Integer.parseInt(temp_map.get("spo2_data").get(2).toString());// ����
                        if (data_pulse == 255)
                            data_pulse = -1;
                    }
                    if (temp_map.containsKey("temp_data"))// ����
                    {
                        data_temp = Float.parseFloat((temp_map.get("temp_data").get(1).toString()));// ����
                    }
                    if (temp_map.containsKey("spo2_curve"))// Ѫ�����Ͷ�����
                    {
                        spo2_curve.add(Integer.parseInt(temp_map.get("spo2_curve").get(0).toString()));
                    }
                    if (temp_map.containsKey("resp_curve"))// ����������
                    {
                        resp_curve.add(Integer.parseInt(temp_map.get("resp_curve").get(0).toString()));
                    }
                    temp_map.clear();
                }

            }


            //Update UI�����û�����
            alarm_tech_bio_counter++;
            //Alarm counter ����������
            if ((hr_alert_switch.equals("on") && (data_hr < hr_alert_low || data_hr > hr_alert_high) && (data_hr > 0)) || (bp_alert_switch.equals("on") && (data_bp_low < bp_alert_low || data_bp_high > bp_alert_high) && (data_bp_low > 0)) || (spo2_alert_switch.equals("on") && (data_spo2 < spo2_alert_low || data_spo2 > spo2_alert_high) && (data_spo2 > 0)) || (resp_alert_switch.equals("on") && (data_resp < resp_alert_low || data_resp > resp_alert_high) && (data_resp > 0)) || (temp_alert_switch.equals("on") && (data_temp < temp_alert_low || data_temp > temp_alert_high) && (data_temp > 0))) {
                alarm_counter++;

                if (data_hr < hr_alert_low)
                    if (!data_bio_alarm.contains("���ʵ�"))
                        data_bio_alarm.add("���ʵ�");

                if (data_hr > hr_alert_high)
                    if (!data_bio_alarm.contains("���ʸ�"))
                        data_bio_alarm.add("���ʸ�");

                if (data_bp_low < bp_alert_low)
                    if (!data_bio_alarm.contains("Ѫѹ��"))
                        data_bio_alarm.add("Ѫѹ��");

                if (data_bp_high > bp_alert_high)
                    if (!data_bio_alarm.contains("Ѫѹ��"))
                        data_bio_alarm.add("Ѫѹ��");

                if (data_spo2 < spo2_alert_low)
                    if (!data_bio_alarm.contains("Ѫ�����Ͷȵ�"))
                        data_bio_alarm.add("Ѫ�����Ͷȵ�");

                if (data_spo2 > spo2_alert_low)
                    if (!data_bio_alarm.contains("Ѫ�����Ͷȸ�"))
                        data_bio_alarm.add("Ѫ�����Ͷȸ�");


                if (data_resp < resp_alert_low)
                    if (!data_bio_alarm.contains("�����ʵ�"))
                        data_bio_alarm.add("�����ʵ�");

                if (data_resp > resp_alert_high)
                    if (!data_bio_alarm.contains("�����ʸ�"))
                        data_bio_alarm.add("�����ʸ�");

                if (data_temp < temp_alert_low)
                    if (!data_bio_alarm.contains("���µ�"))
                        data_bio_alarm.add("���µ�");

                if (data_temp > temp_alert_high)
                    if (!data_bio_alarm.contains("���¸�"))
                        data_bio_alarm.add("���¸�");
            } else
                alarm_counter = 0;
            if (alarm_counter > 50) {
                alarm_counter = 0;
                //Alert Sound������
                soundPool.play(loadId, 1, 1, 1, 0, 1f);
            }
            //Auto Bp measure counter �Զ�Ѫѹ������
            if (bp_auto_switch.equals("on")) {
                if (last_bp_time == 0)
                    last_bp_time = System.currentTimeMillis();//���ص�ǰ�ļ����ʱ��
                else
                    switch (Integer.parseInt(bp_auto_inter)) {
                        case 0:
                            if (System.currentTimeMillis() - last_bp_time > 300000)//���뼶 5����
                            {
                                ct.sendCmd(pp.bp_control(true));
                                last_bp_time = System.currentTimeMillis();
                            }
                            break;
                        case 1:
                            if (System.currentTimeMillis() - last_bp_time > 600000) {
                                ct.sendCmd(pp.bp_control(true));
                                last_bp_time = System.currentTimeMillis();
                            }
                            break;
                        case 2:
                            if (System.currentTimeMillis() - last_bp_time > 1800000) {
                                ct.sendCmd(pp.bp_control(true));
                                last_bp_time = System.currentTimeMillis();
                            }
                            break;
                        case 3:
                            if (System.currentTimeMillis() - last_bp_time > 3600000) {
                                ct.sendCmd(pp.bp_control(true));
                                last_bp_time = System.currentTimeMillis();
                            }
                            break;
                    }
            }
            //Canvas Draw Action ���嶯��
            Canvas canvas = mSurfaceHolder.lockCanvas(new Rect(count, 0, count + mSurfaceView.getWidth() / 140 + 15, mSurfaceView.getHeight() * 3 / 5));// �ؼ�:��ȡ����
            Paint mPaint = new Paint();
            mPaint.setColor(Color.GREEN);// ����Ϊ��ɫ
            mPaint.setStrokeWidth(1);// ���û��ʴ�ϸ
            mPaint.setTextSize(28);
            canvas.drawColor(Color.BLACK);//�������Ϊ��ɫ

            canvas.drawText("V1", 40, mSurfaceView.getHeight() / 5 / 2 - 40, mPaint);
            if (!ecg_mag.equals(""))//�ĵ粨������
                if (Integer.parseInt(ecg_mag) >= 0)
                    switch (Integer.parseInt(ecg_mag)) {
                        case 0:
                            canvas.drawText("X0.25", 120, mSurfaceView.getHeight() / 5 / 2 - 40, mPaint);
                            break;
                        case 1:
                            canvas.drawText("X0.5", 120, mSurfaceView.getHeight() / 5 / 2 - 40, mPaint);
                            break;
                        case 2:
                            canvas.drawText("X1", 120, mSurfaceView.getHeight() / 5 / 2 - 40, mPaint);
                            break;
                        case 3:
                            canvas.drawText("X2", 120, mSurfaceView.getHeight() / 5 / 2 - 40, mPaint);
                            break;
                    }
            if (!ecg_filter_mode.equals(""))//�ĵ��˲�ģʽ
                if (Integer.parseInt(ecg_filter_mode) >= 0)
                    switch (Integer.parseInt(ecg_filter_mode)) {
                        case 0:
                            canvas.drawText("����", 200, mSurfaceView.getHeight() / 5 / 2 - 40, mPaint);
                            break;
                        case 1:
                            canvas.drawText("�໤", 200, mSurfaceView.getHeight() / 5 / 2 - 40, mPaint);
                            break;
                        case 2:
                            canvas.drawText("���", 200, mSurfaceView.getHeight() / 5 / 2 - 40, mPaint);
                            break;

                    }
            canvas.drawText("V2", 40, mSurfaceView.getHeight() / 5 + mSurfaceView.getHeight() / 5 / 2 - 40, mPaint);
            canvas.drawText("V3", 40, mSurfaceView.getHeight() * 2 / 5 + mSurfaceView.getHeight() / 5 / 2 - 40, mPaint);

            int i;
            double k, m;
            int x_base = mSurfaceView.getHeight() / 5;
            read_data_cache_lock = true;
            int rect_height = mSurfaceView.getHeight() / 5;
            int rect_width = mSurfaceView.getWidth() / 140;
            double hr_dis_ratio = hr_I.size() * 1.0 / rect_width;
            int array_count = 0;

            int intern = mSurfaceView.getWidth() / 140;
            for (i = 0; i < hr_I.size() / 2; i++) {
                hr_I_disp.add(hr_I.get(i * 2));        //�ĵ�ͼI
                hr_II_disp.add(hr_II.get(i * 2));      //�ĵ�ͼII
                hr_III_disp.add(hr_III.get(i * 2));    //�ĵ�ͼIII
            }

            for (i = 0; i < spo2_curve.size(); i++) {
                spo2_curve_disp.add(spo2_curve.get(i));
            }

            for (i = 0; i < resp_curve.size(); i++) {
                resp_curve_disp.add(resp_curve.get(i));
            }

            hr_I.clear();
            hr_II.clear();
            hr_III.clear();
            hr_avr.clear();
            hr_avl.clear();
            hr_v.clear();
            spo2_curve.clear();
            resp_curve.clear();

            int rn = mSurfaceView.getWidth() / 100;
            for (i = count; i < count + mSurfaceView.getWidth() / 100; i++) {

                mPaint.setColor(Color.GREEN);// ����Ϊ��ɫ
                if (hr_I_disp.size() > count + mSurfaceView.getWidth() / 100) {
                    canvas.drawLine(i, (int) (-(hr_I_disp.get(i) - 128) / 256.0 * rect_height + rect_height / 2), i + 1, (int) (-(hr_I_disp.get(i + 1) - 128) / 256.0 * rect_height) + rect_height / 2, mPaint);
                    canvas.drawLine(i, (int) (-(hr_II_disp.get(i) - 128) / 256.0 * rect_height + rect_height * 3 / 2), i + 1, (int) (-(hr_II_disp.get(i + 1) - 128) / 256.0 * rect_height) + rect_height * 3 / 2, mPaint);
                    canvas.drawLine(i, (int) (-(hr_III_disp.get(i) - 128) / 256.0 * rect_height + rect_height * 5 / 2), i + 1, (int) (-(hr_III_disp.get(i + 1) - 128) / 256.0 * rect_height) + rect_height * 5 / 2, mPaint);
                } else {
                    canvas.drawLine(i, mSurfaceView.getHeight() / 5 / 2, i + 1, mSurfaceView.getHeight() / 5 / 2, mPaint);
                    canvas.drawLine(i, mSurfaceView.getHeight() * 3 / 5 / 2, i + 1, mSurfaceView.getHeight() * 3 / 5 / 2, mPaint);
                    canvas.drawLine(i, mSurfaceView.getHeight() * 5 / 5 / 2, i + 1, mSurfaceView.getHeight() * 5 / 5 / 2, mPaint);
                }
                array_count++;
            }
            mSurfaceHolder.unlockCanvasAndPost(canvas);
            if (spo2_curve_disp.size() > spo2_count + 3) {
                canvas = mSurfaceHolder.lockCanvas(new Rect(spo2_count, mSurfaceView.getHeight() * 3 / 5, spo2_count + 3 + 15, mSurfaceView.getHeight() * 4 / 5));// �ؼ�:��ȡ����
                canvas.drawColor(Color.BLACK);
                mPaint.setColor(Color.rgb(0, 255, 255));// ����Ϊ��ɫ
                mPaint.setStrokeWidth(1);// ���û��ʴ�ϸ
                for (i = spo2_count; i < spo2_count + 2; i++) {
                    canvas.drawLine(i, (int) ((spo2_curve_disp.get(i)) / 2 + rect_height * 7 / 2), i + 1, (int) ((spo2_curve_disp.get(i + 1)) / 2 + rect_height * 7 / 2), mPaint);
                }
                canvas.drawText("SpO2", 40, mSurfaceView.getHeight() * 3 / 5 + mSurfaceView.getHeight() / 5 / 2 - 40, mPaint);
                spo2_count = spo2_count + 2;
                if (spo2_count > mSurfaceView.getWidth()) {
                    spo2_count = 0;
                    spo2_curve_disp.clear();
                }
                mSurfaceHolder.unlockCanvasAndPost(canvas);

            }

            if (resp_curve_disp.size() > resp_count + 5) {
                canvas = mSurfaceHolder
                        .lockCanvas(new Rect(resp_count, mSurfaceView.getHeight() * 4 / 5, resp_count + 5 + 15, mSurfaceView.getHeight()));// �ؼ�:��ȡ����
                canvas.drawColor(Color.BLACK);
                mPaint.setColor(Color.YELLOW);// ����Ϊ��ɫ
                mPaint.setStrokeWidth(1);// ���û��ʴ�ϸ
                for (i = resp_count; i < resp_count + 4; i++) {
                    canvas.drawLine(i, (int) ((resp_curve_disp.get(i) - 128) + rect_height * 9 / 2), i + 1, (int) ((resp_curve_disp.get(i + 1) - 128) + rect_height * 9 / 2), mPaint);
                }
                canvas.drawText("������", 40, mSurfaceView.getHeight() * 4 / 5 + mSurfaceView.getHeight() / 5 / 2 - 40, mPaint);
                canvas.drawText("X2", 180, mSurfaceView.getHeight() * 4 / 5 + mSurfaceView.getHeight() / 5 / 2 - 40, mPaint);
                resp_count = resp_count + 4;
                if (resp_count > mSurfaceView.getWidth()) {
                    resp_curve_disp.clear();
                    resp_count = 0;
                }
                mSurfaceHolder.unlockCanvasAndPost(canvas);
            }


            Canvas canvas_params = mSurfaceHolder_params
                    .lockCanvas(new Rect(0, 0, mSurfaceView_params.getWidth(), mSurfaceView_params.getHeight()));// �ؼ�:��ȡ����
            Paint mPaint_params = new Paint();
            mPaint_params.setColor(Color.GREEN);// ����Ϊ��ɫ
            mPaint_params.setStrokeWidth(1);// ���û��ʴ�ϸ
            mPaint_params.setTextSize(28);

            //Draw Background
            canvas_params.drawColor(Color.BLACK);


            // Draw Frame
            mPaint_params.setColor(Color.GRAY);
            int base_info_area_width = 300;
            int base_start_p = 80;
            int base_p = (mSurfaceView_params.getHeight() - 2 * base_start_p) / 4;
            canvas_params.drawLine(0, base_start_p, mSurfaceView_params.getWidth(), base_start_p, mPaint_params);
            canvas_params.drawLine(0, base_start_p + base_p, mSurfaceView_params.getWidth(), base_start_p + base_p, mPaint_params);
            canvas_params.drawLine(0, base_start_p + base_p * 2, mSurfaceView_params.getWidth(), base_start_p + base_p * 2, mPaint_params);
            canvas_params.drawLine(0, base_start_p + base_p * 3, mSurfaceView_params.getWidth(), base_start_p + base_p * 3, mPaint_params);

            canvas_params.drawLine(0, mSurfaceView_params.getHeight() - base_start_p, mSurfaceView_params.getWidth(), mSurfaceView_params.getHeight() - base_start_p, mPaint_params);

            canvas_params.drawLine(mSurfaceView_params.getWidth() / 2, base_start_p + base_p * 2, mSurfaceView_params.getWidth() / 2, base_start_p + base_p * 4, mPaint_params);

            //Draw Alert Area
            mPaint_params.setColor(Color.rgb(0, 128, 255));
            mPaint_params.setStyle(Style.FILL);

            canvas_params.drawRect(0, 0, mSurfaceView_params.getWidth() / 2, base_start_p, mPaint_params);


            mPaint_params.setColor(Color.YELLOW);
            mPaint_params.setStyle(Style.FILL);

            canvas_params.drawRect(mSurfaceView_params.getWidth() / 2, 0, mSurfaceView_params.getWidth(), base_start_p, mPaint_params);

            //��ʱ��
            mPaint_params.setColor(Color.WHITE);// ����Ϊ��ɫ
            mPaint_params.setStrokeWidth(1);// ���û��ʴ�ϸ
            mPaint_params.setTextSize(20);
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            canvas_params.drawText(currentDateTimeString, mSurfaceView_params.getWidth() / 2 - mPaint_params.measureText(currentDateTimeString) / 2, mSurfaceView_params.getHeight() - 20, mPaint_params);
            //Draw System Alert

            //Draw Alert params

            if (alarm_tech_bio_counter > 50) {
                alarm_tech_bio_counter = 0;
                //change Alarm param display
                if (pp.tech_alarm.size() > 0) {
                    curr_tech_alarm = pp.tech_alarm.get(0);
                    pp.tech_alarm.remove(0);
                } else
                    curr_tech_alarm = "";

                if (data_bio_alarm.size() > 0) {
                    curr_bio_alarm = data_bio_alarm.get(0);
                    data_bio_alarm.remove(0);
                } else
                    curr_bio_alarm = "";

            }
            mPaint_params.setColor(Color.BLACK);// ����Ϊ��ɫ
            mPaint_params.setStrokeWidth(1);// ���û��ʴ�ϸ
            mPaint_params.setTextSize(20);
            canvas_params.drawText(curr_tech_alarm, mSurfaceView_params.getWidth() / 4 - mPaint_params.measureText(curr_tech_alarm) / 2, 40, mPaint_params);


            mPaint_params.setColor(Color.BLACK);// ����Ϊ��ɫ
            mPaint_params.setStrokeWidth(1);// ���û��ʴ�ϸ
            mPaint_params.setTextSize(20);
            canvas_params.drawText(curr_bio_alarm, mSurfaceView_params.getWidth() * 3 / 4 - mPaint_params.measureText(curr_bio_alarm) / 2, 40, mPaint_params);

            //Draw HR Area
            int base_start_hr = base_start_p;
            mPaint_params.setColor(Color.GREEN);// ����Ϊ��ɫ
            mPaint_params.setStrokeWidth(1);// ���û��ʴ�ϸ
            mPaint_params.setTextSize(30);
            canvas_params.drawText("����", 10, base_start_hr + 40, mPaint_params);
            canvas_params.drawText(hr_alert_high + "", 100 - mPaint_params.measureText(hr_alert_high + ""), base_start_hr + 80, mPaint_params);
            canvas_params.drawText(hr_alert_low + "", 100 - mPaint_params.measureText(hr_alert_low + ""), base_start_hr + 120, mPaint_params);

            mPaint_params.setStrokeWidth(3);// ���û��ʴ�ϸ
            mPaint_params.setTextSize(100);
            if (data_hr == -1 || data_hr > 250)
                canvas_params.drawText("--", mSurfaceView_params.getWidth() / 2 - 30, base_start_hr + 120, mPaint_params);
            else
                canvas_params.drawText(data_hr + "", mSurfaceView_params.getWidth() / 2 - 30, base_start_hr + 120, mPaint_params);
            //Draw BP Area ��Ѫѹ
            int base_start_bp = base_start_p + base_p;
            mPaint_params.setColor(Color.rgb(255, 128, 192));// ����Ϊ��ɫ
            mPaint_params.setStrokeWidth(1);// ���û��ʴ�ϸ
            mPaint_params.setTextSize(30);
            canvas_params.drawText("Ѫѹ", 10, base_start_bp + 40, mPaint_params);
            mPaint_params.setTextSize(20);
            canvas_params.drawText("mmHg", mSurfaceView_params.getWidth() / 2 - mPaint_params.measureText("mmHg") + 20, base_start_bp + 40, mPaint_params);
            switch (Integer.parseInt(bp_auto_inter)) {
                case 0:
                    canvas_params.drawText("�Զ�5����", mSurfaceView_params.getWidth() - mPaint_params.measureText("�Զ�5����"), base_start_bp + 40, mPaint_params);
                    break;
                case 1:
                    canvas_params.drawText("�Զ�10����", mSurfaceView_params.getWidth() - mPaint_params.measureText("�Զ�10����"), base_start_bp + 40, mPaint_params);
                    break;
                case 2:
                    canvas_params.drawText("�Զ�30����", mSurfaceView_params.getWidth() - mPaint_params.measureText("�Զ�30����"), base_start_bp + 40, mPaint_params);
                    break;
                case 3:
                    canvas_params.drawText("�Զ�1Сʱ", mSurfaceView_params.getWidth() - mPaint_params.measureText("�Զ�1Сʱ"), base_start_bp + 40, mPaint_params);
                    break;
            }
            //canvas_params.drawText("�Զ�10����",  mSurfaceView_params.getWidth()- mPaint_params.measureText("�Զ�10����"), base_start_bp+40, mPaint_params);
            mPaint_params.setTextSize(30);
            canvas_params.drawText(bp_alert_high + "", 100 - mPaint_params.measureText(bp_alert_high + ""), base_start_bp + 80, mPaint_params);
            canvas_params.drawText(bp_alert_low + "", 100 - mPaint_params.measureText(bp_alert_low + ""), base_start_bp + 120, mPaint_params);

            mPaint_params.setStrokeWidth(2);// ���û��ʴ�ϸ
            mPaint_params.setTextSize(40);
            if (data_bp_high == -1) {
                canvas_params.drawText("-/-", mSurfaceView_params.getWidth() - mPaint_params.measureText("120/90"), base_start_bp + 80, mPaint_params);
                canvas_params.drawText("(--)", mSurfaceView_params.getWidth() - mPaint_params.measureText("(100)"), base_start_bp + 120, mPaint_params);
            } else {
                canvas_params.drawText(data_bp_low + "/" + data_bp_high, mSurfaceView_params.getWidth() - mPaint_params.measureText(data_bp_low + "/" + data_bp_high), base_start_bp + 80, mPaint_params);
                canvas_params.drawText("(" + data_bp_mean + ")", mSurfaceView_params.getWidth() - mPaint_params.measureText("(100)"), base_start_bp + 120, mPaint_params);

            }
            //Draw spo2 ��Ѫ��
            int base_start_spo2 = base_start_p + base_p * 2;
            mPaint_params.setColor(Color.rgb(0, 255, 255));// ����Ϊ��ɫ
            mPaint_params.setStrokeWidth(1);// ���û��ʴ�ϸ
            mPaint_params.setTextSize(30);
            canvas_params.drawText("Ѫ��", 10, base_start_spo2 + 40, mPaint_params);
            canvas_params.drawText("%", mSurfaceView_params.getWidth() / 2 - mPaint_params.measureText("%") - 20, base_start_spo2 + 40, mPaint_params);
            canvas_params.drawText(spo2_alert_high + "", 10, base_start_spo2 + 80, mPaint_params);
            canvas_params.drawText(spo2_alert_low + "", 10, base_start_spo2 + 120, mPaint_params);

            mPaint_params.setStrokeWidth(3);// ���û��ʴ�ϸ
            mPaint_params.setTextSize(40);
            if (data_spo2 == -1 || data_spo2 == 127)
                canvas_params.drawText("--", mSurfaceView_params.getWidth() / 2 - mPaint_params.measureText(data_spo2 + ""), base_start_spo2 + 120, mPaint_params);
            else
                canvas_params.drawText(data_spo2 + "", mSurfaceView_params.getWidth() / 2 - mPaint_params.measureText(data_spo2 + "") - 10, base_start_spo2 + 120, mPaint_params);
            //Draw pulse ������
            int base_start_pulse = base_start_p + base_p * 2;
            mPaint_params.setColor(Color.rgb(0, 255, 255));// ����Ϊ��ɫ
            mPaint_params.setStrokeWidth(1);// ���û��ʴ�ϸ
            mPaint_params.setTextSize(30);
            canvas_params.drawText("����", mSurfaceView_params.getWidth() / 2 + 10, base_start_pulse + 40, mPaint_params);

            canvas_params.drawText(hr_alert_high + "", mSurfaceView_params.getWidth() / 2 + 10, base_start_pulse + 80, mPaint_params);
            canvas_params.drawText(hr_alert_low + "", mSurfaceView_params.getWidth() / 2 + 10, base_start_pulse + 120, mPaint_params);

            mPaint_params.setStrokeWidth(3);// ���û��ʴ�ϸ
            mPaint_params.setTextSize(40);
            if (data_pulse == -1)
                canvas_params.drawText("--", mSurfaceView_params.getWidth() - mPaint_params.measureText("98"), base_start_pulse + 120, mPaint_params);
            else
                canvas_params.drawText(data_pulse + "", mSurfaceView_params.getWidth() - mPaint_params.measureText("98"), base_start_pulse + 120, mPaint_params);

            //Draw resp ��������
            int base_start_resp = base_start_p + base_p * 3;
            mPaint_params.setColor(Color.YELLOW);// ����Ϊ��ɫ
            mPaint_params.setStrokeWidth(1);// ���û��ʴ�ϸ
            mPaint_params.setTextSize(30);
            canvas_params.drawText("������", 10, base_start_resp + 40, mPaint_params);

            canvas_params.drawText(resp_alert_high + "", 10, base_start_resp + 80, mPaint_params);
            canvas_params.drawText(resp_alert_low + "", 10, base_start_resp + 120, mPaint_params);

            mPaint_params.setStrokeWidth(3);// ���û��ʴ�ϸ
            mPaint_params.setTextSize(40);
            if (data_resp == -1 || data_resp > 250)
                canvas_params.drawText("--", mSurfaceView_params.getWidth() / 2 - mPaint_params.measureText("98") - 10, base_start_resp + 120, mPaint_params);
            else
                canvas_params.drawText(data_resp + "", mSurfaceView_params.getWidth() / 2 - mPaint_params.measureText("98") - 10, base_start_resp + 120, mPaint_params);


            //Draw temp ������
            int base_start_temp = base_start_p + base_p * 3;
            mPaint_params.setColor(Color.LTGRAY);// ����Ϊ��ɫ
            mPaint_params.setStrokeWidth(1);// ���û��ʴ�ϸ
            mPaint_params.setTextSize(30);
            canvas_params.drawText("����", mSurfaceView_params.getWidth() / 2 + 10, base_start_temp + 40, mPaint_params);

            canvas_params.drawText(temp_alert_high + "", mSurfaceView_params.getWidth() / 2 + 10, base_start_temp + 80, mPaint_params);
            canvas_params.drawText(temp_alert_low + "", mSurfaceView_params.getWidth() / 2 + 10, base_start_temp + 120, mPaint_params);

            mPaint_params.setStrokeWidth(3);// ���û��ʴ�ϸ
            mPaint_params.setTextSize(40);
            if (data_temp == -1)
                canvas_params.drawText("--", mSurfaceView_params.getWidth() - mPaint_params.measureText("36.5"), base_start_temp + 120, mPaint_params);
            else
                canvas_params.drawText(data_temp + "", mSurfaceView_params.getWidth() - mPaint_params.measureText("36.5"), base_start_temp + 120, mPaint_params);

            mSurfaceHolder_params.unlockCanvasAndPost(canvas_params);

            count = count + mSurfaceView.getWidth() / 100 - 1;
            if (count > mSurfaceView.getWidth()) {
                count = 0;
                hr_I_disp.clear();
                hr_II_disp.clear();
                hr_III_disp.clear();
            }
            handler.postDelayed(task, 40);

        }
    };

    //�ͻ����߳�
    class ClientThread implements Runnable {

        private boolean isAlive = false;
        private InputStream in = null;
        private OutputStream out = null;

        private int cache_data_offset = 0;//��������ƫ��

        public boolean isSocketAvilable() {
            return isAlive;
        }

        public void sendCmd(String cmd) {
            try {
                out.write(BytesUtil.hexStringToBytes(cmd));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (true) {
                try {

                    if (socket == null)
                        socket = new Socket(ip_addr, SERVER_PORT);
                    PrintWriter stat_test = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                    if (stat_test.checkError())
                        socket = new Socket(ip_addr, SERVER_PORT);
                    in = socket.getInputStream();
                    out = socket.getOutputStream();
                    if (cmd_list.size() > 0)
                        for (int i = 0; i < cmd_list.size(); i++)
                            out.write(BytesUtil.hexStringToBytes(cmd_list.get(i)));
                    cmd_list.clear();

                    int size;
                    byte[] buffer = new byte[2048];
                    size = socket.getInputStream().read(buffer);
                    if (size > 0) {
                        byte[] temp_buffer = new byte[size];
                        System.arraycopy(buffer, 0, temp_buffer, 0, size);
                        data_process(true, BytesUtil.bytesToHexString(temp_buffer));
                    }
                    Thread.sleep(20);
                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    //�������� ͬ����
    synchronized static String data_process(boolean dir, String s) {

        String s_data;
        String[] temp_str;
        if (dir) {  //write data
            cache_data = cache_data + s;
            return "";
        } else {    //write data

            s_data = cache_data;
            temp_str = cache_data.split("55aa");
            if (temp_str.length > 0)
                cache_data = "55aa" + temp_str[temp_str.length - 1];
            return s_data;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Params
        getParamSettings();

        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 100);
        loadId = soundPool.load(getBaseContext(), R.raw.alarm, 1);

        setContentView(R.layout.activity_monitorscreen);

        final View controlsView = findViewById(R.id.fullscreen_content);
        final View contentView = findViewById(R.id.fullscreen_content_controls);

        String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView1);
        mSurfaceHolder = mSurfaceView.getHolder();

        mSurfaceView_params = (SurfaceView) findViewById(R.id.surfaceView2);
        mSurfaceHolder_params = mSurfaceView_params.getHolder();

        cmd_list.add(pp.spo2_out_control(true));
        cmd_list.add(pp.resp_out_control(true));
        this.ct = new ClientThread();
        new Thread(ct).start();

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, controlsView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                            if (mControlsHeight == 0) {
                                mControlsHeight = controlsView.getHeight();
                            }
                            if (mShortAnimTime == 0) {
                                mShortAnimTime = getResources().getInteger(
                                        android.R.integer.config_shortAnimTime);
                            }
                            controlsView
                                    .animate()
                                    .translationY(visible ? 0 : mControlsHeight)
                                    .setDuration(mShortAnimTime);
                        } else {
                            controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
                        }

                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });

        // �����û������ֶ���ʾ������ϵͳ����.
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }
            }
        });

        setParamSetting("bp_alert_switch", "off");

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.

    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(task);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hr_I.clear();
        hr_II.clear();
        hr_III.clear();
        hr_avr.clear();
        hr_avl.clear();
        hr_v.clear();
        spo2_curve.clear();
        resp_curve.clear();
        handler.postDelayed(task, 500);
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    //��ȡ��������
    private void getParamSettings() {

        try {
            ps_data = getHelper().getParamsSettingDataDao();

            //������IP����
            List<ParamsSettingData> list = ps_data.queryForEq("param_name", "IP");
            if (list.size() > 0) {

                for (ParamsSettingData l : list)
                    ip_addr = l.param_value;

            } else {
                ParamsSettingData psd = new ParamsSettingData("IP", "10.10.100.254");
                ps_data.create(psd);
                ip_addr = "10.10.100.254";
            }
            //hr_alert_high ���ʱ�������
            list = ps_data.queryForEq("param_name", "hr_alert_high");
            if (list.size() > 0) {

                for (ParamsSettingData l : list)
                    hr_alert_high = Integer.parseInt(l.param_value);

            } else {
                hr_alert_high = 0;
                ParamsSettingData psd = new ParamsSettingData("hr_alert_high", "" + hr_alert_high);
                ps_data.create(psd);

            }

            //hr_alert_low ���ʱ�������
            list = ps_data.queryForEq("param_name", "hr_alert_low");
            if (list.size() > 0) {

                for (ParamsSettingData l : list)
                    hr_alert_low = Integer.parseInt(l.param_value);

            } else {
                hr_alert_low = 0;
                ParamsSettingData psd = new ParamsSettingData("hr_alert_low", "" + hr_alert_low);
                ps_data.create(psd);

            }

            //bp_alert_high Ѫѹ��������
            list = ps_data.queryForEq("param_name", "bp_alert_high");
            if (list.size() > 0) {

                for (ParamsSettingData l : list)
                    bp_alert_high = Integer.parseInt(l.param_value);

            } else {
                bp_alert_high = 0;
                ParamsSettingData psd = new ParamsSettingData("bp_alert_high", "" + bp_alert_high);
                ps_data.create(psd);

            }

            //bp_alert_low Ѫѹ��������
            list = ps_data.queryForEq("param_name", "bp_alert_low");
            if (list.size() > 0) {

                for (ParamsSettingData l : list)
                    bp_alert_low = Integer.parseInt(l.param_value);

            } else {
                bp_alert_low = 0;
                ParamsSettingData psd = new ParamsSettingData("bp_alert_low", "" + bp_alert_low);
                ps_data.create(psd);

            }

            //spo2_alert_high Ѫ����������
            list = ps_data.queryForEq("param_name", "spo2_alert_high");
            if (list.size() > 0) {

                for (ParamsSettingData l : list)
                    spo2_alert_high = Integer.parseInt(l.param_value);

            } else {
                spo2_alert_high = 0;
                ParamsSettingData psd = new ParamsSettingData("spo2_alert_high", "" + spo2_alert_high);
                ps_data.create(psd);

            }

            //spo2_alert_low Ѫ����������
            list = ps_data.queryForEq("param_name", "spo2_alert_low");
            if (list.size() > 0) {

                for (ParamsSettingData l : list)
                    spo2_alert_low = Integer.parseInt(l.param_value);

            } else {
                spo2_alert_low = 0;
                ParamsSettingData psd = new ParamsSettingData("spo2_alert_low", "" + spo2_alert_low);
                ps_data.create(psd);

            }

            //resp_alert_high �����ʱ�������
            list = ps_data.queryForEq("param_name", "resp_alert_high");
            if (list.size() > 0) {

                for (ParamsSettingData l : list)
                    resp_alert_high = Integer.parseInt(l.param_value);

            } else {
                resp_alert_high = 0;
                ParamsSettingData psd = new ParamsSettingData("resp_alert_high", "" + resp_alert_high);
                ps_data.create(psd);

            }

            //resp_alert_low �����ʱ�������
            list = ps_data.queryForEq("param_name", "resp_alert_low");
            if (list.size() > 0) {

                for (ParamsSettingData l : list)
                    resp_alert_low = Integer.parseInt(l.param_value);

            } else {
                resp_alert_low = 0;
                ParamsSettingData psd = new ParamsSettingData("resp_alert_low", "" + resp_alert_low);
                ps_data.create(psd);

            }
            //temp_alert_switch ���±��� ����
            list = ps_data.queryForEq("param_name", "temp_alert_switch");
            if (list.size() > 0) {

                for (ParamsSettingData l : list)
                    temp_alert_switch = l.param_value;

            } else {
                temp_alert_switch = "";
                ParamsSettingData psd = new ParamsSettingData("temp_alert_switch", temp_alert_switch);
                ps_data.create(psd);

            }

            //temp_alert_high ���±�������
            list = ps_data.queryForEq("param_name", "temp_alert_high");
            if (list.size() > 0) {

                for (ParamsSettingData l : list)
                    temp_alert_high = Float.parseFloat(l.param_value);

            } else {
                temp_alert_high = 0;
                ParamsSettingData psd = new ParamsSettingData("temp_alert_high", "" + temp_alert_high);
                ps_data.create(psd);

            }

            //temp_alert_low ���±�������
            list = ps_data.queryForEq("param_name", "temp_alert_low");
            if (list.size() > 0) {

                for (ParamsSettingData l : list)
                    temp_alert_low = Float.parseFloat(l.param_value);

            } else {
                temp_alert_low = 0;
                ParamsSettingData psd = new ParamsSettingData("temp_alert_low", "" + temp_alert_low);
                ps_data.create(psd);

            }

            //sys_volume
            list = ps_data.queryForEq("param_name", "sys_volume");
            if (list.size() > 0) {

                for (ParamsSettingData l : list)
                    sys_volume = Float.parseFloat(l.param_value);

            } else {
                sys_volume = 0;
                ParamsSettingData psd = new ParamsSettingData("sys_volume", "" + sys_volume);
                ps_data.create(psd);

            }

            //ecg_filter_mode �ĵ��˲�ģʽ
            list = ps_data.queryForEq("param_name", "ecg_filter_mode");
            if (list.size() > 0) {

                for (ParamsSettingData l : list)
                    ecg_filter_mode = l.param_value;

            } else {
                ecg_filter_mode = "";
                ParamsSettingData psd = new ParamsSettingData("ecg_filter_mode", "" + ecg_filter_mode);
                ps_data.create(psd);

            }

            //ecg_mag �ĵ�ͼ
            list = ps_data.queryForEq("param_name", "ecg_mag");
            if (list.size() > 0) {

                for (ParamsSettingData l : list)
                    ecg_mag = l.param_value;

            } else {
                ecg_mag = "";
                ParamsSettingData psd = new ParamsSettingData("ecg_mag", "" + ecg_mag);
                ps_data.create(psd);

            }
            //resp_curve_mag ����������ͼ
            list = ps_data.queryForEq("param_name", "resp_curve_mag");
            if (list.size() > 0) {

                for (ParamsSettingData l : list)
                    resp_curve_mag = l.param_value;

            } else {
                resp_curve_mag = "";
                ParamsSettingData psd = new ParamsSettingData("resp_curve_mag", "" + resp_curve_mag);
                ps_data.create(psd);

            }
            //ecg_channel �ĵ�ͨ��
            list = ps_data.queryForEq("param_name", "ecg_channel");
            if (list.size() > 0) {

                for (ParamsSettingData l : list)
                    ecg_channel = l.param_value;

            } else {
                ecg_channel = "";
                ParamsSettingData psd = new ParamsSettingData("ecg_channel", "" + ecg_channel);
                ps_data.create(psd);

            }
            //bp_auto_switch Ѫѹ
            list = ps_data.queryForEq("param_name", "bp_auto_switch");
            if (list.size() > 0) {

                for (ParamsSettingData l : list)
                    bp_auto_switch = l.param_value;

            } else {
                bp_auto_switch = "";
                ParamsSettingData psd = new ParamsSettingData("bp_auto_switch", "" + bp_auto_switch);
                ps_data.create(psd);

            }
            //hr_alert_switch ����
            list = ps_data.queryForEq("param_name", "hr_alert_switch");
            if (list.size() > 0) {

                for (ParamsSettingData l : list)
                    hr_alert_switch = l.param_value;

            } else {
                hr_alert_switch = "";
                ParamsSettingData psd = new ParamsSettingData("hr_alert_switch", "" + hr_alert_switch);
                ps_data.create(psd);

            }

            //bp_alert_switch Ѫѹ����
            list = ps_data.queryForEq("param_name", "bp_alert_switch");
            if (list.size() > 0) {

                for (ParamsSettingData l : list)
                    bp_alert_switch = l.param_value;

            } else {
                bp_alert_switch = "";
                ParamsSettingData psd = new ParamsSettingData("bp_alert_switch", "" + bp_alert_switch);
                ps_data.create(psd);

            }
            //bp_auto_inter Ѫѹ�Զ�����ʱ��
            list = ps_data.queryForEq("param_name", "bp_auto_inter");
            if (list.size() > 0) {

                for (ParamsSettingData l : list)
                    bp_auto_inter = l.param_value;

                if (bp_auto_inter.length() == 0) {
                    bp_auto_inter = "0";
                    ParamsSettingData psd = new ParamsSettingData("bp_auto_inter", "" + bp_auto_inter);
                    ps_data.create(psd);
                }

            } else {
                bp_auto_inter = "0";
                ParamsSettingData psd = new ParamsSettingData("bp_auto_inter", "" + bp_auto_inter);
                ps_data.create(psd);

            }

            //bp_mode Ѫѹģʽ
            list = ps_data.queryForEq("param_name", "bp_mode");
            if (list.size() > 0) {

                for (ParamsSettingData l : list)
                    bp_mode = l.param_value;

            } else {
                bp_mode = "";
                ParamsSettingData psd = new ParamsSettingData("bp_mode", "" + bp_mode);
                ps_data.create(psd);

            }

            //spo2_alert_switch Ѫ����������
            list = ps_data.queryForEq("param_name", "spo2_alert_switch");
            if (list.size() > 0) {

                for (ParamsSettingData l : list)
                    spo2_alert_switch = l.param_value;

            } else {
                spo2_alert_switch = "";
                ParamsSettingData psd = new ParamsSettingData("spo2_alert_switch", "" + spo2_alert_switch);
                ps_data.create(psd);

            }
            //resp_alert_switch �����ʱ�������
            list = ps_data.queryForEq("param_name", "resp_alert_switch");
            if (list.size() > 0) {

                for (ParamsSettingData l : list)
                    resp_alert_switch = l.param_value;

            } else {
                resp_alert_switch = "";
                ParamsSettingData psd = new ParamsSettingData("resp_alert_switch", "" + resp_alert_switch);
                ps_data.create(psd);

            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    //���ò���
    private void setParamSetting(String p_name, String p_value) {
        try {
            ps_data = getHelper().getParamsSettingDataDao();

            List<ParamsSettingData> list = ps_data.queryForEq("param_name", p_name);
            if (list.size() > 0)//��ѯ�������ݲ�Ϊ��
            {
                for (ParamsSettingData l : list) {
                    l.param_value = p_value;
                    ps_data.update(l);//��������
                }

            } else {
                ParamsSettingData psd = new ParamsSettingData(p_name, p_value);
                ps_data.create(psd);//�����µ�����

            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        SubMenu subMenu0 = menu.addSubMenu(Menu.NONE, 0, 0, "��������");
        SubMenu subMenu1 = menu.addSubMenu(Menu.NONE, 1, 1, "ά������");
        SubMenu subMenu2 = menu.addSubMenu(Menu.NONE, 2, 2, "�ĵ�����");
        SubMenu subMenu3 = menu.addSubMenu(Menu.NONE, 3, 3, "Ѫѹ����");
        SubMenu subMenu4 = menu.addSubMenu(Menu.NONE, 4, 4, "����Ѫѹ����");
        SubMenu subMenu5 = menu.addSubMenu(Menu.NONE, 5, 5, "ר��ϵͳ");


        //��������
        subMenu0.add(Menu.NONE, 10, 0, "���ʱ�����");
        subMenu0.add(Menu.NONE, 11, 1, "Ѫѹ������");
        subMenu0.add(Menu.NONE, 12, 2, "Ѫ��������");
        subMenu0.add(Menu.NONE, 13, 3, "�����ʱ�����");
        subMenu0.add(Menu.NONE, 14, 4, "���±�����");
        subMenu0.add(Menu.NONE, 15, 5, "�˳�");
        //ά������
        subMenu1.add(Menu.NONE, 20, 0, "©�����");
        subMenu1.add(Menu.NONE, 21, 1, "��̬ѹ��У׼");
        //subMenu1.add(Menu.NONE,22,2,"ѹ������У׼");
        subMenu1.add(Menu.NONE, 23, 3, "Ѫѹ����У׼");
        subMenu1.add(Menu.NONE, 24, 4, "���²���У׼");
        //subMenu1.add(Menu.NONE,25,5,"����ʱ������");
        subMenu1.add(Menu.NONE, 26, 6, "��������");
        subMenu1.add(Menu.NONE, 27, 7, "������IP����");
        subMenu1.add(Menu.NONE, 28, 8, "�˳�");
        //�ĵ�����
        subMenu2.add(Menu.NONE, 30, 0, "�ĵ��˲�ģʽ");
        subMenu2.add(Menu.NONE, 31, 1, "�ĵ粨������");
        subMenu2.add(Menu.NONE, 32, 2, "������������");
        subMenu2.add(Menu.NONE, 33, 3, "�ĵ絼���л�");
        subMenu2.add(Menu.NONE, 34, 4, "�˳�");
        //Ѫѹ����
        subMenu3.add(Menu.NONE, 40, 0, "�Զ��������");
        subMenu3.add(Menu.NONE, 41, 1, "Ԥ����ѹ��");
        subMenu3.add(Menu.NONE, 43, 2, "Ѫѹģʽ����");
        subMenu3.add(Menu.NONE, 42, 3, "�˳�");
        //����Ѫѹ����
        //ר��ϵͳ
        subMenu5.add(Menu.NONE, 60, 0, "�ķθ����Ż�");
        subMenu5.add(Menu.NONE, 61, 1, "��еͨ���Ż�");
        subMenu5.add(Menu.NONE, 62, 2, "�޼�ϰ�ѹ�������ɷ���");
        subMenu5.add(Menu.NONE, 63, 3, "ʧѪ�ݿ˳̶ȱ�ʶ");
        subMenu5.add(Menu.NONE, 64, 4, "�˳�");

        return true;
    }

    //Activity������������֮��Ļص�
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        bp_stat_inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        bp_stat_view = bp_stat_inflater.inflate(R.layout.stat_bp_cali, null);
        bp_stat_value_text = (TextView) bp_stat_view.findViewById(R.id.thre);
        handler.postDelayed(task, 500);
        delayedHide(100);
    }

    //�˵�
    @SuppressWarnings("deprecation")
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == 27) {
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.ip_setting_win, null);

            final EditText ip_field = (EditText) view.findViewById(R.id.editText1);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MonitorscreenActivity.this, android.R.layout.simple_spinner_item, new String[]{"��", "��"});

            AlertDialog.Builder ad = new AlertDialog.Builder(MonitorscreenActivity.this);
            ad.setView(view);
            ad.setTitle("������IP����");
            ip_field.setText(ip_addr);
            selfdialog = ad.create();
            selfdialog.setButton("ȡ��", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    selfdialog.cancel();
                }
            });
            selfdialog.setButton2("����", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setParamSetting("IP", ip_field.getText().toString());
                    getParamSettings();
                    selfdialog.cancel();
                }
            });
            selfdialog.show();
        }
        if (item.getItemId() == 4) {
            ct.sendCmd(pp.bp_control(true));

        }
        if (item.getItemId() == 10) {
            //����view�ӵ�ǰactivity��ȡloginactivity
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.hr_alert_param, null);
            // Spinner spinner = (Spinner)view.findViewById(R.id.xdlb);
            final ToggleButton hr_alert_switch_field = (ToggleButton) view.findViewById(R.id.ToggleButton1);
            final TextView hr_alert_high_field = (TextView) view.findViewById(R.id.textView1);
            final TextView hr_alert_low_field = (TextView) view.findViewById(R.id.textView2);
            final Button hr_alert_high_field_up = (Button) view.findViewById(R.id.sxtj);
            final Button hr_alert_high_field_down = (Button) view.findViewById(R.id.sxzs);

            final Button hr_alert_low_field_up = (Button) view.findViewById(R.id.xxtj);
            final Button hr_alert_low_field_down = (Button) view.findViewById(R.id.xxzs);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MonitorscreenActivity.this, android.R.layout.simple_spinner_item, new String[]{"����ģʽ", "�໤ģʽ", "���ģʽ"});
            //spinner.setAdapter(adapter);
            AlertDialog.Builder ad = new AlertDialog.Builder(MonitorscreenActivity.this);
            ad.setView(view);
            ad.setTitle("���ʱ�������");

            if (hr_alert_switch.equals("on"))
                hr_alert_switch_field.setChecked(true);
            else
                hr_alert_switch_field.setChecked(false);
            //String s = (String) hr_alert_high_field.getText();
            hr_alert_high_field.setText(hr_alert_high + "");
            hr_alert_low_field.setText(hr_alert_low + "");

            hr_alert_high_field_up.setOnClickListener(new Button.OnClickListener() {//��������
                public void onClick(View v) {
                    hr_alert_high_field.setText(Integer.parseInt(hr_alert_high_field.getText().toString()) + 1 + "");
                }
            });

            hr_alert_high_field_down.setOnClickListener(new Button.OnClickListener() {//��������
                public void onClick(View v) {
                    if (Integer.parseInt(hr_alert_high_field.getText().toString()) - 1 >= Integer.parseInt(hr_alert_low_field.getText().toString()))
                        hr_alert_high_field.setText(Integer.parseInt(hr_alert_high_field.getText().toString()) - 1 + "");
                }
            });

            hr_alert_low_field_up.setOnClickListener(new Button.OnClickListener() {//��������
                public void onClick(View v) {
                    if (Integer.parseInt(hr_alert_low_field.getText().toString()) + 1 <= Integer.parseInt(hr_alert_high_field.getText().toString()))
                        hr_alert_low_field.setText(Integer.parseInt(hr_alert_low_field.getText().toString()) + 1 + "");
                }
            });

            hr_alert_low_field_down.setOnClickListener(new Button.OnClickListener() {//��������
                public void onClick(View v) {
                    if (Integer.parseInt(hr_alert_low_field.getText().toString()) - 1 >= 0)
                        hr_alert_low_field.setText(Integer.parseInt(hr_alert_low_field.getText().toString()) - 1 + "");
                }
            });

            selfdialog = ad.create();
            selfdialog.setButton("ȡ��", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    selfdialog.cancel();
                }
            });
            selfdialog.setButton2("����", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (hr_alert_switch_field.isChecked())
                        setParamSetting("hr_alert_switch", "on");
                    else
                        setParamSetting("hr_alert_switch", "off");
                    setParamSetting("hr_alert_high", hr_alert_high_field.getText().toString());
                    setParamSetting("hr_alert_low", hr_alert_low_field.getText().toString());

                    getParamSettings();
                    selfdialog.cancel();
                }
            });
            selfdialog.show();
        }
        if (item.getItemId() == 11) {
            //����view�ӵ�ǰactivity��ȡloginactivity
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.bp_alert_param, null);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MonitorscreenActivity.this, android.R.layout.simple_spinner_item, new String[]{"����ģʽ", "�໤ģʽ", "���ģʽ"});
            final ToggleButton bp_alert_switch_field = (ToggleButton) view.findViewById(R.id.ToggleButton1);
            final TextView bp_alert_high_field = (TextView) view.findViewById(R.id.textView1);
            final TextView bp_alert_low_field = (TextView) view.findViewById(R.id.textView2);
            final Button bp_alert_high_field_up = (Button) view.findViewById(R.id.sxtj);
            final Button bp_alert_high_field_down = (Button) view.findViewById(R.id.sxzs);

            final Button bp_alert_low_field_up = (Button) view.findViewById(R.id.xxtj);
            final Button bp_alert_low_field_down = (Button) view.findViewById(R.id.xxzs);

            AlertDialog.Builder ad = new AlertDialog.Builder(MonitorscreenActivity.this);
            ad.setView(view);
            ad.setTitle("Ѫѹ��������");

            if (bp_alert_switch.equals("on"))
                bp_alert_switch_field.setChecked(true);
            else
                bp_alert_switch_field.setChecked(false);
            bp_alert_high_field.setText(bp_alert_high + "");
            bp_alert_low_field.setText(bp_alert_low + "");

            bp_alert_high_field_up.setOnClickListener(new Button.OnClickListener() {//��������
                public void onClick(View v) {
                    //if(Integer.parseInt(hr_alert_high_field.getText().toString())-1 >= Integer.parseInt(hr_alert_low_field.getText().toString()))
                    bp_alert_high_field.setText(Integer.parseInt(bp_alert_high_field.getText().toString()) + 1 + "");
                }
            });

            bp_alert_high_field_down.setOnClickListener(new Button.OnClickListener() {//��������
                public void onClick(View v) {
                    if (Integer.parseInt(bp_alert_high_field.getText().toString()) - 1 >= Integer.parseInt(bp_alert_low_field.getText().toString()))
                        bp_alert_high_field.setText(Integer.parseInt(bp_alert_high_field.getText().toString()) - 1 + "");
                }
            });

            bp_alert_low_field_up.setOnClickListener(new Button.OnClickListener() {//��������
                public void onClick(View v) {
                    if (Integer.parseInt(bp_alert_low_field.getText().toString()) + 1 <= Integer.parseInt(bp_alert_high_field.getText().toString()))
                        bp_alert_low_field.setText(Integer.parseInt(bp_alert_low_field.getText().toString()) + 1 + "");
                }
            });

            bp_alert_low_field_down.setOnClickListener(new Button.OnClickListener() {//��������
                public void onClick(View v) {
                    if (Integer.parseInt(bp_alert_low_field.getText().toString()) - 1 >= 0)
                        bp_alert_low_field.setText(Integer.parseInt(bp_alert_low_field.getText().toString()) - 1 + "");
                }
            });

            selfdialog = ad.create();
            selfdialog.setButton("ȡ��", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    selfdialog.cancel();
                }
            });
            selfdialog.setButton2("����", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (bp_alert_switch_field.isChecked())
                        setParamSetting("bp_alert_switch", "on");
                    else
                        setParamSetting("bp_alert_switch", "off");
                    //setParamSetting("bp_alert_switch",bp_alert_switch_field.getText().toString());
                    setParamSetting("bp_alert_high", bp_alert_high_field.getText().toString());
                    setParamSetting("bp_alert_low", bp_alert_low_field.getText().toString());

                    getParamSettings();
                    selfdialog.cancel();
                }
            });
            selfdialog.show();
        }
        if (item.getItemId() == 12) {
            //����view�ӵ�ǰactivity��ȡloginactivity
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.spo2_alert_param, null);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MonitorscreenActivity.this, android.R.layout.simple_spinner_item, new String[]{"����ģʽ", "�໤ģʽ", "���ģʽ"});
            final ToggleButton spo2_alert_switch_field = (ToggleButton) view.findViewById(R.id.ToggleButton1);
            final TextView spo2_alert_high_field = (TextView) view.findViewById(R.id.textView1);
            final TextView spo2_alert_low_field = (TextView) view.findViewById(R.id.textView2);

            final Button spo2_alert_high_field_up = (Button) view.findViewById(R.id.sxtj);
            final Button spo2_alert_high_field_down = (Button) view.findViewById(R.id.sxzs);

            final Button spo2_alert_low_field_up = (Button) view.findViewById(R.id.xxtj);
            final Button spo2_alert_low_field_down = (Button) view.findViewById(R.id.xxzs);

            AlertDialog.Builder ad = new AlertDialog.Builder(MonitorscreenActivity.this);
            ad.setView(view);
            ad.setTitle("Ѫ����������");

            if (spo2_alert_switch.equals("on"))
                spo2_alert_switch_field.setChecked(true);
            else
                spo2_alert_switch_field.setChecked(false);
            spo2_alert_high_field.setText(spo2_alert_high + "");
            spo2_alert_low_field.setText(spo2_alert_low + "");

            spo2_alert_high_field_up.setOnClickListener(new Button.OnClickListener() {//��������
                public void onClick(View v) {
                    //if(Integer.parseInt(hr_alert_high_field.getText().toString())-1 >= Integer.parseInt(hr_alert_low_field.getText().toString()))
                    spo2_alert_high_field.setText(Integer.parseInt(spo2_alert_high_field.getText().toString()) + 1 + "");
                }
            });

            spo2_alert_high_field_down.setOnClickListener(new Button.OnClickListener() {//��������
                public void onClick(View v) {
                    if (Integer.parseInt(spo2_alert_high_field.getText().toString()) - 1 >= Integer.parseInt(spo2_alert_low_field.getText().toString()))
                        spo2_alert_high_field.setText(Integer.parseInt(spo2_alert_high_field.getText().toString()) - 1 + "");
                }
            });

            spo2_alert_low_field_up.setOnClickListener(new Button.OnClickListener() {//��������
                public void onClick(View v) {
                    if (Integer.parseInt(spo2_alert_low_field.getText().toString()) + 1 <= Integer.parseInt(spo2_alert_high_field.getText().toString()))
                        spo2_alert_low_field.setText(Integer.parseInt(spo2_alert_low_field.getText().toString()) + 1 + "");
                }
            });

            spo2_alert_low_field_down.setOnClickListener(new Button.OnClickListener() {//��������
                public void onClick(View v) {
                    if (Integer.parseInt(spo2_alert_low_field.getText().toString()) - 1 >= 0)
                        spo2_alert_low_field.setText(Integer.parseInt(spo2_alert_low_field.getText().toString()) - 1 + "");
                }
            });

            selfdialog = ad.create();
            selfdialog.setButton("ȡ��", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    selfdialog.cancel();
                }
            });
            selfdialog.setButton2("����", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (spo2_alert_switch_field.isChecked())
                        setParamSetting("spo2_alert_switch", "on");
                    else
                        setParamSetting("spo2_alert_switch", "off");
                    setParamSetting("spo2_alert_high", spo2_alert_high_field.getText().toString());
                    setParamSetting("spo2_alert_low", spo2_alert_low_field.getText().toString());
                    getParamSettings();
                    selfdialog.cancel();
                }
            });
            selfdialog.show();
        }
        if (item.getItemId() == 13) {
            //����view�ӵ�ǰactivity��ȡloginactivity
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.resp_alert_param, null);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MonitorscreenActivity.this, android.R.layout.simple_spinner_item, new String[]{"����ģʽ", "�໤ģʽ", "���ģʽ"});
            final ToggleButton resp_alert_switch_field = (ToggleButton) view.findViewById(R.id.ToggleButton1);
            final TextView resp_alert_high_field = (TextView) view.findViewById(R.id.textView1);
            final TextView resp_alert_low_field = (TextView) view.findViewById(R.id.textView2);

            final Button resp_alert_high_field_up = (Button) view.findViewById(R.id.sxtj);
            final Button resp_alert_high_field_down = (Button) view.findViewById(R.id.sxzs);

            final Button resp_alert_low_field_up = (Button) view.findViewById(R.id.xxtj);
            final Button resp_alert_low_field_down = (Button) view.findViewById(R.id.xxzs);
            AlertDialog.Builder ad = new AlertDialog.Builder(MonitorscreenActivity.this);
            ad.setView(view);
            ad.setTitle("�����ʱ�������");

            if (resp_alert_switch.equals("on"))
                resp_alert_switch_field.setChecked(true);
            else
                resp_alert_switch_field.setChecked(false);
            resp_alert_high_field.setText(resp_alert_high + "");
            resp_alert_low_field.setText(resp_alert_low + "");

            resp_alert_high_field_up.setOnClickListener(new Button.OnClickListener() {//��������
                public void onClick(View v) {
                    //if(Integer.parseInt(hr_alert_high_field.getText().toString())-1 >= Integer.parseInt(hr_alert_low_field.getText().toString()))
                    resp_alert_high_field.setText(Integer.parseInt(resp_alert_high_field.getText().toString()) + 1 + "");
                }
            });

            resp_alert_high_field_down.setOnClickListener(new Button.OnClickListener() {//��������
                public void onClick(View v) {
                    if (Integer.parseInt(resp_alert_high_field.getText().toString()) - 1 >= Integer.parseInt(resp_alert_low_field.getText().toString()))
                        resp_alert_high_field.setText(Integer.parseInt(resp_alert_high_field.getText().toString()) - 1 + "");
                }
            });

            resp_alert_low_field_up.setOnClickListener(new Button.OnClickListener() {//��������
                public void onClick(View v) {
                    if (Integer.parseInt(resp_alert_low_field.getText().toString()) + 1 <= Integer.parseInt(resp_alert_high_field.getText().toString()))
                        resp_alert_low_field.setText(Integer.parseInt(resp_alert_low_field.getText().toString()) + 1 + "");
                }
            });

            resp_alert_low_field_down.setOnClickListener(new Button.OnClickListener() {//��������
                public void onClick(View v) {
                    if (Integer.parseInt(resp_alert_low_field.getText().toString()) - 1 >= 0)
                        resp_alert_low_field.setText(Integer.parseInt(resp_alert_low_field.getText().toString()) - 1 + "");
                }
            });

            selfdialog = ad.create();
            selfdialog.setButton("ȡ��", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    selfdialog.cancel();
                }
            });
            selfdialog.setButton2("����", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (resp_alert_switch_field.isChecked())
                        setParamSetting("resp_alert_switch", "on");
                    else
                        setParamSetting("resp_alert_switch", "off");
                    setParamSetting("resp_alert_high", resp_alert_high_field.getText().toString());
                    setParamSetting("resp_alert_low", resp_alert_low_field.getText().toString());

                    getParamSettings();
                    selfdialog.cancel();
                }
            });
            selfdialog.show();
        }
        if (item.getItemId() == 14) {
            //����view�ӵ�ǰactivity��ȡloginactivity
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.temp_alert_param, null);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MonitorscreenActivity.this, android.R.layout.simple_spinner_item, new String[]{"����ģʽ", "�໤ģʽ", "���ģʽ"});
            final ToggleButton temp_alert_switch_field = (ToggleButton) view.findViewById(R.id.ToggleButton1);
            final TextView temp_alert_high_field = (TextView) view.findViewById(R.id.textView1);
            final TextView temp_alert_low_field = (TextView) view.findViewById(R.id.textView2);

            final Button temp_alert_high_field_up = (Button) view.findViewById(R.id.sxtj);
            final Button temp_alert_high_field_down = (Button) view.findViewById(R.id.sxzs);

            final Button temp_alert_low_field_up = (Button) view.findViewById(R.id.xxtj);
            final Button temp_alert_low_field_down = (Button) view.findViewById(R.id.xxzs);

            AlertDialog.Builder ad = new AlertDialog.Builder(MonitorscreenActivity.this);
            ad.setView(view);
            ad.setTitle("���±�������");

            if (temp_alert_switch.equals("on"))
                temp_alert_switch_field.setChecked(true);
            else
                temp_alert_switch_field.setChecked(false);
            temp_alert_high_field.setText(temp_alert_high + "");
            temp_alert_low_field.setText(temp_alert_low + "");


            temp_alert_high_field_up.setOnClickListener(new Button.OnClickListener() {//��������
                public void onClick(View v) {
                    //if(Integer.parseInt(hr_alert_high_field.getText().toString())-1 >= Integer.parseInt(hr_alert_low_field.getText().toString()))
                    temp_alert_high_field.setText(((int) (Float.parseFloat(temp_alert_high_field.getText().toString()) * 10) + 1) / 10.0 + "");
                }

            });

            temp_alert_high_field_down.setOnClickListener(new Button.OnClickListener() {//��������
                public void onClick(View v) {
                    if (((int) (Float.parseFloat(temp_alert_high_field.getText().toString()) * 10) - 1) / 10.0 >= ((int) (Float.parseFloat(temp_alert_low_field.getText().toString()) * 10)) / 10.0)
                        temp_alert_high_field.setText(((int) (Float.parseFloat(temp_alert_high_field.getText().toString()) * 10) - 1) / 10.0 + "");
                }

            });

            temp_alert_low_field_up.setOnClickListener(new Button.OnClickListener() {//��������
                public void onClick(View v) {
                    if (((int) (Float.parseFloat(temp_alert_low_field.getText().toString()) * 10) + 1) / 10.0 <= ((int) (Float.parseFloat(temp_alert_high_field.getText().toString()) * 10) + 1) / 10.0)
                        temp_alert_low_field.setText(((int) (Float.parseFloat(temp_alert_low_field.getText().toString()) * 10) + 1) / 10.0 + "");
                }

            });

            temp_alert_low_field_down.setOnClickListener(new Button.OnClickListener() {//��������
                public void onClick(View v) {
                    if (((int) (Float.parseFloat(temp_alert_low_field.getText().toString()) * 10) - 1) / 10.0 >= 0)
                        temp_alert_low_field.setText(((int) (Float.parseFloat(temp_alert_low_field.getText().toString()) * 10) - 1) / 10.0 + "");
                }

            });


            selfdialog = ad.create();
            selfdialog.setButton("ȡ��", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    selfdialog.cancel();
                }
            });
            selfdialog.setButton2("����", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (temp_alert_switch_field.isChecked())
                        setParamSetting("temp_alert_switch", "on");
                    else
                        setParamSetting("temp_alert_switch", "off");
                    setParamSetting("temp_alert_high", temp_alert_high_field.getText().toString());
                    setParamSetting("temp_alert_low", temp_alert_low_field.getText().toString());

                    getParamSettings();
                    selfdialog.cancel();
                }
            });
            selfdialog.show();
        }


        if (item.getItemId() == 20) {
            //����view�ӵ�ǰactivity��ȡloginactivity
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.leak_detect, null);


            final TextView thre = (TextView) view.findViewById(R.id.thre);

            final Button thre_up = (Button) view.findViewById(R.id.up);
            final Button thre_down = (Button) view.findViewById(R.id.down);

            final Button start_detect = (Button) view.findViewById(R.id.start_detect);

            AlertDialog.Builder ad = new AlertDialog.Builder(MonitorscreenActivity.this);
            ad.setView(view);
            ad.setTitle("©�����");


            thre_up.setOnClickListener(new Button.OnClickListener() {//��������
                public void onClick(View v) {
                    thre.setText(Integer.parseInt(thre.getText().toString()) + 10 + "");
                }

            });

            thre_down.setOnClickListener(new Button.OnClickListener() {//��������
                public void onClick(View v) {
                    thre.setText(Integer.parseInt(thre.getText().toString()) - 10 + "");
                }

            });

            start_detect.setOnClickListener(new Button.OnClickListener() {//��������
                public void onClick(View v) {
                    ct.sendCmd(pp.bp_leak_detect(Integer.toHexString(Integer.parseInt(thre.getText().toString()) / 2)));

                }

            });


            selfdialog = ad.create();
            selfdialog.setButton("ȡ��", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    ct.sendCmd(pp.bp_leak_detect("00"));
                    selfdialog.cancel();
                }
            });
            selfdialog.setButton2("ȷ��", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ct.sendCmd(pp.bp_leak_detect("00"));
                    selfdialog.cancel();
                }
            });
            selfdialog.show();
        }

        if (item.getItemId() == 21) {
            //����view�ӵ�ǰactivity��ȡloginactivity


            AlertDialog.Builder ad = new AlertDialog.Builder(MonitorscreenActivity.this);
            ad.setView(bp_stat_view);
            ad.setTitle("��̬ѹ��У׼");


            selfdialog = ad.create();
            ct.sendCmd(pp.bp_static_calib(true));
            selfdialog.setButton("ȡ��", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    ct.sendCmd(pp.bp_static_calib(false));
                    selfdialog.cancel();
                }
            });
            selfdialog.setButton2("ȷ��", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ct.sendCmd(pp.bp_static_calib(false));
                    selfdialog.cancel();
                }
            });
            selfdialog.show();

        }

        if (item.getItemId() == 23) {
            //����view�ӵ�ǰactivity��ȡloginactivity
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.bp_param_cali, null);


            AlertDialog.Builder ad = new AlertDialog.Builder(MonitorscreenActivity.this);
            ad.setView(view);
            ad.setTitle("Ѫѹ����У׼");


            selfdialog = ad.create();
            ct.sendCmd(pp.bp_static_calib(true));
            selfdialog.setButton("ȡ��", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    ct.sendCmd(pp.bp_static_calib(false));
                    selfdialog.cancel();
                }
            });
            selfdialog.setButton2("ȷ��", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ct.sendCmd(pp.bp_static_calib(false));
                    selfdialog.cancel();
                }
            });
            selfdialog.show();

        }

        if (item.getItemId() == 24) {
            //����view�ӵ�ǰactivity��ȡloginactivity
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.temp_cali, null);


            AlertDialog.Builder ad = new AlertDialog.Builder(MonitorscreenActivity.this);
            ad.setView(view);
            ad.setTitle("���²���У׼");

            final TextView thre = (TextView) view.findViewById(R.id.thre);

            final Button thre_up = (Button) view.findViewById(R.id.up);
            final Button thre_down = (Button) view.findViewById(R.id.down);

            thre_up.setOnClickListener(new Button.OnClickListener() {//��������
                public void onClick(View v) {
                    thre.setText(((int) (Float.parseFloat(thre.getText().toString()) * 10) + 1) / 10.0 + "");
                }

            });

            thre_down.setOnClickListener(new Button.OnClickListener() {//��������
                public void onClick(View v) {
                    thre.setText(((int) (Float.parseFloat(thre.getText().toString()) * 10) - 1) / 10.0 + "");
                }

            });

            selfdialog = ad.create();

            selfdialog.setButton("ȡ��", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {

                    selfdialog.cancel();
                }
            });
            selfdialog.setButton2("ȷ��", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    selfdialog.cancel();
                }
            });
            selfdialog.show();

        }

        if (item.getItemId() == 26) {
            //����view�ӵ�ǰactivity��ȡloginactivity
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.volum_setting, null);
            // Spinner spinner = (Spinner)view.findViewById(R.id.xdlb);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MonitorscreenActivity.this, android.R.layout.simple_spinner_item, new String[]{"����ģʽ", "�໤ģʽ", "���ģʽ"});
            final SeekBar sys_volume_field = (SeekBar) view.findViewById(R.id.seekBar1);
            //spinner.setAdapter(adapter);
            AlertDialog.Builder ad = new AlertDialog.Builder(MonitorscreenActivity.this);
            ad.setView(view);
            ad.setTitle("������������");
            //Get system voluem and set trackbar position
            final AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            sys_volume_field.setMax(maxVolume);

            sys_volume_field.setProgress(currentVolume);


            sys_volume_field.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    // TODO Auto-generated method stub
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                    soundPool.play(loadId, 1, 1, 1, 0, 1f);

                }
            });

            selfdialog = ad.create();
            selfdialog.setButton("ȡ��", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    selfdialog.cancel();
                }
            });
            selfdialog.setButton2("����", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, sys_volume_field.getProgress(), 0);
                    setParamSetting("sys_volume", sys_volume_field.getProgress() + "");


                    getParamSettings();
                    //Alert Sound


                    selfdialog.cancel();
                }
            });
            selfdialog.show();
        }

        if (item.getItemId() == 30) {
            //����view�ӵ�ǰactivity��ȡloginactivity
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.hr_filter_setting, null);
            // Spinner spinner = (Spinner)view.findViewById(R.id.xdlb);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MonitorscreenActivity.this, android.R.layout.simple_spinner_item, new String[]{"����ģʽ", "�໤ģʽ", "���ģʽ"});
            //spinner.setAdapter(adapter);
            final RadioGroup hr_filter_setting_field = (RadioGroup) view.findViewById(R.id.radioGroup1);
            AlertDialog.Builder ad = new AlertDialog.Builder(MonitorscreenActivity.this);
            ad.setView(view);
            ad.setTitle("�ĵ��˲�ģʽ����");
            if (!ecg_filter_mode.equals(""))
                if (Integer.parseInt(ecg_filter_mode) >= 0)
                    ((RadioButton) (hr_filter_setting_field.getChildAt(Integer.parseInt(ecg_filter_mode)))).setChecked(true);

            selfdialog = ad.create();
            selfdialog.setButton("ȡ��", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    selfdialog.cancel();
                }
            });
            selfdialog.setButton2("����", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for (int i = 0; i < hr_filter_setting_field.getChildCount(); i++)
                        if (((RadioButton) hr_filter_setting_field.getChildAt(i)).isChecked()) {
                            switch (i) {
                                case 0:
                                    ct.sendCmd(pp.h_filter_control(1));
                                    break;
                                case 1:
                                    ct.sendCmd(pp.h_filter_control(2));
                                    break;
                                case 2:
                                    ct.sendCmd(pp.h_filter_control(3));
                                    break;

                            }
                            setParamSetting("ecg_filter_mode", i + "");
                            break;
                        }

                    getParamSettings();
                    selfdialog.cancel();
                }
            });
            selfdialog.show();
        }

        if (item.getItemId() == 31) {
            //����view�ӵ�ǰactivity��ȡloginactivity
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.hr_mag_setting, null);
            // Spinner spinner = (Spinner)view.findViewById(R.id.xdlb);
            final RadioGroup ecg_mag_setting_field = (RadioGroup) view.findViewById(R.id.radioGroup1);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MonitorscreenActivity.this, android.R.layout.simple_spinner_item, new String[]{"����ģʽ", "�໤ģʽ", "���ģʽ"});
            //spinner.setAdapter(adapter);
            AlertDialog.Builder ad = new AlertDialog.Builder(MonitorscreenActivity.this);
            ad.setView(view);
            ad.setTitle("�ĵ粨����������");

            if (!ecg_mag.equals(""))
                if (Integer.parseInt(ecg_mag) >= 0)
                    ((RadioButton) (ecg_mag_setting_field.getChildAt(Integer.parseInt(ecg_mag)))).setChecked(true);

            //ecg_mag_setting_field.setId(Integer.parseInt(ecg_mag));
            selfdialog = ad.create();
            selfdialog.setButton("ȡ��", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    selfdialog.cancel();
                }
            });
            selfdialog.setButton2("����", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    for (int i = 0; i < ecg_mag_setting_field.getChildCount(); i++)
                        if (((RadioButton) ecg_mag_setting_field.getChildAt(i)).isChecked()) {
                            switch (i) {
                                case 0:
                                    ct.sendCmd(pp.h_mag_control(1));
                                    break;
                                case 1:
                                    ct.sendCmd(pp.h_mag_control(2));
                                    break;
                                case 2:
                                    ct.sendCmd(pp.h_mag_control(3));
                                    break;
                                case 3:
                                    ct.sendCmd(pp.h_mag_control(4));
                                    break;

                            }
                            setParamSetting("ecg_mag", i + "");
                            break;
                        }

                    getParamSettings();
                    selfdialog.cancel();
                }
            });
            selfdialog.show();
        }

        if (item.getItemId() == 32) {
            //����view�ӵ�ǰactivity��ȡloginactivity
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.resp_mag_setting, null);
            // Spinner spinner = (Spinner)view.findViewById(R.id.xdlb);
            final RadioGroup resp_mag_setting_field = (RadioGroup) view.findViewById(R.id.radioGroup1);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MonitorscreenActivity.this, android.R.layout.simple_spinner_item, new String[]{"����ģʽ", "�໤ģʽ", "���ģʽ"});

            //spinner.setAdapter(adapter);
            AlertDialog.Builder ad = new AlertDialog.Builder(MonitorscreenActivity.this);
            ad.setView(view);
            ad.setTitle("����������������");

            if (!resp_curve_mag.equals(""))
                if (Integer.parseInt(resp_curve_mag) >= 0)
                    ((RadioButton) (resp_mag_setting_field.getChildAt(Integer.parseInt(resp_curve_mag)))).setChecked(true);

            //resp_mag_setting_field.setId(Integer.parseInt(resp_curve_mag));
            selfdialog = ad.create();
            selfdialog.setButton("ȡ��", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    selfdialog.cancel();
                }
            });
            selfdialog.setButton2("����", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //setParamSetting("resp_curve_mag",resp_mag_setting_field.getCheckedRadioButtonId()+"");
                    for (int i = 0; i < resp_mag_setting_field.getChildCount(); i++)
                        if (((RadioButton) resp_mag_setting_field.getChildAt(i)).isChecked()) {
                            switch (i) {
                                case 0:
                                    ct.sendCmd(pp.resp_mag_control(1));
                                    break;
                                case 1:
                                    ct.sendCmd(pp.resp_mag_control(2));
                                    break;
                                case 2:
                                    ct.sendCmd(pp.resp_mag_control(3));
                                    break;
                                case 3:
                                    ct.sendCmd(pp.resp_mag_control(4));
                                    break;

                            }
                            ct.sendCmd(pp.h_out_control(true));
                            ct.sendCmd(pp.h_out_control(false));
                            setParamSetting("resp_curve_mag", i + "");
                            break;
                        }

                    getParamSettings();
                    selfdialog.cancel();
                }
            });
            selfdialog.show();
        }

        if (item.getItemId() == 33) {
            //����view�ӵ�ǰactivity��ȡloginactivity
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.hr_ch_setting, null);
            // Spinner spinner = (Spinner)view.findViewById(R.id.xdlb);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MonitorscreenActivity.this, android.R.layout.simple_spinner_item, new String[]{"����ģʽ", "�໤ģʽ", "���ģʽ"});
            final RadioGroup hr_channel_setting_field = (RadioGroup) view.findViewById(R.id.radioGroup1);
            //spinner.setAdapter(adapter);
            AlertDialog.Builder ad = new AlertDialog.Builder(MonitorscreenActivity.this);
            ad.setView(view);
            ad.setTitle("�ĵ絼���л�����");

            if (!ecg_channel.equals(""))
                if (Integer.parseInt(ecg_channel) >= 0)
                    ((RadioButton) (hr_channel_setting_field.getChildAt(Integer.parseInt(ecg_channel)))).setChecked(true);

            //hr_channel_setting_field.setId(Integer.parseInt(ecg_channel));
            selfdialog = ad.create();
            selfdialog.setButton("ȡ��", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    selfdialog.cancel();
                }
            });
            selfdialog.setButton2("����", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //setParamSetting("ecg_channel",hr_channel_setting_field.getCheckedRadioButtonId()+"");
                    for (int i = 0; i < hr_channel_setting_field.getChildCount(); i++)
                        if (((RadioButton) hr_channel_setting_field.getChildAt(i)).isChecked()) {
                            switch (i) {
                                case 0:
                                    ct.sendCmd(pp.h_3_5_switch_control(1));
                                    break;
                                case 1:
                                    ct.sendCmd(pp.h_3_5_switch_control(2));
                                    break;
                                case 2:
                                    ct.sendCmd(pp.h_3_5_switch_control(3));
                                    break;
                                case 3:
                                    ct.sendCmd(pp.h_3_5_switch_control(4));
                                    break;

                            }
                            setParamSetting("ecg_channel", i + "");
                            break;
                        }

                    getParamSettings();
                    selfdialog.cancel();
                }
            });
            selfdialog.show();
        }


        if (item.getItemId() == 40) {
            //����view�ӵ�ǰactivity��ȡloginactivity
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.bp_auto_setting, null);
            // Spinner spinner = (Spinner)view.findViewById(R.id.xdlb);
            final ToggleButton bp_auto_switch_field = (ToggleButton) view.findViewById(R.id.toggleButton1);
            final RadioGroup bp_auto_inter_field = (RadioGroup) view.findViewById(R.id.radioGroup1);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MonitorscreenActivity.this, android.R.layout.simple_spinner_item, new String[]{"����ģʽ", "�໤ģʽ", "���ģʽ"});
            //spinner.setAdapter(adapter);
            AlertDialog.Builder ad = new AlertDialog.Builder(MonitorscreenActivity.this);
            ad.setView(view);
            ad.setTitle("Ѫѹ�Զ���������");

            if (bp_auto_switch.equals("on"))
                bp_auto_switch_field.setChecked(true);
            else
                bp_auto_switch_field.setChecked(false);
            //bp_auto_inter_field.setId(Integer.parseInt(bp_auto_inter));

            if (!bp_auto_inter.equals(""))
                if (Integer.parseInt(bp_auto_inter) >= 0)
                    ((RadioButton) (bp_auto_inter_field.getChildAt(Integer.parseInt(bp_auto_inter)))).setChecked(true);

            selfdialog = ad.create();
            selfdialog.setButton("ȡ��", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    selfdialog.cancel();
                }
            });
            selfdialog.setButton2("����", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (bp_auto_switch_field.isChecked())
                        setParamSetting("bp_auto_switch", "on");
                    else
                        setParamSetting("bp_auto_switch", "off");

                    //setParamSetting("bp_auto_inter",bp_auto_inter_field.getCheckedRadioButtonId()+"");
                    for (int i = 0; i < bp_auto_inter_field.getChildCount(); i++)
                        if (((RadioButton) bp_auto_inter_field.getChildAt(i)).isChecked()) {

                            setParamSetting("bp_auto_inter", i + "");
                            break;
                        }
                    getParamSettings();
                    selfdialog.cancel();
                }
            });
            selfdialog.show();
        }

        //Bp Prefill Set
        if (item.getItemId() == 41) {
            //����view�ӵ�ǰactivity��ȡloginactivity
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.bp_prefill_setting, null);
            // Spinner spinner = (Spinner)view.findViewById(R.id.xdlb);


            AlertDialog.Builder ad = new AlertDialog.Builder(MonitorscreenActivity.this);
            ad.setView(view);
            ad.setTitle("Ԥ����ѹ������");


            final TextView cr_prefill_param = (TextView) view.findViewById(R.id.cr);
            final TextView et_prefill_param = (TextView) view.findViewById(R.id.et);
            final TextView ye_prefill_param = (TextView) view.findViewById(R.id.ye);


            final Button cr_up = (Button) view.findViewById(R.id.crup);
            final Button cr_down = (Button) view.findViewById(R.id.crdown);

            final Button et_up = (Button) view.findViewById(R.id.etup);
            final Button et_down = (Button) view.findViewById(R.id.etdown);

            final Button ye_up = (Button) view.findViewById(R.id.yeup);
            final Button ye_down = (Button) view.findViewById(R.id.yedown);

            cr_up.setOnClickListener(new Button.OnClickListener() {//��������
                public void onClick(View v) {
                    if (Integer.parseInt(cr_prefill_param.getText().toString()) + 10 <= 300)
                        cr_prefill_param.setText(Integer.parseInt(cr_prefill_param.getText().toString()) + 10 + "");
                }

            });

            cr_down.setOnClickListener(new Button.OnClickListener() {//��������
                public void onClick(View v) {
                    if (Integer.parseInt(cr_prefill_param.getText().toString()) - 10 >= 40)
                        cr_prefill_param.setText(Integer.parseInt(cr_prefill_param.getText().toString()) - 10 + "");
                }

            });


            et_up.setOnClickListener(new Button.OnClickListener() {//��������
                public void onClick(View v) {
                    if (Integer.parseInt(et_prefill_param.getText().toString()) + 10 <= 210)
                        et_prefill_param.setText(Integer.parseInt(et_prefill_param.getText().toString()) + 10 + "");
                }

            });

            et_down.setOnClickListener(new Button.OnClickListener() {//��������
                public void onClick(View v) {
                    if (Integer.parseInt(et_prefill_param.getText().toString()) - 10 >= 40)
                        et_prefill_param.setText(Integer.parseInt(et_prefill_param.getText().toString()) - 10 + "");
                }

            });


            ye_up.setOnClickListener(new Button.OnClickListener() {//��������
                public void onClick(View v) {
                    if (Integer.parseInt(ye_prefill_param.getText().toString()) + 10 <= 140)
                        ye_prefill_param.setText(Integer.parseInt(ye_prefill_param.getText().toString()) + 10 + "");
                }

            });

            ye_down.setOnClickListener(new Button.OnClickListener() {//��������
                public void onClick(View v) {
                    if (Integer.parseInt(ye_prefill_param.getText().toString()) - 10 >= 40)
                        ye_prefill_param.setText(Integer.parseInt(ye_prefill_param.getText().toString()) - 10 + "");
                }

            });


            selfdialog = ad.create();
            selfdialog.setButton("ȡ��", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    selfdialog.cancel();
                }
            });
            selfdialog.setButton2("����", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    getParamSettings();
                    selfdialog.cancel();
                }
            });
            selfdialog.show();
        }
        //Bp Mode Set
        if (item.getItemId() == 43) {
            //����view�ӵ�ǰactivity��ȡloginactivity
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.bp_mode_setting, null);
            // Spinner spinner = (Spinner)view.findViewById(R.id.xdlb);

            final RadioGroup bp_mode_field = (RadioGroup) view.findViewById(R.id.radioGroup1);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MonitorscreenActivity.this, android.R.layout.simple_spinner_item, new String[]{"����ģʽ", "�໤ģʽ", "���ģʽ"});
            //spinner.setAdapter(adapter);
            AlertDialog.Builder ad = new AlertDialog.Builder(MonitorscreenActivity.this);
            ad.setView(view);
            ad.setTitle("Ѫѹ����ģʽ����");


            //bp_auto_inter_field.setId(Integer.parseInt(bp_auto_inter));

            if (!bp_mode.equals(""))
                if (Integer.parseInt(bp_mode) >= 0)
                    ((RadioButton) (bp_mode_field.getChildAt(Integer.parseInt(bp_mode)))).setChecked(true);

            selfdialog = ad.create();
            selfdialog.setButton("ȡ��", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    selfdialog.cancel();
                }
            });
            selfdialog.setButton2("����", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    for (int i = 0; i < bp_mode_field.getChildCount(); i++)
                        if (((RadioButton) bp_mode_field.getChildAt(i)).isChecked()) {
                            switch (i) {
                                case 0:
                                    ct.sendCmd(pp.bp_measure_control(1));
                                    break;
                                case 1:
                                    ct.sendCmd(pp.bp_measure_control(2));
                                    break;
                                case 2:
                                    ct.sendCmd(pp.bp_measure_control(3));
                                    break;


                            }
                            setParamSetting("bp_mode", i + "");
                            break;
                        }
                    getParamSettings();
                    selfdialog.cancel();
                }
            });
            selfdialog.show();
        }
        //Start Bp measure
        if (item.getItemId() == 4) {


        }
        return super.onMenuItemSelected(featureId, item);
    }

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
