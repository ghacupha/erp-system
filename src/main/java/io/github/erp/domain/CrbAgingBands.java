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
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CrbAgingBands.
 */
@Entity
@Table(name = "crb_aging_bands")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "crbagingbands")
public class CrbAgingBands implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "aging_band_category_code", nullable = false, unique = true)
    private String agingBandCategoryCode;

    @NotNull
    @Column(name = "aging_band_category", nullable = false, unique = true)
    private String agingBandCategory;

    @NotNull
    @Column(name = "aging_band_category_details", nullable = false)
    private String agingBandCategoryDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CrbAgingBands id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgingBandCategoryCode() {
        return this.agingBandCategoryCode;
    }

    public CrbAgingBands agingBandCategoryCode(String agingBandCategoryCode) {
        this.setAgingBandCategoryCode(agingBandCategoryCode);
        return this;
    }

    public void setAgingBandCategoryCode(String agingBandCategoryCode) {
        this.agingBandCategoryCode = agingBandCategoryCode;
    }

    public String getAgingBandCategory() {
        return this.agingBandCategory;
    }

    public CrbAgingBands agingBandCategory(String agingBandCategory) {
        this.setAgingBandCategory(agingBandCategory);
        return this;
    }

    public void setAgingBandCategory(String agingBandCategory) {
        this.agingBandCategory = agingBandCategory;
    }

    public String getAgingBandCategoryDetails() {
        return this.agingBandCategoryDetails;
    }

    public CrbAgingBands agingBandCategoryDetails(String agingBandCategoryDetails) {
        this.setAgingBandCategoryDetails(agingBandCategoryDetails);
        return this;
    }

    public void setAgingBandCategoryDetails(String agingBandCategoryDetails) {
        this.agingBandCategoryDetails = agingBandCategoryDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CrbAgingBands)) {
            return false;
        }
        return id != null && id.equals(((CrbAgingBands) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbAgingBands{" +
            "id=" + getId() +
            ", agingBandCategoryCode='" + getAgingBandCategoryCode() + "'" +
            ", agingBandCategory='" + getAgingBandCategory() + "'" +
            ", agingBandCategoryDetails='" + getAgingBandCategoryDetails() + "'" +
            "}";
    }
}
