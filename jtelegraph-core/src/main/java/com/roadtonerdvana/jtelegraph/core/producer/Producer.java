package com.roadtonerdvana.jtelegraph.core.producer;

import java.util.Arrays;
import java.util.Queue;

import org.springframework.web.client.RestTemplate;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roadtonerdvana.jtelegraph.core.MethodExecutor;
import com.roadtonerdvana.jtelegraph.core.Method;
import com.roadtonerdvana.jtelegraph.telegrambotapi.updates.GetUpdates;
import com.roadtonerdvana.jtelegraph.telegrambotapi.updates.Update;

public class Producer implements Runnable {

    private Queue<Update> queue;
    
    private MethodExecutor methodExecutor;

    public Update[] longPolling(GetUpdates request) {
        System.out.println("Begin long polling");
        return methodExecutor.executeMethod(Method.GET_UPDATES, request, Update[].class);
    }


    @Override
    public void run() {
        try {
            GetUpdates request = new GetUpdates();
            request.setOffset(null);
            request.setLimit(100);
            request.setTimeout(60000);
            while (true) {
                Update[] updates = longPolling(request);
                if (updates != null && updates.length != 0) {
                    queue.addAll(Arrays.asList(updates));
                    request.setOffset(updates[updates.length - 1].getUpdateId() + 1);
                }
                Thread.sleep(1000);

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    public Queue<Update> getQueue() {
        return queue;
    }

    public void setQueue(Queue<Update> queue) {
        this.queue = queue;
    }


    public MethodExecutor getMethodExecutor() {
        return methodExecutor;
    }


    public void setMethodExecutor(MethodExecutor methodExecutor) {
        this.methodExecutor = methodExecutor;
    }



}
