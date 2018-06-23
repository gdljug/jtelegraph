package com.roadtonerdana.jtelegraph.meme.image;

public class MemeBuilderException extends Exception{

    /**
     * 
     */
    private static final long serialVersionUID = -3938912013001148453L;
    
    public MemeBuilderException(String message) {
        super(message);
    }
    public MemeBuilderException(String message, Throwable t) {
        super(message,t);
    }
}
