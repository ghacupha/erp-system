package io.github.erp.domain;

/*-
 * Erp System - Mark III No 9 (Caleb Series) Server ver 0.5.0
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.enumeration.SystemContentTypeAvailability;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A SystemContentType.
 */
@Entity
@Table(name = "system_content_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "systemcontenttype")
public class SystemContentType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "content_type_name", nullable = false, unique = true)
    private String contentTypeName;

    @NotNull
    @Column(name = "content_type_header", nullable = false)
    private String contentTypeHeader;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "comments")
    private String comments;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "availability", nullable = false)
    private SystemContentTypeAvailability availability;

    @ManyToMany
    @JoinTable(
        name = "rel_system_content_type__placeholders",
        joinColumns = @JoinColumn(name = "system_content_type_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholders_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_system_content_type__sys_maps",
        joinColumns = @JoinColumn(name = "system_content_type_id"),
        inverseJoinColumns = @JoinColumn(name = "sys_maps_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parentMapping", "placeholders" }, allowSetters = true)
    private Set<UniversallyUniqueMapping> sysMaps = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SystemContentType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContentTypeName() {
        return this.contentTypeName;
    }

    public SystemContentType contentTypeName(String contentTypeName) {
        this.setContentTypeName(contentTypeName);
        return this;
    }

    public void setContentTypeName(String contentTypeName) {
        this.contentTypeName = contentTypeName;
    }

    public String getContentTypeHeader() {
        return this.contentTypeHeader;
    }

    public SystemContentType contentTypeHeader(String contentTypeHeader) {
        this.setContentTypeHeader(contentTypeHeader);
        return this;
    }

    public void setContentTypeHeader(String contentTypeHeader) {
        this.contentTypeHeader = contentTypeHeader;
    }

    public String getComments() {
        return this.comments;
    }

    public SystemContentType comments(String comments) {
        this.setComments(comments);
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public SystemContentTypeAvailability getAvailability() {
        return this.availability;
    }

    public SystemContentType availability(SystemContentTypeAvailability availability) {
        this.setAvailability(availability);
        return this;
    }

    public void setAvailability(SystemContentTypeAvailability availability) {
        this.availability = availability;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public SystemContentType placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public SystemContentType addPlaceholders(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public SystemContentType removePlaceholders(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public Set<UniversallyUniqueMapping> getSysMaps() {
        return this.sysMaps;
    }

    public void setSysMaps(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.sysMaps = universallyUniqueMappings;
    }

    public SystemContentType sysMaps(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.setSysMaps(universallyUniqueMappings);
        return this;
    }

    public SystemContentType addSysMaps(UniversallyUniqueMapping universallyUniqueMapping) {
        this.sysMaps.add(universallyUniqueMapping);
        return this;
    }

    public SystemContentType removeSysMaps(UniversallyUniqueMapping universallyUniqueMapping) {
        this.sysMaps.remove(universallyUniqueMapping);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SystemContentType)) {
            return false;
        }
        return id != null && id.equals(((SystemContentType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SystemContentType{" +
            "id=" + getId() +
            ", contentTypeName='" + getContentTypeName() + "'" +
            ", contentTypeHeader='" + getContentTypeHeader() + "'" +
            ", comments='" + getComments() + "'" +
            ", availability='" + getAvailability() + "'" +
            "}";
    }
}
