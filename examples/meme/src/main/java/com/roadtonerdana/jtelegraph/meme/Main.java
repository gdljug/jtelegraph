package com.roadtonerdana.jtelegraph.meme;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.roadtonerdana.jtelegraph.meme.image.MemeBuilderException;
import com.roadtonerdvana.jtelegraph.core.runner.Runner;

public class Main {

    public static void main(String[] args) throws IOException, MemeBuilderException {
        ApplicationContext context = new
        ClassPathXmlApplicationContext("springContext.xml");
        Runner runner = (Runner) context.getBean("runner");
        runner.start();
    }

}
