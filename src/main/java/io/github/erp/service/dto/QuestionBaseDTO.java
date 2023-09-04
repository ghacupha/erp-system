package io.github.erp.service.dto;

/*-
 * Erp System - Mark IV No 6 (Ehud Series) Server ver 1.4.0
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
import io.github.erp.domain.enumeration.ControlTypes;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.QuestionBase} entity.
 */
public class QuestionBaseDTO implements Serializable {

    private Long id;

    @NotNull
    private String context;

    @NotNull
    private UUID serial;

    private String questionBaseValue;

    @NotNull
    private String questionBaseKey;

    @NotNull
    private String questionBaseLabel;

    private Boolean required;

    @NotNull
    private Integer order;

    @NotNull
    private ControlTypes controlType;

    private String placeholder;

    private Boolean iterable;

    private Set<UniversallyUniqueMappingDTO> parameters = new HashSet<>();

    private Set<PlaceholderDTO> placeholderItems = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public UUID getSerial() {
        return serial;
    }

    public void setSerial(UUID serial) {
        this.serial = serial;
    }

    public String getQuestionBaseValue() {
        return questionBaseValue;
    }

    public void setQuestionBaseValue(String questionBaseValue) {
        this.questionBaseValue = questionBaseValue;
    }

    public String getQuestionBaseKey() {
        return questionBaseKey;
    }

    public void setQuestionBaseKey(String questionBaseKey) {
        this.questionBaseKey = questionBaseKey;
    }

    public String getQuestionBaseLabel() {
        return questionBaseLabel;
    }

    public void setQuestionBaseLabel(String questionBaseLabel) {
        this.questionBaseLabel = questionBaseLabel;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public ControlTypes getControlType() {
        return controlType;
    }

    public void setControlType(ControlTypes controlType) {
        this.controlType = controlType;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public Boolean getIterable() {
        return iterable;
    }

    public void setIterable(Boolean iterable) {
        this.iterable = iterable;
    }

    public Set<UniversallyUniqueMappingDTO> getParameters() {
        return parameters;
    }

    public void setParameters(Set<UniversallyUniqueMappingDTO> parameters) {
        this.parameters = parameters;
    }

    public Set<PlaceholderDTO> getPlaceholderItems() {
        return placeholderItems;
    }

    public void setPlaceholderItems(Set<PlaceholderDTO> placeholderItems) {
        this.placeholderItems = placeholderItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionBaseDTO)) {
            return false;
        }

        QuestionBaseDTO questionBaseDTO = (QuestionBaseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, questionBaseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionBaseDTO{" +
            "id=" + getId() +
            ", context='" + getContext() + "'" +
            ", serial='" + getSerial() + "'" +
            ", questionBaseValue='" + getQuestionBaseValue() + "'" +
            ", questionBaseKey='" + getQuestionBaseKey() + "'" +
            ", questionBaseLabel='" + getQuestionBaseLabel() + "'" +
            ", required='" + getRequired() + "'" +
            ", order=" + getOrder() +
            ", controlType='" + getControlType() + "'" +
            ", placeholder='" + getPlaceholder() + "'" +
            ", iterable='" + getIterable() + "'" +
            ", parameters=" + getParameters() +
            ", placeholderItems=" + getPlaceholderItems() +
            "}";
    }
}
