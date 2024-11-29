package com.example;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.opentelemetry.instrumentation.annotations.SpanAttribute;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Usecase {

    private static final Logger LOG = LoggerFactory.getLogger(Usecase.class);
    private static final Random random = new Random();

    @WithSpan
    public void doSomething(@SpanAttribute String message) {
        LOG.info("Received: " + message);
    }


}
