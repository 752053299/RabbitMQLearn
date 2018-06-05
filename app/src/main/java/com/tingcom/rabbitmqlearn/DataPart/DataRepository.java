package com.tingcom.rabbitmqlearn.DataPart;

import android.os.Handler;

import java.util.LinkedList;
import java.util.List;

public class DataRepository implements RepositoryInterface {

    private volatile List<Warning> allWarnings = new LinkedList<>();

    private DataRepository(){

    }

    private static class DataRepositoryHolder{
        private final static DataRepository instance = new DataRepository();
    }

    public static DataRepository getInstance(){
        return DataRepositoryHolder.instance;
    }

    public void init(){
        getWarnings(new GetWarningsCallBack() {
            @Override
            public void querySuccess(List<Warning> warnings) {
                addWarnings(warnings);
            }

            @Override
            public void warningDataNotAvalible() {

            }
        });
    }

    private synchronized void addWarnings(List<Warning> warnings){
        allWarnings.clear();
        allWarnings.addAll(warnings);
    }

    @Override
    public void getWarnings(final GetWarningsCallBack callBack) {
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
}
