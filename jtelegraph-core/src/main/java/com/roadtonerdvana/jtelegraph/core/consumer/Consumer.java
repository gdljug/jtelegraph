package com.roadtonerdvana.jtelegraph.core.consumer;

import java.util.Queue;

import com.roadtonerdvana.jtelegraph.telegrambotapi.updates.Update;

public class Consumer implements Runnable {

    private Queue<Update> queue;
    private Command command;

    @Override
    public void run() {
        while (true) {
            while (!queue.isEmpty()) {
                command.execute(queue.poll());
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void setQueue(Queue<Update> queue) {
        this.queue = queue;
    }

    public void setCommand(Command command) {
        this.command = command;
    }
    
    

}
