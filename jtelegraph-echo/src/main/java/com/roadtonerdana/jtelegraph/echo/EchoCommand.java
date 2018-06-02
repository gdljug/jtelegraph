package com.roadtonerdana.jtelegraph.echo;

import com.roadtonerdvana.jtelegraph.core.MethodExecutor;
import com.roadtonerdvana.jtelegraph.core.Method;
import com.roadtonerdvana.jtelegraph.core.consumer.Command;
import com.roadtonerdvana.jtelegraph.telegrambotapi.types.Message;
import com.roadtonerdvana.jtelegraph.telegrambotapi.updates.SendMessage;
import com.roadtonerdvana.jtelegraph.telegrambotapi.updates.Update;

public class EchoCommand implements Command{

    private MethodExecutor methodExecutor;
    
    @Override
    public void execute(Update arg0) {
        if(arg0.getMessage().getText() !=null ) {
            SendMessage request = new SendMessage();
            request.setChatId(arg0.getMessage().getChat().getId()+"");
            request.setText(arg0.getMessage().getText());
            methodExecutor.executeMethodAsync(Method.SEND_MESSAGE, request, Message.class);
        }
    }

    public MethodExecutor getMethodExecutor() {
        return methodExecutor;
    }

    public void setMethodExecutor(MethodExecutor methodExecutor) {
        this.methodExecutor = methodExecutor;
    }
    
    

}
