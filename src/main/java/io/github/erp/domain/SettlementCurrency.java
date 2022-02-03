package io.github.erp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SettlementCurrency.
 */
@Entity
@Table(name = "settlement_currency")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "settlementcurrency")
public class SettlementCurrency implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3, max = 3)
    @Column(name = "iso_4217_currency_code", length = 3, nullable = false, unique = true)
    private String iso4217CurrencyCode;

    @NotNull
    @Column(name = "currency_name", nullable = false, unique = true)
    private String currencyName;

    @NotNull
    @Column(name = "country", nullable = false, unique = true)
    private String country;

    @Column(name = "file_upload_token")
    private String fileUploadToken;

    @Column(name = "compilation_token")
    private String compilationToken;

    @ManyToMany
    @JoinTable(
        name = "rel_settlement_currency__placeholder",
        joinColumns = @JoinColumn(name = "settlement_currency_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SettlementCurrency id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIso4217CurrencyCode() {
        return this.iso4217CurrencyCode;
    }

    public SettlementCurrency iso4217CurrencyCode(String iso4217CurrencyCode) {
        this.setIso4217CurrencyCode(iso4217CurrencyCode);
        return this;
    }

    public void setIso4217CurrencyCode(String iso4217CurrencyCode) {
        this.iso4217CurrencyCode = iso4217CurrencyCode;
    }

    public String getCurrencyName() {
        return this.currencyName;
    }

    public SettlementCurrency currencyName(String currencyName) {
        this.setCurrencyName(currencyName);
        return this;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCountry() {
        return this.country;
    }

    public SettlementCurrency country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFileUploadToken() {
        return this.fileUploadToken;
    }

    public SettlementCurrency fileUploadToken(String fileUploadToken) {
        this.setFileUploadToken(fileUploadToken);
        return this;
    }

    public void setFileUploadToken(String fileUploadToken) {
        this.fileUploadToken = fileUploadToken;
    }

    public String getCompilationToken() {
        return this.compilationToken;
    }

    public SettlementCurrency compilationToken(String compilationToken) {
        this.setCompilationToken(compilationToken);
        return this;
    }

    public void setCompilationToken(String compilationToken) {
        this.compilationToken = compilationToken;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public SettlementCurrency placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public SettlementCurrency addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public SettlementCurrency removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SettlementCurrency)) {
            return false;
        }
        return id != null && id.equals(((SettlementCurrency) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SettlementCurrency{" +
            "id=" + getId() +
            ", iso4217CurrencyCode='" + getIso4217CurrencyCode() + "'" +
            ", currencyName='" + getCurrencyName() + "'" +
            ", country='" + getCountry() + "'" +
            ", fileUploadToken='" + getFileUploadToken() + "'" +
            ", compilationToken='" + getCompilationToken() + "'" +
            "}";
    }
}
