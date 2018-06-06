package com.tingcom.rabbitmqlearn.DataPart;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.ExceptionHandler;
import com.rabbitmq.client.TopologyRecoveryException;

import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

public class DataRepository implements RepositoryInterface {
    private static final String TAG = "DataRepository";
    private volatile List<Warning> allWarnings = new LinkedList<>();
    private ConnectionFactory factory;
    public static final int RABBITMQ_GET_MSG = 0x01;
    public static final int RABBITMQ_CONNECT_BREAK = 0x02;
    private Thread mNotifierThread;
    private WarningNotifier warningNotifier;
    public boolean isQuit = false;
    private newWarning mNewWarning;
    private DataRepository(){

    }

    private static class DataRepositoryHolder{
        private final static DataRepository instance = new DataRepository();
    }

    public static DataRepository getInstance(){
        return DataRepositoryHolder.instance;
    }

    public void setNewWarningListener(newWarning callback){
        this.mNewWarning = callback;
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){

                case RABBITMQ_GET_MSG:
                    Log.i(TAG, "收到一条报警：" + msg.getData().getString("warning"));
                    Log.i(TAG, "收到报警后的回调线程：" + Thread.currentThread().getId());
                    Warning warning = new Warning("新报警",msg.getData().getString("warning"));
                    allWarnings.add(warning);
                    mNewWarning.warningComing();
                    break;
                case RABBITMQ_CONNECT_BREAK:
                    Log.i(TAG, "连接断开了！");
                    if (!isQuit){
                        stopReceiveWarning();
                        startReceiveWarning();
                    }
                    break;
            }
        }
    };
    public void init(){
        refreshWarnings(new GetWarningsCallBack() {
            @Override
            public void querySuccess(List<Warning> warnings) {
                addWarnings(warnings);
            }

            @Override
            public void warningDataNotAvalible() {

            }
        });
        initRabbitMq();
    }

    private void initRabbitMq(){
        factory = new ConnectionFactory();
        try {
            factory.setUri("amqp://thingcom:106ling106@121.40.142.221:5672");
        } catch (Exception e) {
            e.printStackTrace();
        }
        factory.setUsername("thingcom");
        factory.setPassword("106ling106");
        factory.setExceptionHandler(mExceptionHandler);


        Log.i(TAG, "initRabbitMq: 主线程：" + Thread.currentThread().getId());
    }

    public void startReceiveWarning(){
        warningNotifier = new WarningNotifier(factory,mHandler);
        mNotifierThread = new Thread(warningNotifier);
        mNotifierThread.start();
    }

    public void stopReceiveWarning(){
        if (mNotifierThread != null){
            warningNotifier.clearConnection();
            mNotifierThread.interrupt();
            mNotifierThread = null;
        }
    }

    private synchronized void addWarnings(List<Warning> warnings){
        allWarnings.clear();
        allWarnings.addAll(warnings);
    }

    public List<Warning> getAllWarnings(){
        return this.allWarnings;
    }

    @Override
    public void refreshWarnings(final GetWarningsCallBack callBack) {
        //从api请求全部报警
        //模拟
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<Warning> list = new LinkedList<>();
                list.add(new Warning("报警1","温度过高"));
                list.add(new Warning("报警2","气压过高"));
                list.add(new Warning("报警3","湿度超标"));
                callBack.querySuccess(list);
            }
        },2000);
    }

    public interface newWarning{
        void warningComing();
    }

    private ExceptionHandler mExceptionHandler = new ExceptionHandler() {
        @Override
        public void handleUnexpectedConnectionDriverException(Connection conn, Throwable exception) {
            Log.i(TAG, "连接断开");
        }

        @Override
        public void handleReturnListenerException(Channel channel, Throwable exception) {
          //  Log.i(TAG, "2");
        }

        @Override
        public void handleFlowListenerException(Channel channel, Throwable exception) {
          //  Log.i(TAG, "3");
        }

        @Override
        public void handleConfirmListenerException(Channel channel, Throwable exception) {
         //   Log.i(TAG, "4");
        }

        @Override
        public void handleBlockedListenerException(Connection connection, Throwable exception) {
         //   Log.i(TAG, "5");
        }

        @Override
        public void handleConsumerException(Channel channel, Throwable exception, Consumer consumer, String consumerTag, String methodName) {
          //  Log.i(TAG, "6");
        }

        @Override
        public void handleConnectionRecoveryException(Connection conn, Throwable exception) {
          //  Log.i(TAG, "7");
        }

        @Override
        public void handleChannelRecoveryException(Channel ch, Throwable exception) {
          //  Log.i(TAG, "8");
        }

        @Override
        public void handleTopologyRecoveryException(Connection conn, Channel ch, TopologyRecoveryException exception) {
          //  Log.i(TAG, "9");
        }
    };
}
