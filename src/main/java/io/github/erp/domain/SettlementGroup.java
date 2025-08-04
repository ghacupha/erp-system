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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A SettlementGroup.
 */
@Entity
@Table(name = "settlement_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "settlementgroup")
public class SettlementGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    @Field(type = FieldType.Long)
    private Long id;

    @NotNull
    @Column(name = "group_name", nullable = false, unique = true)
    @Field(type = FieldType.Text)
    private String groupName;

    @Column(name = "description")
    @Field(type = FieldType.Text)
    private String description;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "remarks")
    @Field(type = FieldType.Text)
    private String remarks;

    @ManyToOne
    @JsonIgnoreProperties(value = { "parentGroup", "placeholders" }, allowSetters = true)
    private SettlementGroup parentGroup;

    @ManyToMany
    @JoinTable(
        name = "rel_settlement_group__placeholder",
        joinColumns = @JoinColumn(name = "settlement_group_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    public Long getId() {
        return this.id;
    }

    public SettlementGroup id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public SettlementGroup groupName(String groupName) {
        this.setGroupName(groupName);
        return this;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDescription() {
        return this.description;
    }

    public SettlementGroup description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public SettlementGroup remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public SettlementGroup getParentGroup() {
        return this.parentGroup;
    }

    public void setParentGroup(SettlementGroup settlementGroup) {
        this.parentGroup = settlementGroup;
    }

    public SettlementGroup parentGroup(SettlementGroup settlementGroup) {
        this.setParentGroup(settlementGroup);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public SettlementGroup placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public SettlementGroup addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public SettlementGroup removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SettlementGroup)) {
            return false;
        }
        return id != null && id.equals(((SettlementGroup) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "SettlementGroup{" +
            "id=" + getId() +
            ", groupName='" + getGroupName() + "'" +
            ", description='" + getDescription() + "'" +
            ", remarks='" + getRemarks() + "'" +
            "}";
    }
}
