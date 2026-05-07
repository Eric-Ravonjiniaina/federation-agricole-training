package org.hei_school.federation_agricole.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityTestController {

    @GetMapping("/ping")
    public String ping() {
        return "Pong ! La clé API est valide.";
    }
}