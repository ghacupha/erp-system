package io.github.erp.service.dto;

/*-
 * Erp System - Mark V No 7 (Ehud Series) Server ver 1.5.0
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
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.AcademicQualification} entity.
 */
public class AcademicQualificationDTO implements Serializable {

    private Long id;

    @NotNull
    private String academicQualificationsCode;

    @NotNull
    private String academicQualificationType;

    @Lob
    private String academicQualificationTypeDetail;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAcademicQualificationsCode() {
        return academicQualificationsCode;
    }

    public void setAcademicQualificationsCode(String academicQualificationsCode) {
        this.academicQualificationsCode = academicQualificationsCode;
    }

    public String getAcademicQualificationType() {
        return academicQualificationType;
    }

    public void setAcademicQualificationType(String academicQualificationType) {
        this.academicQualificationType = academicQualificationType;
    }

    public String getAcademicQualificationTypeDetail() {
        return academicQualificationTypeDetail;
    }

    public void setAcademicQualificationTypeDetail(String academicQualificationTypeDetail) {
        this.academicQualificationTypeDetail = academicQualificationTypeDetail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AcademicQualificationDTO)) {
            return false;
        }

        AcademicQualificationDTO academicQualificationDTO = (AcademicQualificationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, academicQualificationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AcademicQualificationDTO{" +
            "id=" + getId() +
            ", academicQualificationsCode='" + getAcademicQualificationsCode() + "'" +
            ", academicQualificationType='" + getAcademicQualificationType() + "'" +
            ", academicQualificationTypeDetail='" + getAcademicQualificationTypeDetail() + "'" +
            "}";
    }
}
