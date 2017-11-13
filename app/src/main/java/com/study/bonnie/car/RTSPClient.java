package com.study.bonnie.car;
import android.view.View;
import android.widget.Toast;

import java.net.*;
import java.util.StringTokenizer;
import java.util.Timer;
import java.io.*;
import java.util.TimerTask;

import static android.view.View.*;

/**
 * Created by bonnie on 12/04/2017.
 */

public class RTSPClient{

    private static String IP = "130.229.164.206";

    private static int RTSP_server_port = 3333;
    final static String VideoFileName = "movie.Mjpeg";

    //RTP variables:
    //----------------
    DatagramPacket rcvdp; //UDP packet received from the server
    DatagramSocket RTPsocket; //socket to be used to send and receive UDP packets
    final static int RTP_RCV_PORT = 2222; //port where the client will receive the RTP packets
    InetAddress ServerIPAddr;

    Timer timer; //timer used to receive data from the UDP socket
    TimerTask task;
    byte[] buf; //buffer used to store data received from the server

    //RTSP variables
    //----------------
    //rtsp states
    final static int INIT = 0;
    final static int READY = 1;
    final static int PLAYING = 2;
    static int state; //RTSP state == INIT or READY or PLAYING
    Socket RTSPsocket; //socket used to send/receive RTSP messages
    //input and output stream filters
    static BufferedReader RTSPBufferedReader;
    static BufferedWriter RTSPBufferedWriter;
    int RTSPSeqNb = 0; //Sequence number of RTSP messages within the session
    int RTSPid = 0; //ID of the RTSP session (given by the RTSP Server)

    final static String CRLF = "\r\n";

    //Video constants:
    //------------------
    static int MJPEG_TYPE = 26; //RTP payload type for MJPEG video

    static boolean isConnected = false;
    boolean run = false;


    FrameManager frameManager;


    //--------------------------
    //Constructor
    //--------------------------
    public RTSPClient() {


        frameManager = new FrameManager();
        buf = new byte[15000];

        if(ConnectionFragment.getIP()!=null){
            IP = ConnectionFragment.getIP();
        }
        if(ConnectionFragment.getPort()!=0){
            RTSP_server_port = ConnectionFragment.getPort();
        }

        //init timer
        //--------------------------
        timer = new Timer();

        task = new TimerTask() {
            @Override
            public void run() {
                if(run) {

                    System.out.println("timer task running");
                    rcvdp = new DatagramPacket(buf, buf.length);
                    try {
                        //receive the DP from the socket:
                        System.out.println("receive");

                        RTPsocket.receive(rcvdp);
                        //create an RTPpacket object from the DP
                        RTPpacket rtp_packet = new RTPpacket(rcvdp.getData(), rcvdp.getLength());

                        //print important header fields of the RTP packet received:
                        System.out.println("Got RTP packet with SeqNum # " + rtp_packet.getsequencenumber() + " TimeStamp " + rtp_packet.gettimestamp() + " ms, of type " + rtp_packet.getpayloadtype());

                        //print header bitstream:
                        rtp_packet.printheader();

                        //get the payload bitstream from the RTPpacket object
                        int payload_length = rtp_packet.getpayload_length();
                        byte[] payload = new byte[payload_length];
                        rtp_packet.getpayload(payload);
                        frameManager.addFrame(payload);
                    } catch (Exception e) {
                        System.out.println("Exception caught: " + e);
                    }
                }
            }
        };
        timer.schedule(task, 0, 20);



        //allocate enough memory for the buffer used to receive data from the server

}


    public void connectServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    ServerIPAddr = InetAddress.getByName(IP);

                    //Establish a TCP connection with the server to exchange RTSP messages
                    //------------------
                    RTSPsocket = new Socket(ServerIPAddr, RTSP_server_port);
                    isConnected = true;
                    System.out.println("connected to RTSP server");

