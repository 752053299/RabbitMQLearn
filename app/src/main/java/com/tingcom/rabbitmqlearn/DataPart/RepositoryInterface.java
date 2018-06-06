package com.tingcom.rabbitmqlearn.DataPart;

import java.util.List;

public interface RepositoryInterface {

    void refreshWarnings(GetWarningsCallBack callBack);

    interface GetWarningsCallBack{
        void querySuccess(List<Warning> warnings);
        void warningDataNotAvalible();
    }


}
