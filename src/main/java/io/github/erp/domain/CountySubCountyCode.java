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
 * A CountySubCountyCode.
 */
@Entity
@Table(name = "county_sub_county_code")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "countysubcountycode")
public class CountySubCountyCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 4, max = 4)
    @Pattern(regexp = "^\\d{4}$")
    @Column(name = "sub_county_code", length = 4, nullable = false, unique = true)
    private String subCountyCode;

    @NotNull
    @Column(name = "sub_county_name", nullable = false, unique = true)
    private String subCountyName;

    @NotNull
    @Size(min = 2, max = 2)
    @Pattern(regexp = "^\\d{2}$")
    @Column(name = "county_code", length = 2, nullable = false)
    private String countyCode;

    @NotNull
    @Column(name = "county_name", nullable = false)
    private String countyName;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CountySubCountyCode id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubCountyCode() {
        return this.subCountyCode;
    }

    public CountySubCountyCode subCountyCode(String subCountyCode) {
        this.setSubCountyCode(subCountyCode);
        return this;
    }

    public void setSubCountyCode(String subCountyCode) {
        this.subCountyCode = subCountyCode;
    }

    public String getSubCountyName() {
        return this.subCountyName;
    }

    public CountySubCountyCode subCountyName(String subCountyName) {
        this.setSubCountyName(subCountyName);
        return this;
    }

    public void setSubCountyName(String subCountyName) {
        this.subCountyName = subCountyName;
    }

    public String getCountyCode() {
        return this.countyCode;
    }

    public CountySubCountyCode countyCode(String countyCode) {
        this.setCountyCode(countyCode);
        return this;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public String getCountyName() {
        return this.countyName;
    }

    public CountySubCountyCode countyName(String countyName) {
        this.setCountyName(countyName);
        return this;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CountySubCountyCode)) {
            return false;
        }
        return id != null && id.equals(((CountySubCountyCode) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountySubCountyCode{" +
            "id=" + getId() +
            ", subCountyCode='" + getSubCountyCode() + "'" +
            ", subCountyName='" + getSubCountyName() + "'" +
            ", countyCode='" + getCountyCode() + "'" +
            ", countyName='" + getCountyName() + "'" +
            "}";
    }
}
