package com.tingcom.rabbitmqlearn;

import com.tingcom.rabbitmqlearn.DataPart.Warning;

import java.util.List;

public interface Contract {

    interface View {
        void setPresent(Present present);
        void onGetWarnings(List<Warning> warnings);
        void onNewWarningComing();
    }

    interface Present {
        void start();
        void getWarnings();
        void finish();
    }
}
