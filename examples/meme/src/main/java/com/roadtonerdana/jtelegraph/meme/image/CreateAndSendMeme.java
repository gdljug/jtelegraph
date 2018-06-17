package com.roadtonerdana.jtelegraph.meme.image;

import java.io.File;
import java.io.IOException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.roadtonerdvana.jtelegraph.core.Method;
import com.roadtonerdvana.jtelegraph.core.MethodExecutor;
import com.roadtonerdvana.jtelegraph.telegrambotapi.types.Message;
import com.roadtonerdvana.jtelegraph.telegrambotapi.updates.GetFile;
import com.roadtonerdvana.jtelegraph.telegrambotapi.updates.SendPhoto;

public class CreateAndSendMeme {


    
    private static String downloadFile(MethodExecutor methodExecutor, String fileId) {
        GetFile getFile = new GetFile();
        getFile.setFileId(fileId);
        com.roadtonerdvana.jtelegraph.telegrambotapi.types.File file = methodExecutor.executeMethod(Method.GET_FILE, getFile, com.roadtonerdvana.jtelegraph.telegrambotapi.types.File.class);
        return methodExecutor.downloadFile(file);
    }
    
    private static String createMeme(MethodExecutor methodExecutor, String memeDirectoryPath, MemeRequest memeRequest) throws MemeBuilderException {
        return new MemeBuilder().loadImage(downloadFile(methodExecutor, memeRequest.getFile())).toTargetDirectory(memeDirectoryPath)
                .withUpperText(memeRequest.getUpperText()).withLowerText(memeRequest.getLowerText()).build();
    }
    
    public static String sendMeme(MethodExecutor methodExecutor, String chatId, MemeRequest memeRequest, String memeDirectoryPath) throws MemeBuilderException, IOException {
        String path = createMeme(methodExecutor,memeDirectoryPath,memeRequest);
        File file = new File(path);
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(file.getAbsolutePath());
        return methodExecutor.executeMethod(Method.SEND_PHOTO_WITH_FILE, sendPhoto, Message.class).toString();
    } 
    
    
}
