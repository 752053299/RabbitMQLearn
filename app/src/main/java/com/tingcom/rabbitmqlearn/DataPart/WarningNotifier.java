package com.tingcom.rabbitmqlearn.DataPart;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.tingcom.rabbitmqlearn.DataPart.DataRepository.RABBITMQ_CONNECT_BREAK;
import static com.tingcom.rabbitmqlearn.DataPart.DataRepository.RABBITMQ_GET_MSG;

public class WarningNotifier implements Runnable {

    private static final String TAG = "DataRepository";
    private boolean mHasQuit = false;
    private Handler mResponsetHandler;
    private ConnectionFactory mFactory;
    private Connection mConnection;
    private Channel mChannel;

    public WarningNotifier(ConnectionFactory factory,Handler handler) {
        mFactory = factory;
        mResponsetHandler = handler;
    }

    @Override
    public void run() {
            try {
                mConnection = mFactory.newConnection();
                mChannel = mConnection.createChannel();
                mChannel.basicQos(1);
               // channel.exchangeDeclare("1032","fanout");

                AMQP.Queue.DeclareOk q = mChannel.queueDeclare();
                mChannel.queueBind(q.getQueue(),"1032","");
                Log.i(TAG," [*] Waiting for messages.current Thread:"  + Thread.currentThread().getId());

                Consumer consumer = new DefaultConsumer(mChannel){
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        String warning = new String(body,"UTF-8");
                        Log.d(TAG, "[r]" + warning);
                        Log.i(TAG, "收到消息，当前线程：" + Thread.currentThread().getId());
                        Message msg = mResponsetHandler.obtainMessage();
                        Bundle bundle = new Bundle();
                        bundle.putString("warning",warning);
                        msg.setData(bundle);
                        msg.what = RABBITMQ_GET_MSG;
                        mResponsetHandler.sendMessage(msg);
                    }
                };
                mChannel.basicConsume(q.getQueue(),true,consumer);

            } catch (Exception e1) {
                Log.d(TAG, "Connection broken: " + e1.getClass().getName());
                try {
                    Thread.sleep(5000); //sleep and then try again
                    mResponsetHandler.sendEmptyMessage(RABBITMQ_CONNECT_BREAK);
                } catch (InterruptedException e) {
                    mResponsetHandler.sendEmptyMessage(RABBITMQ_CONNECT_BREAK);
                    e.printStackTrace();
                }
            }

    }

    public void clearConnection(){
        if (mChannel!= null && mConnection!= null){
            try {
                mChannel.close();
                mConnection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mChannel =null;
            mConnection = null;
        }
    }
}
