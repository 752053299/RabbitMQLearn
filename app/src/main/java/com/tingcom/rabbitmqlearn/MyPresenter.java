package com.tingcom.rabbitmqlearn;

import com.tingcom.rabbitmqlearn.DataPart.DataRepository;
import com.tingcom.rabbitmqlearn.DataPart.RepositoryInterface;
import com.tingcom.rabbitmqlearn.DataPart.Warning;

import java.util.LinkedList;
import java.util.List;

public class MyPresenter implements Contract.Present {

    private Contract.View mView;
    public MyPresenter(Contract.View mView){
        this.mView = mView;
        mView.setPresent(this);
    }
    @Override
    public void start() {
        DataRepository.getInstance().init();
        DataRepository.getInstance().startReceiveWarning();
        DataRepository.getInstance().setNewWarningListener(new DataRepository.newWarning() {
            @Override
            public void warningComing() {
                mView.onNewWarningComing();
            }
        });

    }

    @Override
    public void getWarnings() {
        DataRepository.getInstance().refreshWarnings(new RepositoryInterface.GetWarningsCallBack() {
            @Override
            public void querySuccess(List<Warning> warnings) {
                mView.onGetWarnings(DataRepository.getInstance().getAllWarnings());
            }

            @Override
            public void warningDataNotAvalible() {

            }
        });


    }

    @Override
    public void finish() {

        DataRepository.getInstance().isQuit = true;
        DataRepository.getInstance().stopReceiveWarning();
    }
}
