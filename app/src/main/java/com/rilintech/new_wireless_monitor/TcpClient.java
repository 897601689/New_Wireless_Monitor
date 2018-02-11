package com.rilintech.new_wireless_monitor;

import android.util.Log;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;;
public class TcpClient {
 
    private String serverMessage;//��������Ϣ
    public String SERVERIP = "10.10.100.254"; //��������IP��ַ
    public  int SERVERPORT = 4499;//�������˿�
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;
    private int timeout = 5000;	//��ʱ
    private InetSocketAddress  isa;
    private byte[] origin_buffer;
    private List<Byte> total_buffer;
    
    
    private boolean total_buffer_lock;
    
    private boolean read_params_lock;
    
    
    public HashMap<String , List> data_map; 
    
    private List<Double> h_curve_v1;
    private List<Double> h_curve_v2;
    private List<Double> h_curve_v3;
    private List<Double> h_curve_avr;
    private List<Double> h_curve_avl;
    private List<Double> h_curve_avf;
    private List<Double> h_curve_v;
    
    
    private List<String> h_params;
    
    private List<String> bp_params;
    private List<String> spo2_params;
    
    private List<Double> temp_params;
    
    private List<Integer> spo2_curve;
    
	private List<Integer> resp_curve;
    
    PrintWriter out;
    BufferedReader in;
 
    /**
     *  ��Ĺ��캯����OnMessagedReceived�����ӷ��������յ�����Ϣ
     */
	public TcpClient() {
        
        origin_buffer = new byte[1024];
        total_buffer = new ArrayList<Byte>();
        data_map = new HashMap<String ,List >(); 
        
        this.h_curve_v1 = new ArrayList<Double>();
        this.h_curve_v2 = new ArrayList<Double>();
        this.h_curve_v3 = new ArrayList<Double>();
        this.h_curve_avr = new ArrayList<Double>();
        this.h_curve_avl = new ArrayList<Double>();
        this.h_curve_avf = new ArrayList<Double>();
        this.h_curve_v = new ArrayList<Double>();
        
        this.h_params = new ArrayList<String>();
        
        this.bp_params = new ArrayList<String>();
        
        this.spo2_params = new ArrayList<String>();
        
        this.temp_params = new ArrayList<Double>();
        
        this.spo2_curve = new ArrayList<Integer>();
        this.resp_curve = new ArrayList<Integer>();
    }
 
    
	/**
     * Sends the message entered by client to the server �ͻ����������Ϣ���͵�������
     * @param message text entered by client ��Ϣ�ı������
     */
    public void sendMessage(String message){
        if (out != null && !out.checkError()) {
            out.println(message);
            out.flush();
        }
    }
 
    public void stopClient(){
        mRun = false;
    }
 
    public void run() {
 
        mRun = true;
 
        try {
            //here you must put your computer's IP address.
            //InetAddress serverAddr = InetAddress.getByName(SERVERIP);
 
            Log.e("TCP Client", "C: ����...");
 
            //create a socket to make the connection with the server
            //����һ���׽������������������
            Socket socket = new Socket();   
            isa = new InetSocketAddress(SERVERIP, SERVERPORT);     
            socket.connect(isa, timeout);
            //socket = new Socket(serverAddr, SERVERPORT);
 
            try {
 
                //send the message to the server
            	//�������������Ϣ
            	
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                
                Log.e("TCP Client", "C: Sent.");
 
                Log.e("TCP Client", "C: Done.");
 
                //receive the message which the server sends back
                //���շ��������ͻ�������Ϣ
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
 
                //in this while the client listens for the messages sent by the server
                //������ͻ��˼������������͵���Ϣ
               /* while (mRun) {
                    serverMessage = in.readLine();
 
                    if (serverMessage != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(serverMessage);
                    }
                    serverMessage = null;
 
                }*/
 
                Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + serverMessage + "'");
 
            } catch (Exception e) {
 
                Log.e("TCP", "S: Error", e);
 
            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
            	//����ر��׽��֡��ǲ������������ӵ�����׽��ֺ�ر�,����ζ�ű��봴��һ���µ��׽���ʵ����
                socket.close();
            }
 
        } catch (Exception e) {
 
            Log.e("TCP", "C: ����", e);
 
        }
 
    }
 
    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    //�����ӿڡ�messageReceived(String message)����MyActivity�����ʵ�ֺ�̨����
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
}



