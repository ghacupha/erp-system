package io.github.erp.domain;

/*-
 * Erp System - Mark VII No 1 (Gideon Series) Server ver 1.5.5
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
 * A BusinessSegmentTypes.
 */
@Entity
@Table(name = "business_segment_types")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "businesssegmenttypes")
public class BusinessSegmentTypes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "business_economic_segment_code", nullable = false, unique = true)
    private String businessEconomicSegmentCode;

    @NotNull
    @Column(name = "business_economic_segment", nullable = false)
    private String businessEconomicSegment;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "details")
    private String details;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BusinessSegmentTypes id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusinessEconomicSegmentCode() {
        return this.businessEconomicSegmentCode;
    }

    public BusinessSegmentTypes businessEconomicSegmentCode(String businessEconomicSegmentCode) {
        this.setBusinessEconomicSegmentCode(businessEconomicSegmentCode);
        return this;
    }

    public void setBusinessEconomicSegmentCode(String businessEconomicSegmentCode) {
        this.businessEconomicSegmentCode = businessEconomicSegmentCode;
    }

    public String getBusinessEconomicSegment() {
        return this.businessEconomicSegment;
    }

    public BusinessSegmentTypes businessEconomicSegment(String businessEconomicSegment) {
        this.setBusinessEconomicSegment(businessEconomicSegment);
        return this;
    }

    public void setBusinessEconomicSegment(String businessEconomicSegment) {
        this.businessEconomicSegment = businessEconomicSegment;
    }

    public String getDetails() {
        return this.details;
    }

    public BusinessSegmentTypes details(String details) {
        this.setDetails(details);
        return this;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BusinessSegmentTypes)) {
            return false;
        }
        return id != null && id.equals(((BusinessSegmentTypes) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BusinessSegmentTypes{" +
            "id=" + getId() +
            ", businessEconomicSegmentCode='" + getBusinessEconomicSegmentCode() + "'" +
            ", businessEconomicSegment='" + getBusinessEconomicSegment() + "'" +
            ", details='" + getDetails() + "'" +
            "}";
    }
}
