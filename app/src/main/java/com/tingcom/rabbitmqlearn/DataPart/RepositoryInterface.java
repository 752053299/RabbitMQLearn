package com.tingcom.rabbitmqlearn.DataPart;

import java.util.List;

public interface RepositoryInterface {

    void getWarnings(GetWarningsCallBack callBack);

    interface GetWarningsCallBack{
        void querySuccess(List<Warning> warnings);
        void warningDataNotAvalible();
    }


}
