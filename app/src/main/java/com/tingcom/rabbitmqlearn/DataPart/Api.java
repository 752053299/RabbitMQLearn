package com.tingcom.rabbitmqlearn.DataPart;

import android.os.Handler;

import java.util.List;

public class Api {

    private Handler msgHandler;

    public void getWarnings(){

    }


    private class askRunnable implements Runnable{

        @Override
        public void run() {

        }
    }

    interface callback{
        void onSuccess(List<Warning> warnings);
        void onFailed(String msg);
    }
}
