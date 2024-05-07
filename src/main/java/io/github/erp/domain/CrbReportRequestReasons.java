package io.github.erp.domain;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A CrbReportRequestReasons.
 */
@Entity
@Table(name = "crb_report_request_reasons")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "crbreportrequestreasons")
public class CrbReportRequestReasons implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "credit_report_request_reason_type_code", nullable = false, unique = true)
    private String creditReportRequestReasonTypeCode;

    @NotNull
    @Column(name = "credit_report_request_reason_type", nullable = false)
    private String creditReportRequestReasonType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "credit_report_request_details")
    private String creditReportRequestDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CrbReportRequestReasons id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreditReportRequestReasonTypeCode() {
        return this.creditReportRequestReasonTypeCode;
    }

    public CrbReportRequestReasons creditReportRequestReasonTypeCode(String creditReportRequestReasonTypeCode) {
        this.setCreditReportRequestReasonTypeCode(creditReportRequestReasonTypeCode);
        return this;
    }

    public void setCreditReportRequestReasonTypeCode(String creditReportRequestReasonTypeCode) {
        this.creditReportRequestReasonTypeCode = creditReportRequestReasonTypeCode;
    }

    public String getCreditReportRequestReasonType() {
        return this.creditReportRequestReasonType;
    }

    public CrbReportRequestReasons creditReportRequestReasonType(String creditReportRequestReasonType) {
        this.setCreditReportRequestReasonType(creditReportRequestReasonType);
        return this;
    }

    public void setCreditReportRequestReasonType(String creditReportRequestReasonType) {
        this.creditReportRequestReasonType = creditReportRequestReasonType;
    }

    public String getCreditReportRequestDetails() {
        return this.creditReportRequestDetails;
    }

    public CrbReportRequestReasons creditReportRequestDetails(String creditReportRequestDetails) {
        this.setCreditReportRequestDetails(creditReportRequestDetails);
        return this;
    }

    public void setCreditReportRequestDetails(String creditReportRequestDetails) {
        this.creditReportRequestDetails = creditReportRequestDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CrbReportRequestReasons)) {
            return false;
        }
        return id != null && id.equals(((CrbReportRequestReasons) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbReportRequestReasons{" +
            "id=" + getId() +
            ", creditReportRequestReasonTypeCode='" + getCreditReportRequestReasonTypeCode() + "'" +
            ", creditReportRequestReasonType='" + getCreditReportRequestReasonType() + "'" +
            ", creditReportRequestDetails='" + getCreditReportRequestDetails() + "'" +
            "}";
    }
}
