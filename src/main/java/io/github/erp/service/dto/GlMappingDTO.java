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
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.GlMapping} entity.
 */
public class GlMappingDTO implements Serializable {

    private Long id;

    @NotNull
    private String subGLCode;

    private String subGLDescription;

    @NotNull
    private String mainGLCode;

    private String mainGLDescription;

    @NotNull
    private String glType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubGLCode() {
        return subGLCode;
    }

    public void setSubGLCode(String subGLCode) {
        this.subGLCode = subGLCode;
    }

    public String getSubGLDescription() {
        return subGLDescription;
    }

    public void setSubGLDescription(String subGLDescription) {
        this.subGLDescription = subGLDescription;
    }

    public String getMainGLCode() {
        return mainGLCode;
    }

    public void setMainGLCode(String mainGLCode) {
        this.mainGLCode = mainGLCode;
    }

    public String getMainGLDescription() {
        return mainGLDescription;
    }

    public void setMainGLDescription(String mainGLDescription) {
        this.mainGLDescription = mainGLDescription;
    }

    public String getGlType() {
        return glType;
    }

    public void setGlType(String glType) {
        this.glType = glType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GlMappingDTO)) {
            return false;
        }

        GlMappingDTO glMappingDTO = (GlMappingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, glMappingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GlMappingDTO{" +
            "id=" + getId() +
            ", subGLCode='" + getSubGLCode() + "'" +
            ", subGLDescription='" + getSubGLDescription() + "'" +
            ", mainGLCode='" + getMainGLCode() + "'" +
            ", mainGLDescription='" + getMainGLDescription() + "'" +
            ", glType='" + getGlType() + "'" +
            "}";
    }
}
