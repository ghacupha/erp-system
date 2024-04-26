package io.github.erp.service.dto;

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
