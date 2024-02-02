package io.github.erp.domain;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.2-SNAPSHOT
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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A InstitutionCode.
 */
@Entity
@Table(name = "institution_code")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "institutioncode")
public class InstitutionCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "institution_code", nullable = false, unique = true)
    private String institutionCode;

    @NotNull
    @Column(name = "institution_name", nullable = false, unique = true)
    private String institutionName;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "category")
    private String category;

    @Column(name = "institution_category")
    private String institutionCategory;

    @Column(name = "institution_ownership")
    private String institutionOwnership;

    @Column(name = "date_licensed")
    private LocalDate dateLicensed;

    @Column(name = "institution_status")
    private String institutionStatus;

    @ManyToMany
    @JoinTable(
        name = "rel_institution_code__placeholder",
        joinColumns = @JoinColumn(name = "institution_code_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InstitutionCode id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstitutionCode() {
        return this.institutionCode;
    }

    public InstitutionCode institutionCode(String institutionCode) {
        this.setInstitutionCode(institutionCode);
        return this;
    }

    public void setInstitutionCode(String institutionCode) {
        this.institutionCode = institutionCode;
    }

    public String getInstitutionName() {
        return this.institutionName;
    }

    public InstitutionCode institutionName(String institutionName) {
        this.setInstitutionName(institutionName);
        return this;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getShortName() {
        return this.shortName;
    }

    public InstitutionCode shortName(String shortName) {
        this.setShortName(shortName);
        return this;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCategory() {
        return this.category;
    }

    public InstitutionCode category(String category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInstitutionCategory() {
        return this.institutionCategory;
    }

    public InstitutionCode institutionCategory(String institutionCategory) {
        this.setInstitutionCategory(institutionCategory);
        return this;
    }

    public void setInstitutionCategory(String institutionCategory) {
        this.institutionCategory = institutionCategory;
    }

    public String getInstitutionOwnership() {
        return this.institutionOwnership;
    }

    public InstitutionCode institutionOwnership(String institutionOwnership) {
        this.setInstitutionOwnership(institutionOwnership);
        return this;
    }

    public void setInstitutionOwnership(String institutionOwnership) {
        this.institutionOwnership = institutionOwnership;
    }

    public LocalDate getDateLicensed() {
        return this.dateLicensed;
    }

    public InstitutionCode dateLicensed(LocalDate dateLicensed) {
        this.setDateLicensed(dateLicensed);
        return this;
    }

    public void setDateLicensed(LocalDate dateLicensed) {
        this.dateLicensed = dateLicensed;
    }

    public String getInstitutionStatus() {
        return this.institutionStatus;
    }

    public InstitutionCode institutionStatus(String institutionStatus) {
        this.setInstitutionStatus(institutionStatus);
        return this;
    }

    public void setInstitutionStatus(String institutionStatus) {
        this.institutionStatus = institutionStatus;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public InstitutionCode placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public InstitutionCode addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public InstitutionCode removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InstitutionCode)) {
            return false;
        }
        return id != null && id.equals(((InstitutionCode) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InstitutionCode{" +
            "id=" + getId() +
            ", institutionCode='" + getInstitutionCode() + "'" +
            ", institutionName='" + getInstitutionName() + "'" +
            ", shortName='" + getShortName() + "'" +
            ", category='" + getCategory() + "'" +
            ", institutionCategory='" + getInstitutionCategory() + "'" +
            ", institutionOwnership='" + getInstitutionOwnership() + "'" +
            ", dateLicensed='" + getDateLicensed() + "'" +
            ", institutionStatus='" + getInstitutionStatus() + "'" +
            "}";
    }
}
