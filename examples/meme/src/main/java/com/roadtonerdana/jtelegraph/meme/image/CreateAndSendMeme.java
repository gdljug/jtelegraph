package com.roadtonerdana.jtelegraph.meme.image;

import java.io.File;
import java.io.IOException;


import com.roadtonerdvana.jtelegraph.core.Method;
import com.roadtonerdvana.jtelegraph.core.MethodExecutor;
import com.roadtonerdvana.jtelegraph.telegrambotapi.types.Message;
import com.roadtonerdvana.jtelegraph.telegrambotapi.methods.GetFile;
import com.roadtonerdvana.jtelegraph.telegrambotapi.methods.SendPhoto;

public class CreateAndSendMeme {


    
    private static String downloadFile(MethodExecutor methodExecutor, String fileId) {
        var getFile = new GetFile().withFileId(fileId);
        com.roadtonerdvana.jtelegraph.telegrambotapi.types.File file = methodExecutor.executeMethod(Method.GET_FILE, getFile, com.roadtonerdvana.jtelegraph.telegrambotapi.types.File.class);
        return methodExecutor.downloadFile(file);
    }
    
    private static String createMeme(MethodExecutor methodExecutor, String memeDirectoryPath, MemeRequest memeRequest) throws MemeBuilderException {
        return new MemeBuilder().loadImage(downloadFile(methodExecutor, memeRequest.getFile())).toTargetDirectory(memeDirectoryPath)
                .withUpperText(memeRequest.getUpperText()).withLowerText(memeRequest.getLowerText()).build();
    }
    
    public static String sendMeme(MethodExecutor methodExecutor, String chatId, MemeRequest memeRequest, String memeDirectoryPath) throws MemeBuilderException, IOException {
        var path = createMeme(methodExecutor,memeDirectoryPath,memeRequest);
        var file = new File(path);
        var sendPhoto = new SendPhoto()
                .withChatId(chatId)
                .withPhoto(file.getAbsolutePath());
        return methodExecutor.executeMethod(Method.SEND_PHOTO_WITH_FILE, sendPhoto, Message.class).toString();
    } 
    
    
}
