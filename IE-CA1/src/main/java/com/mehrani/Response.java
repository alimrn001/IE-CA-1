package com.mehrani;

public class Response {
    private boolean status;
    private String data;

    public void setData(String data) {
        this.data = data;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    public boolean getStatus() {
        return status;
    }
    public String getData() {
        return data;
    }
}
