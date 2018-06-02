package com.roadtonerdvana.jtelegraph.core.runner;

import com.roadtonerdvana.jtelegraph.core.consumer.Consumer;
import com.roadtonerdvana.jtelegraph.core.producer.Producer;

public class Runner {
    
    private Producer producer;
    private Consumer consumer;
    
    public void start() {
        new Thread(producer).start();
        new Thread(consumer).start();
    }

    public Producer getProducer() {
        return producer;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }
    
    
    
}