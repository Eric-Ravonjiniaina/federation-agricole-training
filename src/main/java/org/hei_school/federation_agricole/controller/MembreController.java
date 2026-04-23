package org.hei_school.federation_agricole.controller;

import org.hei_school.federation_agricole.dto.MemberDTO;
import org.hei_school.federation_agricole.entity.MemberEntity;
import org.hei_school.federation_agricole.exception.BadRequestException;
import org.hei_school.federation_agricole.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MembreController {
    private final MemberService service;

    public MembreController(MemberService  service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> createMembers(@RequestBody List<MemberDTO> dtos) {
        try {
            List<MemberEntity> created = new ArrayList<>();

            for (MemberDTO dto : dtos) {
                created.add(service.create(dto));
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(created);

        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Collectivity or Member not found");
        }
    }
}