                    //Set input and output stream filters:
                    RTSPBufferedReader = new BufferedReader(new InputStreamReader(RTSPsocket.getInputStream()));
                    RTSPBufferedWriter = new BufferedWriter(new OutputStreamWriter(RTSPsocket.getOutputStream()));
                }catch (Exception e){
                    e.printStackTrace();
                }
                //init RTSP state:
                state = INIT;
            }
        }).start();


    }

    //------------------------------------
    //Handler for buttons
    //------------------------------------

    //.............
    //TO COMPLETE
    //.............


    //Handler for Setup button
    //-----------------------



    public void play(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Setup...");

                //Init non-blocking RTPsocket that will be used to receive data
                try {
                    //construct a new DatagramSocket to receive RTP packets from the server, on port RTP_RCV_PORT
                    //RTPsocket = ...

                    RTPsocket = new DatagramSocket(RTP_RCV_PORT);
                    RTPsocket.setSoTimeout(20);


                    //set TimeOut value of the socket to 5msec.
                    //....


                } catch (SocketException se) {
                    System.out.println("Socket exception: " + se);
                    System.exit(0);
                }

                //init RTSP sequence number
                RTSPSeqNb = 1;

                //Send SETUP message to the server
                send_RTSP_request("SETUP");

                //Wait for the response
                if (parse_server_response() != 200)
                    System.out.println("Invalid Server Response");
                else {
                    //change RTSP state and print new state
                    //state = ....
                    //System.out.println("New RTSP state: ....");
                    state = READY;
                    System.out.println("New RTSP state: READY");

                }
                    System.out.println("start play");

                    if (state == READY) {
                        //increase RTSP sequence number
                        //.....
                        RTSPSeqNb++;


                        //Send PLAY message to the server
                        send_RTSP_request("PLAY");

                        //Wait for the response
                        if (parse_server_response() != 200)
                            System.out.println("Invalid Server Response");
                        else {
                            //change RTSP state and print out new state
                            //.....
                            // System.out.println("New RTSP state: ...")

                            state = PLAYING;
                            System.out.println("New RTSP state: PLAYING");


                            //start the timer
                            run = true;
                        }
                    }

                }
        }).start();






    }

    public void pause(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Pause Button pressed !");

                if (state == PLAYING)
                {
                    //increase RTSP sequence number
                    //........
                    RTSPSeqNb++;

                    //Send PAUSE message to the server
                    send_RTSP_request("PAUSE");

                    //Wait for the response
                    if (parse_server_response() != 200)
                        System.out.println("Invalid Server Response");
                    else
                    {
                        //change RTSP state and print out new state
                        //........
                        //System.out.println("New RTSP state: ...");
                        state = READY;
                        System.out.println("New RTSP state: READY");

                        //clear frames
                        frameManager.clearFrames();

                        //stop the timertask
                        run = false;

                       RTPsocket.close();
                    }
                }

            }
        }).start();



    }



    //------------------------------------
    //Parse Server Response
    //------------------------------------
    private int parse_server_response()
    {
        int reply_code = 0;

        try{
            //parse status line and extract the reply_code:
            String StatusLine = RTSPBufferedReader.readLine();
            System.out.println("RTSP Client - Received from Server:");
            System.out.println(StatusLine);

            StringTokenizer tokens = new StringTokenizer(StatusLine);
            tokens.nextToken(); //skip over the RTSP version
            reply_code = Integer.parseInt(tokens.nextToken());

            //if reply code is OK get and print the 2 other lines
            if (reply_code == 200)
            {
                String SeqNumLine = RTSPBufferedReader.readLine();
                System.out.println(SeqNumLine);

                String SessionLine = RTSPBufferedReader.readLine();
                System.out.println(SessionLine);

                //if state == INIT gets the Session Id from the SessionLine
                tokens = new StringTokenizer(SessionLine);
                tokens.nextToken(); //skip over the Session:
                RTSPid = Integer.parseInt(tokens.nextToken());
            }
        }
        catch(Exception ex)
        {
            System.out.println("parse Exception caught: "+ex);
            System.exit(0);
        }

        return(reply_code);
    }

    //------------------------------------
    //Send RTSP Request
    //------------------------------------

    //.............
    //TO COMPLETE
    //.............

    private void send_RTSP_request(String request_type)
    {

                try{
                    //Use the RTSPBufferedWriter to write to the RTSP socket

                    //write the request line:
                    //RTSPBufferedWriter.write(...);

                    //write the CSeq line:
                    //......

                    //check if request_type is equal to "SETUP" and in this case write the Transport: line advertising to the server the port used to receive the RTP packets RTP_RCV_PORT
                    //if ....
                    //otherwise, write the Session line from the RTSPid field
                    //else ....

                    RTSPBufferedWriter.write(request_type+" "+VideoFileName+" RTSP/1.0"+CRLF);

                    RTSPBufferedWriter.write("CSeq: "+RTSPSeqNb+CRLF);

                    if(request_type.equals("SETUP")){
                        RTSPBufferedWriter.write("Transport: RTP/UDP; client_port= "+RTP_RCV_PORT+CRLF);

                    }
                    else {
                        RTSPBufferedWriter.write("Session: " + RTSPid + CRLF);

                    }



                    RTSPBufferedWriter.flush();
                }
                catch(Exception ex)
                {
                    System.out.println("send Exception caught: "+ex);
                    System.exit(0);
                }

            }




        }//end of Class Client