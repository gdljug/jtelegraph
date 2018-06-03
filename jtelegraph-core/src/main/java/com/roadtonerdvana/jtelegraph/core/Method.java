package com.roadtonerdvana.jtelegraph.core;

public enum Method {
    GET_UPDATES("/getUpdates"), 
    SEND_MESSAGE("/sendMessage"), 
    SEND_PHOTO_WITH_ID("/sendPhoto"), 
    SEND_PHOTO_WITH_FILE("/sendPhoto", "photo");

    private String methodName;
    private String fileKey;


    private Method(String methodName) {
        this.methodName = methodName;
    }
    
    private Method(String methodName, String fileKey) {
        this(methodName);
        this.fileKey = fileKey;
    }

    public String getMethodName() {
        return methodName;
    }
    
    public String getFileKey() {
        return fileKey;
    }
}
