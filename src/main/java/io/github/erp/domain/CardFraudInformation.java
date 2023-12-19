package io.github.erp.domain;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CardFraudInformation.
 */
@Entity
@Table(name = "card_fraud_information")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "cardfraudinformation")
public class CardFraudInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "reporting_date", nullable = false)
    private LocalDate reportingDate;

    @NotNull
    @Min(value = 0)
    @Column(name = "total_number_of_fraud_incidents", nullable = false)
    private Integer totalNumberOfFraudIncidents;

    @NotNull
    @Min(value = 0)
    @Column(name = "value_of_fraud_incedents_in_lcy", nullable = false)
    private Integer valueOfFraudIncedentsInLCY;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CardFraudInformation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getReportingDate() {
        return this.reportingDate;
    }

    public CardFraudInformation reportingDate(LocalDate reportingDate) {
        this.setReportingDate(reportingDate);
        return this;
    }

    public void setReportingDate(LocalDate reportingDate) {
        this.reportingDate = reportingDate;
    }

    public Integer getTotalNumberOfFraudIncidents() {
        return this.totalNumberOfFraudIncidents;
    }

    public CardFraudInformation totalNumberOfFraudIncidents(Integer totalNumberOfFraudIncidents) {
        this.setTotalNumberOfFraudIncidents(totalNumberOfFraudIncidents);
        return this;
    }

    public void setTotalNumberOfFraudIncidents(Integer totalNumberOfFraudIncidents) {
        this.totalNumberOfFraudIncidents = totalNumberOfFraudIncidents;
    }

    public Integer getValueOfFraudIncedentsInLCY() {
        return this.valueOfFraudIncedentsInLCY;
    }

    public CardFraudInformation valueOfFraudIncedentsInLCY(Integer valueOfFraudIncedentsInLCY) {
        this.setValueOfFraudIncedentsInLCY(valueOfFraudIncedentsInLCY);
        return this;
    }

    public void setValueOfFraudIncedentsInLCY(Integer valueOfFraudIncedentsInLCY) {
        this.valueOfFraudIncedentsInLCY = valueOfFraudIncedentsInLCY;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CardFraudInformation)) {
            return false;
        }
        return id != null && id.equals(((CardFraudInformation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardFraudInformation{" +
            "id=" + getId() +
            ", reportingDate='" + getReportingDate() + "'" +
            ", totalNumberOfFraudIncidents=" + getTotalNumberOfFraudIncidents() +
            ", valueOfFraudIncedentsInLCY=" + getValueOfFraudIncedentsInLCY() +
            "}";
    }
}
