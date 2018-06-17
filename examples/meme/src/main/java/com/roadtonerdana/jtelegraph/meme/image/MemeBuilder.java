package com.roadtonerdana.jtelegraph.meme.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MemeBuilder {
    
    private static Logger logger = LogManager.getLogger();


    private Meme meme;
   
    private class Meme{
        private BufferedImage bufferedImage;
        private String upperText;
        private String lowerText;
        private String targetFileName;
        private String targetPath;
        
        public BufferedImage getBufferedImage() {
            return bufferedImage;
        }
        public void setBufferedImage(BufferedImage bufferedImage) {
            this.bufferedImage = bufferedImage;
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
        public String getTargetFileName() {
            return targetFileName;
        }
        public void setTargetFileName(String targetFileName) {
            this.targetFileName = targetFileName;
        }
        public String getTargetPath() {
            return targetPath;
        }
        public void setTargetPath(String targetPath) {
            this.targetPath = targetPath;
        }
  
        
    }
    
    public MemeBuilder() {
        meme = new Meme();
    }
    
    public MemeBuilder loadImage(String filePath) throws MemeBuilderException {
        try {
            File f = new File(filePath);
            meme.setBufferedImage(ImageIO.read(f));
            meme.setTargetFileName(f.getName().replace('.', 'o'));
            return this;
        } catch (Exception e) {
            throw new MemeBuilderException("Failed to load image: "+filePath, e);
        }
    }
    
    public MemeBuilder toTargetDirectory(String directoryPath) throws MemeBuilderException {
        File f = new File(directoryPath);
        if(f.exists() && f.isDirectory() ) {
            meme.setTargetPath(f.getAbsolutePath() + File.separatorChar + meme.getTargetFileName() + ".png");
            return this;
        } else {
            throw new MemeBuilderException("Invalid path: "+ directoryPath);
        }
    }
        
    public MemeBuilder withUpperText(String upperText) {
        meme.setUpperText(upperCaseOrNull(upperText));
        return this;

    }
    
    public MemeBuilder withLowerText(String lowerText) {
        meme.setLowerText(upperCaseOrNull(lowerText));
        return this;
    }
    
    public String build() throws MemeBuilderException {
        FileOutputStream fos = null;
        try {  
            ImageOverlay.overlay(meme.getBufferedImage(), meme.getUpperText(), meme.getLowerText());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(meme.getBufferedImage(), "png", baos);
            fos = new FileOutputStream(meme.getTargetPath());
            fos.write(baos.toByteArray());
            return meme.getTargetPath();
        } catch(Exception e) {
            throw new MemeBuilderException("Failed to build meme", e);
        } finally {
            if(fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    logger.error("Exception while closing FileOutputStream",e);
                }
            }
        }

    }
    
    private String upperCaseOrNull(String text) {
        return text == null ? null : text.toUpperCase();
    }
    
}
