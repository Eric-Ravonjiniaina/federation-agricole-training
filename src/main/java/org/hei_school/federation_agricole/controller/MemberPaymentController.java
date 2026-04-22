package org.hei_school.federation_agricole.controller;

import org.hei_school.federation_agricole.entity.MemberPayment;
import org.hei_school.federation_agricole.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/members")
public class MemberPaymentController {


    @PostMapping("/{id}/payments")
    public ResponseEntity<List<MemberPayment>> createPayments(
            @PathVariable String id,
            @RequestBody ) {

        List<MemberPayment> results = dtos.stream()
                .map(dto ->)
                .collect(Collectors.toList());

        return new ResponseEntity<>(results, HttpStatus.CREATED);
    }
}