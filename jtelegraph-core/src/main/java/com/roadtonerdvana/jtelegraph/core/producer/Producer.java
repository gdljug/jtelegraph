package com.roadtonerdvana.jtelegraph.core.producer;

import java.util.Arrays;
import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.roadtonerdvana.jtelegraph.core.MethodExecutor;
import com.roadtonerdvana.jtelegraph.core.Method;
import com.roadtonerdvana.jtelegraph.telegrambotapi.updates.GetUpdates;
import com.roadtonerdvana.jtelegraph.telegrambotapi.updates.Update;

public class Producer implements Runnable {
    
    private static Logger logger = LogManager.getLogger();

    private Queue<Update> queue;
    
    private MethodExecutor methodExecutor;
    
    private Integer maxUpdatesToGet;
    
    private Integer longPollingTimeOut;
    
    private Integer timeToSleepBeetweenPolling;

    public Update[] longPolling(GetUpdates request) {
        logger.info("Begin long polling");
        return methodExecutor.executeMethod(Method.GET_UPDATES, request, Update[].class);
    }


    @Override
    public void run() {
        try {
            GetUpdates request = new GetUpdates();
            request.setOffset(null);
            request.setLimit(maxUpdatesToGet);
            request.setTimeout(longPollingTimeOut);
            while (true) {
                Update[] updates = longPolling(request);
                if (updates != null && updates.length != 0) {
                    queue.addAll(Arrays.asList(updates));
                    request.setOffset(updates[updates.length - 1].getUpdateId() + 1);
                }
                Thread.sleep(timeToSleepBeetweenPolling);

            }
        } catch (Exception e) {
            logger.error(e);
        }

    }

    public void setQueue(Queue<Update> queue) {
        if (this.queue == null) { 
            this.queue = queue;
        }
    }

    public void setMethodExecutor(MethodExecutor methodExecutor) {
        if (this.methodExecutor == null) {
            this.methodExecutor = methodExecutor;
        }
    }


    public void setMaxUpdatesToGet(int maxUpdatesToGet) {
        if (this.maxUpdatesToGet == null) {
            this.maxUpdatesToGet = maxUpdatesToGet;
        }
    }

    public void setLongPollingTimeOut(int longPollingTimeOut) {
        if (this.longPollingTimeOut == null) {
            this.longPollingTimeOut = longPollingTimeOut;
        }        
    }

    public void setTimeToSleepBeetweenPolling(int timeToSleepBeetweenPolling) {
        if (this.timeToSleepBeetweenPolling == null) {
            this.timeToSleepBeetweenPolling = timeToSleepBeetweenPolling;
        }     
    }

}
