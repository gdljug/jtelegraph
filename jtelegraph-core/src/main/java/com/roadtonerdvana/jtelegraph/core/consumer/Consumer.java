package com.roadtonerdvana.jtelegraph.core.consumer;

import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.roadtonerdvana.jtelegraph.telegrambotapi.updates.Update;

public class Consumer implements Runnable {
    
    private static Logger logger = LogManager.getLogger();

    private Queue<Update> queue;
    private Command command;
    private Integer timeToSleepIfNoItemsInQueue;

    @Override
    public void run() {
        while (true) {
            while (!queue.isEmpty()) {
                command.execute(queue.poll());
            }
            try {
                Thread.sleep(timeToSleepIfNoItemsInQueue);
            } catch (InterruptedException e) {
                logger.error(e);
            }
        }
    }

    public void setQueue(Queue<Update> queue) {
        if (this.queue == null) {
            this.queue = queue;   
        }
    }

    public void setCommand(Command command) {
        if (this.command == null) {
            this.command = command;
        }
    }
    
    public void setTimeToSleepIfNoItemsInQueue(int timeToSleepIfNoItemsInQueue) {
        if (this.timeToSleepIfNoItemsInQueue == null) {
            this.timeToSleepIfNoItemsInQueue = timeToSleepIfNoItemsInQueue;
        }
    }
    
    

}
