package com.roadtonerdana.jtelegraph.meme;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.roadtonerdvana.jtelegraph.core.Method;
import com.roadtonerdvana.jtelegraph.core.MethodExecutor;
import com.roadtonerdvana.jtelegraph.core.consumer.Command;
import com.roadtonerdvana.jtelegraph.telegrambotapi.types.Message;
import com.roadtonerdvana.jtelegraph.telegrambotapi.types.PhotoSize;
import com.roadtonerdvana.jtelegraph.telegrambotapi.updates.SendMessage;
import com.roadtonerdvana.jtelegraph.telegrambotapi.updates.Update;
import com.roadtonerdana.jtelegraph.meme.image.CreateAndSendMeme;
import com.roadtonerdana.jtelegraph.meme.image.MemeRequest;

public class MemeCommand implements Command{
    private static Logger logger = LogManager.getLogger();

    private MethodExecutor methodExecutor;

    private Map<String, MemeRequest> requestMap = new HashMap<String, MemeRequest>();
    
    private ExecutorService executorService;

    private String memeDirectoryPath;
    
    @Override
    public void execute(Update update) {
        try {
            logger.info(update);
            String chatId = update.getMessage().getChat().getId().toString();
            MemeRequest memeRequest = null;
            if(requestMap.containsKey(chatId)) {
               memeRequest = requestMap.get(chatId);
            } else {
               memeRequest = new MemeRequest(); 
            }
            switch (memeRequest.getState()) {
                case IMAGE:
                    if(update.getMessage().getPhoto() == null || update.getMessage().getPhoto().isEmpty()) {
                        sendMessage(chatId,"Comienza enviando una imagen");
                    } else {
                        List<PhotoSize> photo = update.getMessage().getPhoto();
                        memeRequest.setFile(photo.get(photo.size()-1).getFileId());
                        memeRequest.setNextState();
                        requestMap.put(chatId, memeRequest);
                        sendMessage(chatId,"Imagen Recibida. Envia el texto de arriba o un punto para dejarlo vacio");
                    }
                    break;
                case LOWER_TEXT:
                    String lowerText = update.getMessage().getText();
                    if (lowerText == null) {
                        if(memeRequest.getUpperText() == null) {
                            sendMessage(chatId,"En este paso envia el texto de abajo o un punto para dejarlo vacio");
                        } else {
                            sendMessage(chatId,"En este paso envia el texto de abajo");
                        }
                    } else {
                        if(".".equals(lowerText) && memeRequest.getUpperText() == null) {
                            sendMessage(chatId,"No puedes dejar vacios ambos textos, envia el texto de abajo");
                        } else {
                            memeRequest.setLowerText(lowerText);
                            memeRequest.setNextState();
                            requestMap.put(chatId, memeRequest);
                            final MemeRequest clonedMemeRequest = new MemeRequest(memeRequest);
                            executorService.execute(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        CreateAndSendMeme.sendMeme(methodExecutor, chatId, clonedMemeRequest, memeDirectoryPath);
                                    } catch (Exception e) {
                                        logger.error(e);
                                    } 
                                }
                                
                            });
                        }
                    }
                    break;
                case UPPER_TEXT:
                    String upperText = update.getMessage().getText();
                    if (upperText == null) {
                        sendMessage(chatId,"En este paso envia el texto de arriba o un punto para dejarlo vacio");
                    } else {
                        memeRequest.setUpperText(".".equals(upperText) ? null : upperText);
                        memeRequest.setNextState();
                        requestMap.put(chatId, memeRequest);
                        if(memeRequest.getUpperText() == null ) {
                            sendMessage(chatId,"Texto Recibido. Envia el texto de abajo");
                        } else {
                            sendMessage(chatId,"Texto Recibido. Envia el texto de abajo o un punto para dejarlo vacio");
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }
    
    private void sendMessage(String chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        methodExecutor.executeMethodAsync(Method.SEND_MESSAGE, sendMessage, Message.class);
    }
    
    public void setMethodExecutor (MethodExecutor methodExecutor) {
        if (this.methodExecutor == null) {
            this.methodExecutor = methodExecutor;
        }
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public String getMemeDirectoryPath() {
        return memeDirectoryPath;
    }

    public void setMemeDirectoryPath(String memeDirectoryPath) {
        this.memeDirectoryPath = memeDirectoryPath;
    }
    
    

}
