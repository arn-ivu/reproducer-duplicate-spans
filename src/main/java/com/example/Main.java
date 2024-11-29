package com.example;

import java.time.Duration;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static final Tracer TRACER = GlobalOpenTelemetry.getTracer(Main.class.getName());
    private static final Random random = new Random();

    private final Usecase usecase;

    public Main(final Usecase usecase) {
        this.usecase = usecase;
    }

    public void main(@Observes final StartupEvent event) {
        Multi.createBy().repeating()
                .uni(this::startUseCaseDelayed)
                .withDelay(Duration.ofSeconds(9))
                .indefinitely()
                .subscribe()
                .with(item -> LOG.info("Subscription completed"));
    }

    private Uni<Void> startUseCaseDelayed(){
        final int delay = random.nextInt(900, 1500);

        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        startUseCase(delay);
        return Uni.createFrom().voidItem();
    }

    private  void startUseCase(int delay) {
        Span span = TRACER.spanBuilder("Main.main")
                .setNoParent()
                .setAttribute("delay", delay)
                .startSpan();

        try (final Scope scope = span.makeCurrent()) {
            usecase.doSomething(random.nextInt(10000));
        } finally {
            span.end();
        }
    }

}
