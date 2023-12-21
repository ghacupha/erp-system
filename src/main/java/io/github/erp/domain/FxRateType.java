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
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A FxRateType.
 */
@Entity
@Table(name = "fx_rate_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "fxratetype")
public class FxRateType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fx_rate_code", nullable = false, unique = true)
    private String fxRateCode;

    @NotNull
    @Column(name = "fx_rate_type", nullable = false, unique = true)
    private String fxRateType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "fx_rate_details")
    private String fxRateDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FxRateType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFxRateCode() {
        return this.fxRateCode;
    }

    public FxRateType fxRateCode(String fxRateCode) {
        this.setFxRateCode(fxRateCode);
        return this;
    }

    public void setFxRateCode(String fxRateCode) {
        this.fxRateCode = fxRateCode;
    }

    public String getFxRateType() {
        return this.fxRateType;
    }

    public FxRateType fxRateType(String fxRateType) {
        this.setFxRateType(fxRateType);
        return this;
    }

    public void setFxRateType(String fxRateType) {
        this.fxRateType = fxRateType;
    }

    public String getFxRateDetails() {
        return this.fxRateDetails;
    }

    public FxRateType fxRateDetails(String fxRateDetails) {
        this.setFxRateDetails(fxRateDetails);
        return this;
    }

    public void setFxRateDetails(String fxRateDetails) {
        this.fxRateDetails = fxRateDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FxRateType)) {
            return false;
        }
        return id != null && id.equals(((FxRateType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FxRateType{" +
            "id=" + getId() +
            ", fxRateCode='" + getFxRateCode() + "'" +
            ", fxRateType='" + getFxRateType() + "'" +
            ", fxRateDetails='" + getFxRateDetails() + "'" +
            "}";
    }
}
