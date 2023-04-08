package com.cantcode.overengineeredtodoserver.controllers.api;

import com.cantcode.overengineeredtodoserver.controllers.APIDefinition;
import com.cantcode.overengineeredtodoserver.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(APIDefinition.SECURITY)
@Slf4j
public class SecurityController {

    @GetMapping(value = "/email", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> getEmail() {
        return SecurityUtils.getEmail();
    }

    @GetMapping(value = "/id", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> getUserId() {
        return SecurityUtils.getUserId();
    }
}
