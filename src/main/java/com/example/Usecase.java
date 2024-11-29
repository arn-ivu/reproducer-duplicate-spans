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
    public void doSomething(@SpanAttribute int n) {
        int fib = fibonacci(n);
        LOG.info("calculated fibonacci: " + fib);
    }

    private int fibonacci(final int i) {
        int x1 = 1;
        int x2 = 1;

        for(int j = 0; j < i; j++) {
            int x = x1 + x2;
            x1 = x2;
            x2 = x;
        }
        return x2;
    }

}
