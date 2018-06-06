package com.tingcom.rabbitmqlearn.DataPart;

import java.util.Date;

public class Warning {
    private String name;
    private String content;
    private int value;
    private Date time;

    public Warning(){

    }

    public Warning(String name,String content){
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }


    @Override
    public String toString() {
        return "name:" + name + "  content:" + content;
    }
}
