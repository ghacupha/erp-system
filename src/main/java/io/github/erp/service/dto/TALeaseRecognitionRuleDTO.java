package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.TALeaseRecognitionRule} entity.
 */
public class TALeaseRecognitionRuleDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private UUID identifier;

    private IFRS16LeaseContractDTO leaseContract;

    private TransactionAccountDTO debit;

    private TransactionAccountDTO credit;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public void setIdentifier(UUID identifier) {
        this.identifier = identifier;
    }

    public IFRS16LeaseContractDTO getLeaseContract() {
        return leaseContract;
    }

    public void setLeaseContract(IFRS16LeaseContractDTO leaseContract) {
        this.leaseContract = leaseContract;
    }

    public TransactionAccountDTO getDebit() {
        return debit;
    }

    public void setDebit(TransactionAccountDTO debit) {
        this.debit = debit;
    }

    public TransactionAccountDTO getCredit() {
        return credit;
    }

    public void setCredit(TransactionAccountDTO credit) {
        this.credit = credit;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TALeaseRecognitionRuleDTO)) {
            return false;
        }

        TALeaseRecognitionRuleDTO tALeaseRecognitionRuleDTO = (TALeaseRecognitionRuleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tALeaseRecognitionRuleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TALeaseRecognitionRuleDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", identifier='" + getIdentifier() + "'" +
            ", leaseContract=" + getLeaseContract() +
            ", debit=" + getDebit() +
            ", credit=" + getCredit() +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
