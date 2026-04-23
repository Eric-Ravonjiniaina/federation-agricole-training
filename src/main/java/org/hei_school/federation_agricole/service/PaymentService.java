package org.hei_school.federation_agricole.service;

import org.hei_school.federation_agricole.dto.CreateMemberPaymentDTO;
import org.hei_school.federation_agricole.entity.CollectivityTransaction;
import org.hei_school.federation_agricole.entity.FinancialAccount;
import org.hei_school.federation_agricole.entity.MemberEntity;
import org.hei_school.federation_agricole.entity.MemberPayment;
import org.hei_school.federation_agricole.repository.AccountRepository;
import org.hei_school.federation_agricole.repository.MemberPaymentRepository;
import org.hei_school.federation_agricole.repository.MemberRepository;
import org.hei_school.federation_agricole.repository.TransactionRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class PaymentService {
    private final MemberPaymentRepository memberPaymentRepository;
    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public PaymentService(MemberPaymentRepository memberPaymentRepository, MemberRepository memberRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.memberPaymentRepository = memberPaymentRepository;
        this.memberRepository = memberRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public MemberPayment processPayment(String memberId, CreateMemberPaymentDTO dto) {
        Optional<MemberEntity> memberOpt = memberRepository.findById(memberId);
        if (memberOpt.isEmpty()) {
            throw new RuntimeException("Le membre avec l'ID " + memberId + " n'existe pas.");
        }
        MemberEntity member = memberOpt.get();

        Optional<FinancialAccount> accountOpt = accountRepository.findById(dto.accountCreditedIdentifier());
        if (accountOpt.isEmpty()) {
            throw new RuntimeException("Compte financier introuvable.");
        }
        FinancialAccount account = accountOpt.get();

        MemberPayment payment = new MemberPayment();
        payment.setAmount(dto.amount());
        payment.setMember(member);
        payment.setAccountCredited(account);
        payment.setPayment(dto.paymentMode());
        payment.setCreationDate(LocalDate.now());

        account.setAmount(account.getAmount() + dto.amount());
        accountRepository.save(account);

        CollectivityTransaction tx = new CollectivityTransaction();
        tx.setAmount(dto.amount().doubleValue());
        tx.setAccountCredited(account);
        tx.setMemberDebited(member);
        tx.setPaymentMode(String.valueOf(dto.paymentMode()));
        tx.setCreationDate(LocalDate.now());

        transactionRepository.save(tx);

        return memberPaymentRepository.save(payment);

    }
}