package org.hei_school.federation_agricole.controller.mapper;

import org.hei_school.federation_agricole.controller.dto.Bank;
import org.hei_school.federation_agricole.controller.dto.FinancialAccount;
import org.hei_school.federation_agricole.controller.dto.MobileBankingService;
import org.hei_school.federation_agricole.entity.BankAccount;
import org.hei_school.federation_agricole.entity.CashAccount;
import org.hei_school.federation_agricole.entity.MobileBankingAccount;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

import static java.time.LocalDate.now;

@Component
public class FinancialAccountDtoMapper {
    public FinancialAccount mapToDto(org.hei_school.federation_agricole.entity.FinancialAccount financialAccount, LocalDate at) {
        LocalDate balanceAt = at == null ? now() : at;
        if (financialAccount instanceof CashAccount cashAccount) {
            return org.hei_school.federation_agricole.controller.dto.CashAccount.builder()
                    .id(cashAccount.getId())
                    .amount(cashAccount.getBalanceAt(balanceAt))
                    .build();
        } else if (financialAccount instanceof BankAccount bankAccount) {
            return org.hei_school.federation_agricole.controller.dto.BankAccount.builder()
                    .id(bankAccount.getId())
                    .holderName(bankAccount.getHolderName())
                    .bankName(bankAccount.getBankName() == null ? null : Bank.valueOf(bankAccount.getBankName().name()))
                    .bankCode(bankAccount.getBankCode())
                    .bankBranchCode(bankAccount.getBranchCode())
                    .bankAccountNumber(bankAccount.getAccountNumber())
                    .bankAccountKey(bankAccount.getAccountKey())
                    .amount(bankAccount.getBalanceAt(balanceAt))
                    .build();
        } else if (financialAccount instanceof MobileBankingAccount mobileBankingAccount) {
            return org.hei_school.federation_agricole.controller.dto.MobileBankingAccount.builder()
                    .id(mobileBankingAccount.getId())
                    .holderName(mobileBankingAccount.getHolderName())
                    .mobileNumber(mobileBankingAccount.getMobileNumber())
                    .mobileBankingService(mobileBankingAccount.getMobileBankingService() == null ? null : MobileBankingService.valueOf(mobileBankingAccount.getMobileBankingService().name()))
                    .amount(mobileBankingAccount.getBalanceAt(balanceAt))
                    .build();
        }
        throw new IllegalArgumentException("Unknown financial account type " + financialAccount.getClass().getName());
    }

    public FinancialAccount mapToDto(org.hei_school.federation_agricole.entity.FinancialAccount financialAccount) {
        return  mapToDto(financialAccount, now());
    }

}
