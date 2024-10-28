package com.gjp;

import com.gjp.controller.NettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.util.concurrent.Callable;

@SpringBootApplication
public class TestApplication implements CommandLineRunner {

    public static void main(String[] args) {
        try{
            SpringApplication.run(TestApplication.class, args);
        }catch (Exception e){
            e.printStackTrace();

        }

    }

    @Autowired
    NettyServer nettyServer;

    @Override
    public void run(String... args) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                nettyServer.start();
            }
        }).start();
    }
}
