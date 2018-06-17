package com.roadtonerdana.jtelegraph.meme.image;

public class MemeRequest {

    public enum State{
        IMAGE, UPPER_TEXT, LOWER_TEXT;
    }
    
    private State state;
    private String file;
    private String upperText;
    private String lowerText;
    
    public MemeRequest() {
        state = State.IMAGE;
    }
    
    public MemeRequest(MemeRequest memeRequest) {
        this.state = memeRequest.state;
        this.file = memeRequest.file;
        this.upperText = memeRequest.upperText;
        this.lowerText = memeRequest.lowerText;
    }
    
    public State getState() {
        return state;
    }
    public void setNextState() {
        switch(state) {
            case IMAGE:
                state = State.UPPER_TEXT;
                break;
            case LOWER_TEXT:
                state = State.IMAGE;
                break;
            case UPPER_TEXT:
                state = State.LOWER_TEXT;
                break;
            
        }
    }
    public String getFile() {
        return file;
    }
    public void setFile(String file) {
        this.file = file;
    }
    public String getUpperText() {
        return upperText;
    }
    public void setUpperText(String upperText) {
        this.upperText = upperText;
    }
    public String getLowerText() {
        return lowerText;
    }
    public void setLowerText(String lowerText) {
        this.lowerText = lowerText;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((file == null) ? 0 : file.hashCode());
        result = prime * result + ((lowerText == null) ? 0 : lowerText.hashCode());
        result = prime * result + ((state == null) ? 0 : state.hashCode());
        result = prime * result + ((upperText == null) ? 0 : upperText.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MemeRequest other = (MemeRequest) obj;
        if (file == null) {
            if (other.file != null)
                return false;
        } else if (!file.equals(other.file))
            return false;
        if (lowerText == null) {
            if (other.lowerText != null)
                return false;
        } else if (!lowerText.equals(other.lowerText))
            return false;
        if (state != other.state)
            return false;
        if (upperText == null) {
            if (other.upperText != null)
                return false;
        } else if (!upperText.equals(other.upperText))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "MemeRequest [state=" + state + ", file=" + file + ", upperText=" + upperText + ", lowerText="
                + lowerText + "]";
    }
    
    
    
}
