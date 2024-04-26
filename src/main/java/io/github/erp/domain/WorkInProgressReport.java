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
 * A WorkInProgressReport.
 */
@Entity
@Table(name = "work_in_progress_report")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "workinprogressreport")
public class WorkInProgressReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "project_title")
    private String projectTitle;

    @Column(name = "dealer_name")
    private String dealerName;

    @Column(name = "number_of_items")
    private Integer numberOfItems;

    @Column(name = "instalment_amount", precision = 21, scale = 2)
    private BigDecimal instalmentAmount;

    @Column(name = "transfer_amount", precision = 21, scale = 2)
    private BigDecimal transferAmount;

    @Column(name = "outstanding_amount", precision = 21, scale = 2)
    private BigDecimal outstandingAmount;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WorkInProgressReport id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectTitle() {
        return this.projectTitle;
    }

    public WorkInProgressReport projectTitle(String projectTitle) {
        this.setProjectTitle(projectTitle);
        return this;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getDealerName() {
        return this.dealerName;
    }

    public WorkInProgressReport dealerName(String dealerName) {
        this.setDealerName(dealerName);
        return this;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public Integer getNumberOfItems() {
        return this.numberOfItems;
    }

    public WorkInProgressReport numberOfItems(Integer numberOfItems) {
        this.setNumberOfItems(numberOfItems);
        return this;
    }

    public void setNumberOfItems(Integer numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public BigDecimal getInstalmentAmount() {
        return this.instalmentAmount;
    }

    public WorkInProgressReport instalmentAmount(BigDecimal instalmentAmount) {
        this.setInstalmentAmount(instalmentAmount);
        return this;
    }

    public void setInstalmentAmount(BigDecimal instalmentAmount) {
        this.instalmentAmount = instalmentAmount;
    }

    public BigDecimal getTransferAmount() {
        return this.transferAmount;
    }

    public WorkInProgressReport transferAmount(BigDecimal transferAmount) {
        this.setTransferAmount(transferAmount);
        return this;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public BigDecimal getOutstandingAmount() {
        return this.outstandingAmount;
    }

    public WorkInProgressReport outstandingAmount(BigDecimal outstandingAmount) {
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
        if (!(o instanceof WorkInProgressReport)) {
            return false;
        }
        return id != null && id.equals(((WorkInProgressReport) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkInProgressReport{" +
            "id=" + getId() +
            ", projectTitle='" + getProjectTitle() + "'" +
            ", dealerName='" + getDealerName() + "'" +
            ", numberOfItems=" + getNumberOfItems() +
            ", instalmentAmount=" + getInstalmentAmount() +
            ", transferAmount=" + getTransferAmount() +
            ", outstandingAmount=" + getOutstandingAmount() +
            "}";
    }
}
