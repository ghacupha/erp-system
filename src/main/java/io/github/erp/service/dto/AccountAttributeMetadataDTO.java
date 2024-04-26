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
import io.github.erp.domain.enumeration.MandatoryFieldFlagTypes;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.AccountAttributeMetadata} entity.
 */
public class AccountAttributeMetadataDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer precedence;

    @NotNull
    private String columnName;

    @NotNull
    private String shortName;

    @Lob
    private String detailedDefinition;

    @NotNull
    private String dataType;

    private Integer length;

    private String columnIndex;

    @NotNull
    private MandatoryFieldFlagTypes mandatoryFieldFlag;

    @Lob
    private String businessValidation;

    @Lob
    private String technicalValidation;

    private String dbColumnName;

    private Integer metadataVersion;

    private GdiMasterDataIndexDTO standardInputTemplate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPrecedence() {
        return precedence;
    }

    public void setPrecedence(Integer precedence) {
        this.precedence = precedence;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDetailedDefinition() {
        return detailedDefinition;
    }

    public void setDetailedDefinition(String detailedDefinition) {
        this.detailedDefinition = detailedDefinition;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(String columnIndex) {
        this.columnIndex = columnIndex;
    }

    public MandatoryFieldFlagTypes getMandatoryFieldFlag() {
        return mandatoryFieldFlag;
    }

    public void setMandatoryFieldFlag(MandatoryFieldFlagTypes mandatoryFieldFlag) {
        this.mandatoryFieldFlag = mandatoryFieldFlag;
    }

    public String getBusinessValidation() {
        return businessValidation;
    }

    public void setBusinessValidation(String businessValidation) {
        this.businessValidation = businessValidation;
    }

    public String getTechnicalValidation() {
        return technicalValidation;
    }

    public void setTechnicalValidation(String technicalValidation) {
        this.technicalValidation = technicalValidation;
    }

    public String getDbColumnName() {
        return dbColumnName;
    }

    public void setDbColumnName(String dbColumnName) {
        this.dbColumnName = dbColumnName;
    }

    public Integer getMetadataVersion() {
        return metadataVersion;
    }

    public void setMetadataVersion(Integer metadataVersion) {
        this.metadataVersion = metadataVersion;
    }

    public GdiMasterDataIndexDTO getStandardInputTemplate() {
        return standardInputTemplate;
    }

    public void setStandardInputTemplate(GdiMasterDataIndexDTO standardInputTemplate) {
        this.standardInputTemplate = standardInputTemplate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountAttributeMetadataDTO)) {
            return false;
        }

        AccountAttributeMetadataDTO accountAttributeMetadataDTO = (AccountAttributeMetadataDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, accountAttributeMetadataDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountAttributeMetadataDTO{" +
            "id=" + getId() +
            ", precedence=" + getPrecedence() +
            ", columnName='" + getColumnName() + "'" +
            ", shortName='" + getShortName() + "'" +
            ", detailedDefinition='" + getDetailedDefinition() + "'" +
            ", dataType='" + getDataType() + "'" +
            ", length=" + getLength() +
            ", columnIndex='" + getColumnIndex() + "'" +
            ", mandatoryFieldFlag='" + getMandatoryFieldFlag() + "'" +
            ", businessValidation='" + getBusinessValidation() + "'" +
            ", technicalValidation='" + getTechnicalValidation() + "'" +
            ", dbColumnName='" + getDbColumnName() + "'" +
            ", metadataVersion=" + getMetadataVersion() +
            ", standardInputTemplate=" + getStandardInputTemplate() +
            "}";
    }
}
