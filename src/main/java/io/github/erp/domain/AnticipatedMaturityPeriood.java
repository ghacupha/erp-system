package io.github.erp.domain;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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
 * A AnticipatedMaturityPeriood.
 */
@Entity
@Table(name = "anticipated_maturity_periood")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "anticipatedmaturityperiood")
public class AnticipatedMaturityPeriood implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "anticipated_maturity_tenor_code", nullable = false, unique = true)
    private String anticipatedMaturityTenorCode;

    @NotNull
    @Column(name = "aniticipated_maturity_tenor_type", nullable = false, unique = true)
    private String aniticipatedMaturityTenorType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "anticipated_maturity_tenor_details")
    private String anticipatedMaturityTenorDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AnticipatedMaturityPeriood id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnticipatedMaturityTenorCode() {
        return this.anticipatedMaturityTenorCode;
    }

    public AnticipatedMaturityPeriood anticipatedMaturityTenorCode(String anticipatedMaturityTenorCode) {
        this.setAnticipatedMaturityTenorCode(anticipatedMaturityTenorCode);
        return this;
    }

    public void setAnticipatedMaturityTenorCode(String anticipatedMaturityTenorCode) {
        this.anticipatedMaturityTenorCode = anticipatedMaturityTenorCode;
    }

    public String getAniticipatedMaturityTenorType() {
        return this.aniticipatedMaturityTenorType;
    }

    public AnticipatedMaturityPeriood aniticipatedMaturityTenorType(String aniticipatedMaturityTenorType) {
        this.setAniticipatedMaturityTenorType(aniticipatedMaturityTenorType);
        return this;
    }

    public void setAniticipatedMaturityTenorType(String aniticipatedMaturityTenorType) {
        this.aniticipatedMaturityTenorType = aniticipatedMaturityTenorType;
    }

    public String getAnticipatedMaturityTenorDetails() {
        return this.anticipatedMaturityTenorDetails;
    }

    public AnticipatedMaturityPeriood anticipatedMaturityTenorDetails(String anticipatedMaturityTenorDetails) {
        this.setAnticipatedMaturityTenorDetails(anticipatedMaturityTenorDetails);
        return this;
    }

    public void setAnticipatedMaturityTenorDetails(String anticipatedMaturityTenorDetails) {
        this.anticipatedMaturityTenorDetails = anticipatedMaturityTenorDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnticipatedMaturityPeriood)) {
            return false;
        }
        return id != null && id.equals(((AnticipatedMaturityPeriood) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnticipatedMaturityPeriood{" +
            "id=" + getId() +
            ", anticipatedMaturityTenorCode='" + getAnticipatedMaturityTenorCode() + "'" +
            ", aniticipatedMaturityTenorType='" + getAniticipatedMaturityTenorType() + "'" +
            ", anticipatedMaturityTenorDetails='" + getAnticipatedMaturityTenorDetails() + "'" +
            "}";
    }
}
