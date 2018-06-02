package com.roadtonerdvana.jtelegraph.core.consumer;

import com.roadtonerdvana.jtelegraph.telegrambotapi.updates.Update;

public interface Command {

    public void execute(Update update);
    
}
