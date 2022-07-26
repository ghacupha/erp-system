package io.github.erp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PrepaymentMapping.
 */
@Entity
@Table(name = "prepayment_mapping")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "prepaymentmapping")
public class PrepaymentMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "key", nullable = false, unique = true)
    private String key;

    @NotNull
    @Column(name = "guid", nullable = false, unique = true)
    private UUID guid;

    @NotNull
    @Column(name = "parameter", nullable = false)
    private String parameter;

    @ManyToMany
    @JoinTable(
        name = "rel_prepayment_mapping__placeholder",
        joinColumns = @JoinColumn(name = "prepayment_mapping_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PrepaymentMapping id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return this.key;
    }

    public PrepaymentMapping key(String key) {
        this.setKey(key);
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public UUID getGuid() {
        return this.guid;
    }

    public PrepaymentMapping guid(UUID guid) {
        this.setGuid(guid);
        return this;
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
    }

    public String getParameter() {
        return this.parameter;
    }

    public PrepaymentMapping parameter(String parameter) {
        this.setParameter(parameter);
        return this;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public PrepaymentMapping placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public PrepaymentMapping addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public PrepaymentMapping removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrepaymentMapping)) {
            return false;
        }
        return id != null && id.equals(((PrepaymentMapping) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrepaymentMapping{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", guid='" + getGuid() + "'" +
            ", parameter='" + getParameter() + "'" +
            "}";
    }
}
