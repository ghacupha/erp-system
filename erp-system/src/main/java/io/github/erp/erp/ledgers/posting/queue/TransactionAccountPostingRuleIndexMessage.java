package io.github.erp.erp.ledgers.posting.queue;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TransactionAccountPostingRuleIndexMessage implements Serializable {

    private Long postingRuleId;
    private List<Long> templateIds = new ArrayList<>();
    private List<Long> conditionIds = new ArrayList<>();
    private PostingRuleIndexOperation operation;

    public Long getPostingRuleId() {
        return postingRuleId;
    }

    public void setPostingRuleId(Long postingRuleId) {
        this.postingRuleId = postingRuleId;
    }

    public List<Long> getTemplateIds() {
        return templateIds;
    }

    public void setTemplateIds(List<Long> templateIds) {
        this.templateIds = templateIds;
    }

    public List<Long> getConditionIds() {
        return conditionIds;
    }

    public void setConditionIds(List<Long> conditionIds) {
        this.conditionIds = conditionIds;
    }

    public PostingRuleIndexOperation getOperation() {
        return operation;
    }

    public void setOperation(PostingRuleIndexOperation operation) {
        this.operation = operation;
    }
}
