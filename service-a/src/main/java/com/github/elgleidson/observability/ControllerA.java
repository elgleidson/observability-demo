package com.github.elgleidson.observability;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ControllerA {

  private static final Logger log = LoggerFactory.getLogger(ControllerA.class);
  private static final Random delayGenerator = new Random(System.currentTimeMillis());

  private final ServiceBClient serviceBClient;

  public ControllerA(ServiceBClient serviceBClient) {
    this.serviceBClient = serviceBClient;
  }

  @GetMapping("/a/something")
  public Mono<ResponseEntity<String>> something() {
    return serviceBClient.callB()
      .map(responseB -> "response from A at " + LocalDateTime.now() + " - " + responseB)
      .delayElement(Duration.ofMillis(delayGenerator.nextInt(100, 1_000)))
      .map(ResponseEntity::ok)

      .doFirst(() -> log.info("enter something"))
      .doOnSuccess(response -> log.info("exit something: response={}", response));
  }

}
