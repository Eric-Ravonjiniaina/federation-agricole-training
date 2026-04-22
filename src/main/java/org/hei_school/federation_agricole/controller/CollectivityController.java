package org.hei_school.federation_agricole.controller;

import org.hei_school.federation_agricole.dto.request.CreateCollectivityRequest;
import org.hei_school.federation_agricole.dto.response.CollectivityResponse;
import org.hei_school.federation_agricole.entity.Collectivity;
import org.hei_school.federation_agricole.mapper.CollectivityMapper;
import org.hei_school.federation_agricole.service.CollectivityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CollectivityController {

    private final CollectivityService service;

    public CollectivityController(CollectivityService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<CollectivityResponse> create(@RequestBody List<CreateCollectivityRequest> req) {

        return service.create(req)
                .stream()
                .map(CollectivityMapper::toResponse)
                .toList();
    }
}
