package com.study.bonnie.car;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.view.SurfaceView;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.spec.ECField;
import java.util.concurrent.Exchanger;
import java.io.File;
import android.util.Log;



public class UDPClient extends Service {

    private final IBinder binder = new MyLocalBinder();
    private int serverPort = 3333;
    private final int TIMEOUT = 3000;
    private String IP = "192.168.0.104";
    InetAddress serverAddress;

    DatagramSocket clientSocket;



    public UDPClient(){

        if(ConnectionFragment.getIP()!=null){
            IP = ConnectionFragment.getIP();
        }
        try {

            serverAddress = InetAddress.getByName(IP);
        }catch(Exception e){
            e.printStackTrace();
        }

    }



    public int getSensor1() throws Exception {

        clientSocket = new DatagramSocket();
        clientSocket.setReuseAddress(true);
        clientSocket.setSoTimeout(TIMEOUT);

        String str = "sensor1";
        byte[] request = str.getBytes();
        DatagramPacket packetSend = new DatagramPacket(request, request.length, serverAddress, serverPort);
        try {
            clientSocket.send(packetSend);
        } catch(Exception e){
            e.printStackTrace();
        }
        byte[] buffer = new byte[10];
        DatagramPacket packetReceived = new DatagramPacket(buffer, buffer.length);
        try {
            clientSocket.receive(packetReceived);
        } catch(Exception e){
            e.printStackTrace();
        }
        String str2 = new String(packetReceived.getData());
        int sensorData = Integer.parseInt(str2.trim());
        clientSocket.close();

        return sensorData;


    }

    public int getSensor2() throws Exception{

        clientSocket = new DatagramSocket();
        clientSocket.setReuseAddress(true);

        clientSocket.setSoTimeout(TIMEOUT);
        String str = "sensor2";
        byte[] request = str.getBytes();
        DatagramPacket packetSend = new DatagramPacket(request, request.length, serverAddress, serverPort);
        try {
            clientSocket.send(packetSend);
        } catch(Exception e){
            e.printStackTrace();
        }
        byte[] buffer = new byte[10];
        DatagramPacket packetReceived = new DatagramPacket(buffer, buffer.length);
        try {
            clientSocket.receive(packetReceived);
        } catch(Exception e){
            e.printStackTrace();
        }
        String str2 = new String(packetReceived.getData());
        int sensorData = Integer.parseInt(str2.trim());
        clientSocket.close();

        return sensorData;
    }


    public int getSensor3() throws Exception{

        clientSocket = new DatagramSocket();
        clientSocket.setReuseAddress(true);

        clientSocket.setSoTimeout(TIMEOUT);
        String str = "sensor3";
        byte[] request = str.getBytes();
        DatagramPacket packetSend = new DatagramPacket(request, request.length, serverAddress, serverPort);
        try {
            clientSocket.send(packetSend);
        } catch(Exception e){
            e.printStackTrace();
        }
        byte[] buffer = new byte[10];
        DatagramPacket packetReceived = new DatagramPacket(buffer, buffer.length);
        try {
            clientSocket.receive(packetReceived);
        } catch(Exception e){
            e.printStackTrace();
        }
        String str2 = new String(packetReceived.getData());
        int sensorData = Integer.parseInt(str2.trim());
        clientSocket.close();

        return sensorData;
    }


    public int getSensor4() throws Exception{

        clientSocket = new DatagramSocket();
        clientSocket.setReuseAddress(true);

        clientSocket.setSoTimeout(TIMEOUT);
        String str = "sensor4";
        byte[] request = str.getBytes();
        DatagramPacket packetSend = new DatagramPacket(request, request.length, serverAddress, serverPort);
        try {
            clientSocket.send(packetSend);
        } catch(Exception e){
            e.printStackTrace();
        }
        byte[] buffer = new byte[10];
        DatagramPacket packetReceived = new DatagramPacket(buffer, buffer.length);
        try {
            clientSocket.receive(packetReceived);
        } catch(Exception e){
            e.printStackTrace();
        }
        String str2 = new String(packetReceived.getData());
        int sensorData = Integer.parseInt(str2.trim());
        clientSocket.close();

        return sensorData;
    }



    public void sendSpeed(int speed) throws Exception {



                    clientSocket = new DatagramSocket();
        clientSocket.setReuseAddress(true);

        clientSocket.setSoTimeout(TIMEOUT);

                String str = "control speed";
                byte[] request = str.getBytes();
                DatagramPacket packetSend = new DatagramPacket(request, request.length, serverAddress, serverPort);
                try {
                    clientSocket.send(packetSend);
                } catch(Exception e){
                    e.printStackTrace();
                }
                byte[] newSpeed = String.valueOf(speed).getBytes();
                packetSend = new DatagramPacket(newSpeed, newSpeed.length, serverAddress, serverPort);
                try {
                    clientSocket.send(packetSend);
                } catch(Exception e){
                    e.printStackTrace();
                }
                clientSocket.close();





    }


    public void sendAngle(int angle) throws Exception{



                    clientSocket = new DatagramSocket();
        clientSocket.setReuseAddress(true);

        clientSocket.setSoTimeout(TIMEOUT);

                String str = "control angle";
                byte[] request = str.getBytes();
                DatagramPacket packetSend = new DatagramPacket(request, request.length, serverAddress, serverPort);
                try {
                    clientSocket.send(packetSend);
                } catch(Exception e){
                    e.printStackTrace();
                }
                byte[] newAngle = String.valueOf(angle).getBytes();
                packetSend = new DatagramPacket(newAngle,newAngle.length, serverAddress, serverPort);
                try {
                    clientSocket.send(packetSend);
                } catch(Exception e){
                    e.printStackTrace();
                }
                clientSocket.close();



    }




    @Override
    public IBinder onBind(Intent intent) {
        return binder;

    }

    public class MyLocalBinder extends Binder{

        UDPClient getService(){
            return UDPClient.this;
        }
    }

}

