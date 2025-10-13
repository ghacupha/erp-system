package io.github.erp.domain;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
 * A AcademicQualification.
 */
@Entity
@Table(name = "academic_qualification")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "academicqualification")
public class AcademicQualification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "academic_qualifications_code", nullable = false, unique = true)
    private String academicQualificationsCode;

    @NotNull
    @Column(name = "academic_qualification_type", nullable = false, unique = true)
    private String academicQualificationType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "academic_qualification_type_detail")
    private String academicQualificationTypeDetail;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AcademicQualification id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAcademicQualificationsCode() {
        return this.academicQualificationsCode;
    }

    public AcademicQualification academicQualificationsCode(String academicQualificationsCode) {
        this.setAcademicQualificationsCode(academicQualificationsCode);
        return this;
    }

    public void setAcademicQualificationsCode(String academicQualificationsCode) {
        this.academicQualificationsCode = academicQualificationsCode;
    }

    public String getAcademicQualificationType() {
        return this.academicQualificationType;
    }

    public AcademicQualification academicQualificationType(String academicQualificationType) {
        this.setAcademicQualificationType(academicQualificationType);
        return this;
    }

    public void setAcademicQualificationType(String academicQualificationType) {
        this.academicQualificationType = academicQualificationType;
    }

    public String getAcademicQualificationTypeDetail() {
        return this.academicQualificationTypeDetail;
    }

    public AcademicQualification academicQualificationTypeDetail(String academicQualificationTypeDetail) {
        this.setAcademicQualificationTypeDetail(academicQualificationTypeDetail);
        return this;
    }

    public void setAcademicQualificationTypeDetail(String academicQualificationTypeDetail) {
        this.academicQualificationTypeDetail = academicQualificationTypeDetail;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AcademicQualification)) {
            return false;
        }
        return id != null && id.equals(((AcademicQualification) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AcademicQualification{" +
            "id=" + getId() +
            ", academicQualificationsCode='" + getAcademicQualificationsCode() + "'" +
            ", academicQualificationType='" + getAcademicQualificationType() + "'" +
            ", academicQualificationTypeDetail='" + getAcademicQualificationTypeDetail() + "'" +
            "}";
    }
}
