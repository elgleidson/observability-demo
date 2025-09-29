package com.github.elgleidson.observability;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ServiceBClient {

  private static final Logger log = LoggerFactory.getLogger(ServiceBClient.class);

  private final WebClient webClient;

  public ServiceBClient(WebClient.Builder webClientBuilder) {
    this.webClient = webClientBuilder.build();
  }

  public Mono<String> callB() {
    return webClient.get()
      .uri("http://localhost:8082/b/something")
      .retrieve()
      .bodyToMono(String.class)
      .doFirst(() -> log.info("enter callB"))
      .doOnSuccess(response -> log.info("exit callB: response={}", response));
  }

}
