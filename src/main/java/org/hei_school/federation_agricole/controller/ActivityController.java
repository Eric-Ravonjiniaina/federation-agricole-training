package org.hei_school.federation_agricole.controller;

import org.hei_school.federation_agricole.controller.dto.Activity;
import org.hei_school.federation_agricole.repository.ActivityRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collectivities/{id}/activities")
public class ActivityController {
    private final ActivityRepository repository;

    public ActivityController(ActivityRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public List<Activity> createActivities(
            @PathVariable String id,
            @RequestBody List<Activity> activities) {
        return repository.saveAll(id, activities);
    }
}