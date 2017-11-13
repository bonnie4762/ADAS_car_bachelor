package com.study.bonnie.car;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MonitorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MonitorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MonitorFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageView leftTyre;
    ImageView rightTyre;
    ImageView window;
    ImageView door;
    TextView sensorInfo;

    UDPClient sensorService;
    boolean isBound = false;

    private static Handler handler;


    private OnFragmentInteractionListener mListener;

    public MonitorFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MonitorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MonitorFragment newInstance(String param1, String param2) {
        MonitorFragment fragment = new MonitorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        handler = new Handler();
        Intent intent = new Intent(getActivity(), UDPClient.class);
        getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        isBound = true;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_monitor, container, false);
        leftTyre = (ImageView)view.findViewById(R.id.leftTyre);
        rightTyre = (ImageView)view.findViewById(R.id.rightTyre);
        window = (ImageView)view.findViewById(R.id.window);
        door = (ImageView)view.findViewById(R.id.door);
        sensorInfo = (TextView)view.findViewById(R.id.sensorTextView);



        leftTyre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final int sensorData = sensorService.getSensor1();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    sensorInfo.setText("sensor1:  "+sensorData+"\n");
                                }
                            });
                        }catch(Exception e){
                            e.printStackTrace();

                        }
                    }
                }).start();





            }
        });
        rightTyre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final int sensorData = sensorService.getSensor2();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    sensorInfo.setText("sensor2:  " + sensorData + "\n");
                                }
                            });
                        }catch(Exception e){
                            e.printStackTrace();

                        }
                    }
                }).start();





            }
        });
        window.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final int sensorData = sensorService.getSensor3();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    sensorInfo.setText("sensor3:  " + sensorData + "\n");
                                }
                            });
                        }catch(Exception e){
                            e.printStackTrace();

                        }
                    }
                }).start();

            }
        });
        door.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final int sensorData = sensorService.getSensor4();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    sensorInfo.setText("sensor4:  " + sensorData + "\n");
                                }
                            });
                        }catch(Exception e){
                            e.printStackTrace();

                        }
                    }
                }).start();
            }
        });


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStop(){
        if(isBound) {
            getActivity().unbindService(serviceConnection);
            isBound = false;
        }
        super.onStop();
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }





    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            UDPClient.MyLocalBinder binder = (UDPClient.MyLocalBinder)iBinder;
            sensorService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

            isBound = false;

        }
    };
}
