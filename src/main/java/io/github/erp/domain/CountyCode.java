package io.github.erp.domain;

/*-
 * Erp System - Mark III No 4 (Caleb Series) Server ver 0.1.5-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CountyCode.
 */
@Entity
@Table(name = "county_code")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "countycode")
public class CountyCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "county_code", nullable = false)
    private Integer countyCode;

    @NotNull
    @Column(name = "county_name", nullable = false)
    private String countyName;

    @NotNull
    @Column(name = "sub_county_code", nullable = false, unique = true)
    private Integer subCountyCode;

    @NotNull
    @Column(name = "sub_county_name", nullable = false, unique = true)
    private String subCountyName;

    @ManyToMany
    @JoinTable(
        name = "rel_county_code__placeholder",
        joinColumns = @JoinColumn(name = "county_code_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CountyCode id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCountyCode() {
        return this.countyCode;
    }

    public CountyCode countyCode(Integer countyCode) {
        this.setCountyCode(countyCode);
        return this;
    }

    public void setCountyCode(Integer countyCode) {
        this.countyCode = countyCode;
    }

    public String getCountyName() {
        return this.countyName;
    }

    public CountyCode countyName(String countyName) {
        this.setCountyName(countyName);
        return this;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public Integer getSubCountyCode() {
        return this.subCountyCode;
    }

    public CountyCode subCountyCode(Integer subCountyCode) {
        this.setSubCountyCode(subCountyCode);
        return this;
    }

    public void setSubCountyCode(Integer subCountyCode) {
        this.subCountyCode = subCountyCode;
    }

    public String getSubCountyName() {
        return this.subCountyName;
    }

    public CountyCode subCountyName(String subCountyName) {
        this.setSubCountyName(subCountyName);
        return this;
    }

    public void setSubCountyName(String subCountyName) {
        this.subCountyName = subCountyName;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public CountyCode placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public CountyCode addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public CountyCode removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CountyCode)) {
            return false;
        }
        return id != null && id.equals(((CountyCode) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountyCode{" +
            "id=" + getId() +
            ", countyCode=" + getCountyCode() +
            ", countyName='" + getCountyName() + "'" +
            ", subCountyCode=" + getSubCountyCode() +
            ", subCountyName='" + getSubCountyName() + "'" +
            "}";
    }
}
