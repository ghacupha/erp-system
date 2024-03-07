package io.github.erp.domain;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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
import java.math.BigDecimal;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RouDepreciationEntry.
 */
@Entity
@Table(name = "rou_depreciation_entry")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "roudepreciationentry")
public class RouDepreciationEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "depreciation_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal depreciationAmount;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "outstanding_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal outstandingAmount;

    @Column(name = "rou_asset_identifier")
    private UUID rouAssetIdentifier;

    @NotNull
    @Column(name = "rou_depreciation_identifier", nullable = false, unique = true)
    private UUID rouDepreciationIdentifier;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RouDepreciationEntry id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public RouDepreciationEntry description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getDepreciationAmount() {
        return this.depreciationAmount;
    }

    public RouDepreciationEntry depreciationAmount(BigDecimal depreciationAmount) {
        this.setDepreciationAmount(depreciationAmount);
        return this;
    }

    public void setDepreciationAmount(BigDecimal depreciationAmount) {
        this.depreciationAmount = depreciationAmount;
    }

    public BigDecimal getOutstandingAmount() {
        return this.outstandingAmount;
    }

    public RouDepreciationEntry outstandingAmount(BigDecimal outstandingAmount) {
        this.setOutstandingAmount(outstandingAmount);
        return this;
    }

    public void setOutstandingAmount(BigDecimal outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public UUID getRouAssetIdentifier() {
        return this.rouAssetIdentifier;
    }

    public RouDepreciationEntry rouAssetIdentifier(UUID rouAssetIdentifier) {
        this.setRouAssetIdentifier(rouAssetIdentifier);
        return this;
    }

    public void setRouAssetIdentifier(UUID rouAssetIdentifier) {
        this.rouAssetIdentifier = rouAssetIdentifier;
    }

    public UUID getRouDepreciationIdentifier() {
        return this.rouDepreciationIdentifier;
    }

    public RouDepreciationEntry rouDepreciationIdentifier(UUID rouDepreciationIdentifier) {
        this.setRouDepreciationIdentifier(rouDepreciationIdentifier);
        return this;
    }

    public void setRouDepreciationIdentifier(UUID rouDepreciationIdentifier) {
        this.rouDepreciationIdentifier = rouDepreciationIdentifier;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RouDepreciationEntry)) {
            return false;
        }
        return id != null && id.equals(((RouDepreciationEntry) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RouDepreciationEntry{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", depreciationAmount=" + getDepreciationAmount() +
            ", outstandingAmount=" + getOutstandingAmount() +
            ", rouAssetIdentifier='" + getRouAssetIdentifier() + "'" +
            ", rouDepreciationIdentifier='" + getRouDepreciationIdentifier() + "'" +
            "}";
    }
}
