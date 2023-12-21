package io.github.erp.domain;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.enumeration.ControlTypes;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A QuestionBase.
 */
@Entity
@Table(name = "question_base")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "questionbase")
public class QuestionBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "context", nullable = false, unique = true)
    private String context;

    @NotNull
    @Column(name = "serial", nullable = false, unique = true)
    private UUID serial;

    @Column(name = "question_base_value")
    private String questionBaseValue;

    @NotNull
    @Column(name = "question_base_key", nullable = false, unique = true)
    private String questionBaseKey;

    @NotNull
    @Column(name = "question_base_label", nullable = false, unique = true)
    private String questionBaseLabel;

    @Column(name = "required")
    private Boolean required;

    @NotNull
    @Column(name = "jhi_order", nullable = false)
    private Integer order;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "control_type", nullable = false)
    private ControlTypes controlType;

    @Column(name = "placeholder")
    private String placeholder;

    @Column(name = "iterable")
    private Boolean iterable;

    @ManyToMany
    @JoinTable(
        name = "rel_question_base__parameters",
        joinColumns = @JoinColumn(name = "question_base_id"),
        inverseJoinColumns = @JoinColumn(name = "parameters_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parentMapping", "placeholders" }, allowSetters = true)
    private Set<UniversallyUniqueMapping> parameters = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_question_base__placeholder_item",
        joinColumns = @JoinColumn(name = "question_base_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_item_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholderItems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public QuestionBase id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContext() {
        return this.context;
    }

    public QuestionBase context(String context) {
        this.setContext(context);
        return this;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public UUID getSerial() {
        return this.serial;
    }

    public QuestionBase serial(UUID serial) {
        this.setSerial(serial);
        return this;
    }

    public void setSerial(UUID serial) {
        this.serial = serial;
    }

    public String getQuestionBaseValue() {
        return this.questionBaseValue;
    }

    public QuestionBase questionBaseValue(String questionBaseValue) {
        this.setQuestionBaseValue(questionBaseValue);
        return this;
    }

    public void setQuestionBaseValue(String questionBaseValue) {
        this.questionBaseValue = questionBaseValue;
    }

    public String getQuestionBaseKey() {
        return this.questionBaseKey;
    }

    public QuestionBase questionBaseKey(String questionBaseKey) {
        this.setQuestionBaseKey(questionBaseKey);
        return this;
    }

    public void setQuestionBaseKey(String questionBaseKey) {
        this.questionBaseKey = questionBaseKey;
    }

    public String getQuestionBaseLabel() {
        return this.questionBaseLabel;
    }

    public QuestionBase questionBaseLabel(String questionBaseLabel) {
        this.setQuestionBaseLabel(questionBaseLabel);
        return this;
    }

    public void setQuestionBaseLabel(String questionBaseLabel) {
        this.questionBaseLabel = questionBaseLabel;
    }

    public Boolean getRequired() {
        return this.required;
    }

    public QuestionBase required(Boolean required) {
        this.setRequired(required);
        return this;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Integer getOrder() {
        return this.order;
    }

    public QuestionBase order(Integer order) {
        this.setOrder(order);
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public ControlTypes getControlType() {
        return this.controlType;
    }

    public QuestionBase controlType(ControlTypes controlType) {
        this.setControlType(controlType);
        return this;
    }

    public void setControlType(ControlTypes controlType) {
        this.controlType = controlType;
    }

    public String getPlaceholder() {
        return this.placeholder;
    }

    public QuestionBase placeholder(String placeholder) {
        this.setPlaceholder(placeholder);
        return this;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public Boolean getIterable() {
        return this.iterable;
    }

    public QuestionBase iterable(Boolean iterable) {
        this.setIterable(iterable);
        return this;
    }

    public void setIterable(Boolean iterable) {
        this.iterable = iterable;
    }

    public Set<UniversallyUniqueMapping> getParameters() {
        return this.parameters;
    }

    public void setParameters(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.parameters = universallyUniqueMappings;
    }

    public QuestionBase parameters(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.setParameters(universallyUniqueMappings);
        return this;
    }

    public QuestionBase addParameters(UniversallyUniqueMapping universallyUniqueMapping) {
        this.parameters.add(universallyUniqueMapping);
        return this;
    }

    public QuestionBase removeParameters(UniversallyUniqueMapping universallyUniqueMapping) {
        this.parameters.remove(universallyUniqueMapping);
        return this;
    }

    public Set<Placeholder> getPlaceholderItems() {
        return this.placeholderItems;
    }

    public void setPlaceholderItems(Set<Placeholder> placeholders) {
        this.placeholderItems = placeholders;
    }

    public QuestionBase placeholderItems(Set<Placeholder> placeholders) {
        this.setPlaceholderItems(placeholders);
        return this;
    }

    public QuestionBase addPlaceholderItem(Placeholder placeholder) {
        this.placeholderItems.add(placeholder);
        return this;
    }

    public QuestionBase removePlaceholderItem(Placeholder placeholder) {
        this.placeholderItems.remove(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionBase)) {
            return false;
        }
        return id != null && id.equals(((QuestionBase) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionBase{" +
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
            "}";
    }
}
