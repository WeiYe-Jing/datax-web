package com.wugui.dataxweb;

import com.alibaba.datax.core.Engine;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataxWebApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(DataxWebApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Engine.startJob();
    }
}
