package com.roadtonerdvana.jtelegraph.core;

public enum Method {
    GET_UPDATES("/getUpdates"), SEND_MESSAGE("/sendMessage");
    
    private String methodName;
    
    private Method(String methodName) {
        this.methodName = methodName;
    }
    
    public String getMethodName() {
        return methodName;
    }
}
