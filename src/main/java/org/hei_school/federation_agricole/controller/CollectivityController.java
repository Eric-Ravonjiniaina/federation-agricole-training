package org.hei_school.federation_agricole.controller;

import org.hei_school.federation_agricole.dto.request.AssignCollectivityIdentityRequest;
import org.hei_school.federation_agricole.dto.request.CreateCollectivityRequest;
import org.hei_school.federation_agricole.dto.request.CreateMembershipFee;
import org.hei_school.federation_agricole.dto.response.CollectivityResponse;
import org.hei_school.federation_agricole.entity.CollectivityTransaction;
import org.hei_school.federation_agricole.entity.MembershipFee;
import org.hei_school.federation_agricole.mapper.CollectivityMapper;
import org.hei_school.federation_agricole.service.CollectivityService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class CollectivityController {

    private final CollectivityService service;

    public CollectivityController(CollectivityService service) {
        this.service = service;
    }

    @PostMapping("/collectivities")
    @ResponseStatus(HttpStatus.CREATED)
    public List<CollectivityResponse> create(@RequestBody List<CreateCollectivityRequest> req) {

        return service.create(req)
                .stream()
                .map(CollectivityMapper::toResponse)
                .toList();
    }

    @GetMapping("/collectivities/{id}/transactions")
    public List<CollectivityTransaction> getTransactions(
            @PathVariable String id,
            @RequestParam String from,
            @RequestParam String to
    ) {

        return service.getTransactions(
                id,
                LocalDate.parse(from),
                LocalDate.parse(to)
        );
    }
    @PutMapping("/collectivities/{id}/informations")
    public CollectivityResponse assignIdentity(
            @PathVariable String id,
            @RequestBody AssignCollectivityIdentityRequest req
    ) {
        return CollectivityMapper.toResponse(
                service.assignIdentity(id, req)
        );
    }
    @GetMapping("/collectivities/{id}/membershipFees")
    public List<MembershipFee> getMembershipFees(@PathVariable String id) {
        return service.getMembershipFees(id);
    }


    @PostMapping("/collectivities/{id}/membershipFees")
    public List<MembershipFee> createMembershipFees(
            @PathVariable String id,
            @RequestBody List<CreateMembershipFee> req
    ) {
        return service.createMembershipFees(id, req);
    }
}
