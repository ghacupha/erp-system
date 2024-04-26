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
 * A DTO for the {@link io.github.erp.domain.Placeholder} entity.
 */
public class PlaceholderDTO implements Serializable {

    private Long id;

    @NotNull
    private String description;

    private String token;

    private String fileUploadToken;

    private String compilationToken;

    private PlaceholderDTO containingPlaceholder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFileUploadToken() {
        return fileUploadToken;
    }

    public void setFileUploadToken(String fileUploadToken) {
        this.fileUploadToken = fileUploadToken;
    }

    public String getCompilationToken() {
        return compilationToken;
    }

    public void setCompilationToken(String compilationToken) {
        this.compilationToken = compilationToken;
    }

    public PlaceholderDTO getContainingPlaceholder() {
        return containingPlaceholder;
    }

    public void setContainingPlaceholder(PlaceholderDTO containingPlaceholder) {
        this.containingPlaceholder = containingPlaceholder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlaceholderDTO)) {
            return false;
        }

        PlaceholderDTO placeholderDTO = (PlaceholderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, placeholderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlaceholderDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", token='" + getToken() + "'" +
            ", fileUploadToken='" + getFileUploadToken() + "'" +
            ", compilationToken='" + getCompilationToken() + "'" +
            ", containingPlaceholder=" + getContainingPlaceholder() +
            "}";
    }
}
