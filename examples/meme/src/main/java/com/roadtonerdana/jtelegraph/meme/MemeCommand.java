package com.roadtonerdana.jtelegraph.meme;


import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.roadtonerdvana.jtelegraph.core.Method;
import com.roadtonerdvana.jtelegraph.core.MethodExecutor;
import com.roadtonerdvana.jtelegraph.core.consumer.Command;
import com.roadtonerdvana.jtelegraph.telegrambotapi.types.Message;
import com.roadtonerdvana.jtelegraph.telegrambotapi.updates.SendPhoto;
import com.roadtonerdvana.jtelegraph.telegrambotapi.updates.Update;

public class MemeCommand implements Command{
    
    private MethodExecutor methodExecutor;

    @Override
    public void execute(Update arg0) {
        try {
            Resource resource = new ClassPathResource("memes/smartEddie.jpg");
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(arg0.getMessage().getChat().getId().toString());
            sendPhoto.setPhoto(resource.getFile().getAbsolutePath());
            Message message = methodExecutor.executeMethod(Method.SEND_PHOTO_WITH_FILE, sendPhoto, Message.class);
            if (message != null) {
                System.out.println("---------------");
                System.out.println(message);
                System.out.println("---------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setMethodExecutor (MethodExecutor methodExecutor) {
        if (this.methodExecutor == null) {
            this.methodExecutor = methodExecutor;
        }
    }

}
