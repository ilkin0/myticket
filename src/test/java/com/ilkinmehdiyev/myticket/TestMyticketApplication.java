package com.ilkinmehdiyev.myticket;

import org.springframework.boot.SpringApplication;

public class TestMyticketApplication {

    public static void main(String[] args) {
        SpringApplication.from(MyticketApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
