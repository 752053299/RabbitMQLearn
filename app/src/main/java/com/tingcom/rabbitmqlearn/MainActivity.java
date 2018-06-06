package com.tingcom.rabbitmqlearn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.tingcom.rabbitmqlearn.DataPart.DataRepository;
import com.tingcom.rabbitmqlearn.DataPart.Warning;

import java.util.List;

public class MainActivity extends AppCompatActivity implements Contract.View{
    private static final String TAG = "DataRepository";
    private Contract.Present mPresent;
    private List<Warning> mWarnings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        mPresent = new MyPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresent.start();
        mPresent.getWarnings();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
        mPresent.finish();
    }

    @Override
    public void setPresent(Contract.Present present) {

    }

    @Override
    public void onGetWarnings(List<Warning> warnings) {
        Log.i(TAG, "onGetWarnings: " + warnings);
        mWarnings = warnings;
    }

    @Override
    public void onNewWarningComing() {
        Log.i(TAG, "onNewWarningComing: " + mWarnings);
    }
}
