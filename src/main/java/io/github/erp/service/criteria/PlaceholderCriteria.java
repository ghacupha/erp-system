package io.github.erp.service.criteria;

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
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.Placeholder} entity. This class is used
 * in {@link io.github.erp.web.rest.PlaceholderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /placeholders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PlaceholderCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private StringFilter token;

    private StringFilter fileUploadToken;

    private StringFilter compilationToken;

    private LongFilter containingPlaceholderId;

    private Boolean distinct;

    public PlaceholderCriteria() {}

    public PlaceholderCriteria(PlaceholderCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.token = other.token == null ? null : other.token.copy();
        this.fileUploadToken = other.fileUploadToken == null ? null : other.fileUploadToken.copy();
        this.compilationToken = other.compilationToken == null ? null : other.compilationToken.copy();
        this.containingPlaceholderId = other.containingPlaceholderId == null ? null : other.containingPlaceholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PlaceholderCriteria copy() {
        return new PlaceholderCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getToken() {
        return token;
    }

    public StringFilter token() {
        if (token == null) {
            token = new StringFilter();
        }
        return token;
    }

    public void setToken(StringFilter token) {
        this.token = token;
    }

    public StringFilter getFileUploadToken() {
        return fileUploadToken;
    }

    public StringFilter fileUploadToken() {
        if (fileUploadToken == null) {
            fileUploadToken = new StringFilter();
        }
        return fileUploadToken;
    }

    public void setFileUploadToken(StringFilter fileUploadToken) {
        this.fileUploadToken = fileUploadToken;
    }

    public StringFilter getCompilationToken() {
        return compilationToken;
    }

    public StringFilter compilationToken() {
        if (compilationToken == null) {
            compilationToken = new StringFilter();
        }
        return compilationToken;
    }

    public void setCompilationToken(StringFilter compilationToken) {
        this.compilationToken = compilationToken;
    }

    public LongFilter getContainingPlaceholderId() {
        return containingPlaceholderId;
    }

    public LongFilter containingPlaceholderId() {
        if (containingPlaceholderId == null) {
            containingPlaceholderId = new LongFilter();
        }
        return containingPlaceholderId;
    }

    public void setContainingPlaceholderId(LongFilter containingPlaceholderId) {
        this.containingPlaceholderId = containingPlaceholderId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PlaceholderCriteria that = (PlaceholderCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(token, that.token) &&
            Objects.equals(fileUploadToken, that.fileUploadToken) &&
            Objects.equals(compilationToken, that.compilationToken) &&
            Objects.equals(containingPlaceholderId, that.containingPlaceholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, token, fileUploadToken, compilationToken, containingPlaceholderId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlaceholderCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (token != null ? "token=" + token + ", " : "") +
            (fileUploadToken != null ? "fileUploadToken=" + fileUploadToken + ", " : "") +
            (compilationToken != null ? "compilationToken=" + compilationToken + ", " : "") +
            (containingPlaceholderId != null ? "containingPlaceholderId=" + containingPlaceholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
