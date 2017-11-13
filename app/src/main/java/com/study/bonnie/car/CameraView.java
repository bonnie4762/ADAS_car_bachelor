package com.study.bonnie.car;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.graphics.Matrix;

/**
 * Created by bonnie on 24/03/2017.
 */

public class CameraView extends SurfaceView implements SurfaceHolder.Callback{

    //static List<byte[]> frameList = null;
    private SurfaceHolder holder = null;
    private long frequency = 40;
    private int frameIndex = 0;

    PlayThread thread;
    FrameManager frmanager;


    public CameraView(Context context, AttributeSet attrs) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        frmanager = new FrameManager();
        //frameList = new ArrayList<byte[]>();

    }

    public void drawImg(){
        Canvas canvas = holder.lockCanvas();
        if(canvas!=null) {
            Paint paint = new Paint();
            int width = getWidth();
            int height = getHeight();
            Bitmap image = null;
            if(frmanager.noFrame()){
                image = BitmapFactory.decodeResource(getResources(),R.drawable.black);
                //System.out.println("black");
            }
            else {
                byte[] frame = frmanager.getFrame();
                image = BitmapFactory.decodeByteArray(frame,0,frame.length);
            }
            if(image!=null) {
                image = getReducedBitmap(image, width, height);
                canvas.drawBitmap(image, 0, 0, paint);
            }
            holder.unlockCanvasAndPost(canvas);
        }
    }



    public Bitmap getReducedBitmap(Bitmap bitmap, int width, int height){
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        Float wScale = (float)width/w;
        Float hScale = (float)height/h;
        matrix.postScale(wScale, hScale);
        return Bitmap.createBitmap(bitmap,0,0,w,h,matrix,true);

    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        thread = new PlayThread();
        thread.playing = true;
        thread.start();



    }


    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        thread.playing = false;
        holder.removeCallback(this);

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }



    class PlayThread extends Thread{
        boolean playing;
        boolean visible;
        public PlayThread(){
            this.playing = false;
            this.visible = true;
        }
        public void run(){

            while(playing){
                if(visible) {
                    synchronized (holder) {
                       try {


                            drawImg();
                           thread.sleep(frequency);
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                      }

                    }
                }
                else;
            }

        }
    }

}

