package com.roadtonerdana.jtelegraph.echo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.roadtonerdvana.jtelegraph.core.runner.Runner;

public class Main {

    
    public static void main(String []args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("springContext.xml");
        Runner runner = (Runner) context.getBean("runner");
        runner.start();
    }
}
