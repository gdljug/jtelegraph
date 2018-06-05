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

    public Runner setProducer(Producer producer) {
        if (this.producer == null) {
            this.producer = producer;
        }
        return this;
    }

    public Runner setConsumer(Consumer consumer) {
        if (this.consumer == null) {
            this.consumer = consumer;
        }
        return this;
    }
    
    
    
}