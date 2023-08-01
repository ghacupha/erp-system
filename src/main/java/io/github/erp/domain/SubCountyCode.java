package io.github.erp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SubCountyCode.
 */
@Entity
@Table(name = "sub_county_code")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "subcountycode")
public class SubCountyCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "county_code")
    private String countyCode;

    @Column(name = "county_name")
    private String countyName;

    @Column(name = "sub_county_code")
    private String subCountyCode;

    @Column(name = "sub_county_name")
    private String subCountyName;

    @ManyToMany
    @JoinTable(
        name = "rel_sub_county_code__placeholder",
        joinColumns = @JoinColumn(name = "sub_county_code_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SubCountyCode id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountyCode() {
        return this.countyCode;
    }

    public SubCountyCode countyCode(String countyCode) {
        this.setCountyCode(countyCode);
        return this;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public String getCountyName() {
        return this.countyName;
    }

    public SubCountyCode countyName(String countyName) {
        this.setCountyName(countyName);
        return this;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getSubCountyCode() {
        return this.subCountyCode;
    }

    public SubCountyCode subCountyCode(String subCountyCode) {
        this.setSubCountyCode(subCountyCode);
        return this;
    }

    public void setSubCountyCode(String subCountyCode) {
        this.subCountyCode = subCountyCode;
    }

    public String getSubCountyName() {
        return this.subCountyName;
    }

    public SubCountyCode subCountyName(String subCountyName) {
        this.setSubCountyName(subCountyName);
        return this;
    }

    public void setSubCountyName(String subCountyName) {
        this.subCountyName = subCountyName;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public SubCountyCode placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public SubCountyCode addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public SubCountyCode removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubCountyCode)) {
            return false;
        }
        return id != null && id.equals(((SubCountyCode) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubCountyCode{" +
            "id=" + getId() +
            ", countyCode='" + getCountyCode() + "'" +
            ", countyName='" + getCountyName() + "'" +
            ", subCountyCode='" + getSubCountyCode() + "'" +
            ", subCountyName='" + getSubCountyName() + "'" +
            "}";
    }
}
