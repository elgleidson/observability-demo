package observability;

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
public class ControllerB {

  private static final Logger log = LoggerFactory.getLogger(ControllerB.class);
  private static final Random delayGenerator = new Random(System.currentTimeMillis());

  @GetMapping("/b/something")
  public Mono<ResponseEntity<String>> something() {
    return Mono.just("response from B at " + LocalDateTime.now())
      .map(ResponseEntity::ok)
      .delayElement(Duration.ofMillis(delayGenerator.nextInt(100, 1_000)))
      .doFirst(() -> log.info("enter something"))
      .doOnSuccess(response -> log.info("exit something: response={}", response));
  }

}
