package org.hei_school.federation_agricole.controller;

import lombok.RequiredArgsConstructor;
import org.hei_school.federation_agricole.controller.dto.CreateMember;
import org.hei_school.federation_agricole.controller.dto.CreateMemberPayment;
import org.hei_school.federation_agricole.controller.mapper.MemberDtoMapper;
import org.hei_school.federation_agricole.controller.mapper.MemberPaymentDtoMaper;
import org.hei_school.federation_agricole.entity.Member;
import org.hei_school.federation_agricole.entity.MemberPayment;
import org.hei_school.federation_agricole.exception.BadRequestException;
import org.hei_school.federation_agricole.exception.NotFoundException;
import org.hei_school.federation_agricole.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MemberDtoMapper memberDtoMapper;
    private final MemberPaymentDtoMaper memberPaymentDtoMaper;

    @PostMapping("/members")
    public ResponseEntity<?> createMembers(@RequestBody List<CreateMember> createMemberDtos) {
        try {
            List<Member> convertedCreateMembers = createMemberDtos.stream()
                    .map(memberDtoMapper::mapToEntity)
                    .toList();

            List<Member> savedMembers = memberService.addNewMembers(convertedCreateMembers);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(savedMembers.stream()
                            .map(memberDtoMapper::mapToDto)
                            .toList());
        } catch (BadRequestException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/members/{id}/payments")
    public ResponseEntity<?> createMemberPayments(@PathVariable String id, @RequestBody List<CreateMemberPayment> createMemberPayments) {
        try {
            List<MemberPayment> memberPayments = createMemberPayments.stream()
                    .map(createMemberPayment -> memberPaymentDtoMaper.mapToEntity(id, createMemberPayment))
                    .toList();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(memberService.createPayments(memberPayments).stream()
                            .map(memberPaymentDtoMaper::mapToDto)
                            .toList());
        } catch (BadRequestException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
}

