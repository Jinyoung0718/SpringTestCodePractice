package com.sjy.dayonetest.service;

import org.springframework.stereotype.Service;

@Service
public class KafKaConsumerService {

    public void process(String message) {
        System.out.println("processing ..." + message);
    }
}
