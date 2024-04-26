package io.github.erp.domain;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WorkInProgressOverview.
 */
@Entity
@Table(name = "work_in_progress_overview")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "workinprogressoverview")
public class WorkInProgressOverview implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "number_of_items")
    private Long numberOfItems;

    @Column(name = "instalment_amount", precision = 21, scale = 2)
    private BigDecimal instalmentAmount;

    @Column(name = "total_transfer_amount", precision = 21, scale = 2)
    private BigDecimal totalTransferAmount;

    @Column(name = "outstanding_amount", precision = 21, scale = 2)
    private BigDecimal outstandingAmount;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WorkInProgressOverview id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumberOfItems() {
        return this.numberOfItems;
    }

    public WorkInProgressOverview numberOfItems(Long numberOfItems) {
        this.setNumberOfItems(numberOfItems);
        return this;
    }

    public void setNumberOfItems(Long numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public BigDecimal getInstalmentAmount() {
        return this.instalmentAmount;
    }

    public WorkInProgressOverview instalmentAmount(BigDecimal instalmentAmount) {
        this.setInstalmentAmount(instalmentAmount);
        return this;
    }

    public void setInstalmentAmount(BigDecimal instalmentAmount) {
        this.instalmentAmount = instalmentAmount;
    }

    public BigDecimal getTotalTransferAmount() {
        return this.totalTransferAmount;
    }

    public WorkInProgressOverview totalTransferAmount(BigDecimal totalTransferAmount) {
        this.setTotalTransferAmount(totalTransferAmount);
        return this;
    }

    public void setTotalTransferAmount(BigDecimal totalTransferAmount) {
        this.totalTransferAmount = totalTransferAmount;
    }

    public BigDecimal getOutstandingAmount() {
        return this.outstandingAmount;
    }

    public WorkInProgressOverview outstandingAmount(BigDecimal outstandingAmount) {
        this.setOutstandingAmount(outstandingAmount);
        return this;
    }

    public void setOutstandingAmount(BigDecimal outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkInProgressOverview)) {
            return false;
        }
        return id != null && id.equals(((WorkInProgressOverview) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkInProgressOverview{" +
            "id=" + getId() +
            ", numberOfItems=" + getNumberOfItems() +
            ", instalmentAmount=" + getInstalmentAmount() +
            ", totalTransferAmount=" + getTotalTransferAmount() +
            ", outstandingAmount=" + getOutstandingAmount() +
            "}";
    }
}
