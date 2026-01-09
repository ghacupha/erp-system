package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.domain.enumeration.PostingRuleConditionOperator;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.TransactionAccountPostingRuleCondition} entity.
 */
public class TransactionAccountPostingRuleConditionDTO implements Serializable {

    private Long id;

    @NotNull
    private String conditionKey;

    @NotNull
    private PostingRuleConditionOperator conditionOperator;

    @NotNull
    private String conditionValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConditionKey() {
        return conditionKey;
    }

    public void setConditionKey(String conditionKey) {
        this.conditionKey = conditionKey;
    }

    public PostingRuleConditionOperator getConditionOperator() {
        return conditionOperator;
    }

    public void setConditionOperator(PostingRuleConditionOperator conditionOperator) {
        this.conditionOperator = conditionOperator;
    }

    public String getConditionValue() {
        return conditionValue;
    }

    public void setConditionValue(String conditionValue) {
        this.conditionValue = conditionValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionAccountPostingRuleConditionDTO)) {
            return false;
        }

        TransactionAccountPostingRuleConditionDTO transactionAccountPostingRuleConditionDTO = (TransactionAccountPostingRuleConditionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transactionAccountPostingRuleConditionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionAccountPostingRuleConditionDTO{" +
            "id=" + getId() +
            ", conditionKey='" + getConditionKey() + "'" +
            ", conditionOperator='" + getConditionOperator() + "'" +
            ", conditionValue='" + getConditionValue() + "'" +
            "}";
    }
}
