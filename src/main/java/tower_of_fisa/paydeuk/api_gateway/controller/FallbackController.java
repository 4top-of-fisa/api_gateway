package tower_of_fisa.paydeuk.api_gateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
public class FallbackController {

	@RequestMapping("/fallback")
	public Mono<String> fallback() {
		log.info("fallback handler triggered!");
		return Mono.just("fallback");
	}
}
