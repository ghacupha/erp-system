package io.github.erp.service.dto;

/*-
 * Erp System - Mark VII No 1 (Gideon Series) Server ver 1.5.5
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
