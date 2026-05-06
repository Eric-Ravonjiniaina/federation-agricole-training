package org.hei_school.federation_agricole.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CashAccount extends FinancialAccount {
}