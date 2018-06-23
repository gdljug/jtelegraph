package com.roadtonerdana.jtelegraph.echo;

import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roadtonerdvana.jtelegraph.core.Method;
import com.roadtonerdvana.jtelegraph.core.MethodExecutor;
import com.roadtonerdvana.jtelegraph.core.consumer.Command;
import com.roadtonerdvana.jtelegraph.core.consumer.Consumer;
import com.roadtonerdvana.jtelegraph.core.producer.Producer;
import com.roadtonerdvana.jtelegraph.core.runner.Runner;
import com.roadtonerdvana.jtelegraph.telegrambotapi.methods.SendMessage;
import com.roadtonerdvana.jtelegraph.telegrambotapi.types.Message;
import com.roadtonerdvana.jtelegraph.telegrambotapi.updates.Update;

public class Main {

    
    public static void main(String []args) {
        String token = "";
        final Queue<Update> queue = new ConcurrentLinkedQueue<Update>();
        final MethodExecutor methodExcecutor = new MethodExecutor()
                .setExecutorService(Executors.newFixedThreadPool(10))
                .setObjectMapper(new ObjectMapper())
                .setRestTemplate(new RestTemplate())
                .setMethods(new HashMap<Method, String>())
                .setUrl("https://api.telegram.org/bot").setToken(token);
        new Runner()
                .setProducer(new Producer()
                        .setLongPollingTimeOut(60000)
                        .setMaxUpdatesToGet(100).setQueue(queue)
                        .setTimeToSleepBeetweenPolling(1000)
                        .setMethodExecutor(methodExcecutor))
                .setConsumer(
                        new Consumer()
                        .setTimeToSleepIfNoItemsInQueue(1000)
                        .setQueue(queue)
                        .setCommand(new Command() {
                            @Override
                            public void execute(Update update) {
                                String text = update.getMessage().getText();
                                if(text == null || text.length() == 0) {
                                    text = "no habia texto";
                                } 
                                var sendMessage = new SendMessage()
                                        .withChatId(update.getMessage().getChat().getId().toString())
                                        .withText(text);
                                methodExcecutor.executeMethodAsync(Method.SEND_MESSAGE, sendMessage, Message.class);
                            }
                        }))
                .start();
    }
}
