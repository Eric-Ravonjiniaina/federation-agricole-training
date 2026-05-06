package org.hei_school.federation_agricole.service;

import org.hei_school.federation_agricole.dto.CreateMemberPaymentDTO;
import org.hei_school.federation_agricole.entity.*;
import org.hei_school.federation_agricole.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {

    private final MemberPaymentRepository memberPaymentRepository;
    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public PaymentService(MemberPaymentRepository memberPaymentRepository,
                          MemberRepository memberRepository,
                          AccountRepository accountRepository,
                          TransactionRepository transactionRepository) {
        this.memberPaymentRepository = memberPaymentRepository;
        this.memberRepository = memberRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public MemberPayment processPayment(String memberId, CreateMemberPaymentDTO dto) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found: " + memberId));

        FinancialAccount account = accountRepository.findById(dto.accountCreditedIdentifier())
                .orElseThrow(() -> new RuntimeException("Account not found: " + dto.accountCreditedIdentifier()));

        // Créer le paiement
        MemberPayment payment = new MemberPayment();
        payment.setId(UUID.randomUUID().toString());
        payment.setAmount(dto.amount());
        payment.setMember(member);
        payment.setAccountCredited(account);
        payment.setPayment(dto.paymentMode());
        payment.setCreationDate(LocalDate.now());

        // Mettre à jour le solde du compte
        account.setAmount(account.getAmount() + dto.amount());
        accountRepository.save(account);

        // Créer la transaction associée
        CollectivityTransaction tx = new CollectivityTransaction();
        tx.setId(UUID.randomUUID().toString());
        tx.setAmount(dto.amount());
        tx.setAccountCredited(account);
        tx.setMemberDebited(member);
        tx.setPaymentMode(dto.paymentMode().name());
        tx.setCreationDate(LocalDate.now());
        // collectivity_id récupéré depuis le compte
        transactionRepository.save(tx);

        return memberPaymentRepository.save(payment);
    }
}