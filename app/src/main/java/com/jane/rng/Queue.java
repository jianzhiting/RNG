package com.jane.rng;

import android.widget.RelativeLayout;

public class Queue {
    RelativeLayout[] v;
    int i, j;

    public Queue(){
        i = -1;
        j = -1;
        v = new RelativeLayout[5];
    }
    void EnQueue(RelativeLayout view){
        i = (i+1)%5;
        v[i] = view;
    }
    RelativeLayout DeQueue(){
        j = (j+1)%5;
        return v[j];
    }
}
