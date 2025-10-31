package com.system.design.system.design.impl.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("api/idempotencyKey/")
public class IdempotencyController {

    @PostMapping
    public String generateIdempotencyKey() {
        return "IDEMP-" + UUID.randomUUID();
    }
}
