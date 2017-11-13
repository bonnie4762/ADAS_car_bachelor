package com.study.bonnie.car;

/**
 * Created by bonnie on 28/03/2017.
 */
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class FrameManager {
    static Queue<byte[]> frames;
    static int size;
    static int index = 0;
    static int maxSize = 100;

    FrameManager(){
        frames = new LinkedList<byte[]>();
    }

    public void addFrame(byte[] newFrame){
        /*
        if(size >= maxSize) {
            frames.set(index, newFrame);
        }
        else
            frames.add(newFrame);
        index++;
        index = (index > maxSize-1)?0:index;
        this.size = frames.size();
        */
        frames.offer(newFrame);
    }

    public int getSize(){
        return size;
    }

    public byte[] getFrame(){
        return frames.poll();
    }
    public boolean noFrame(){
        return frames.isEmpty();
    }

    public void clearFrames(){
        frames.clear();
    }

}
