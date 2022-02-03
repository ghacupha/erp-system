package io.github.erp.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.SettlementCurrency} entity.
 */
public class SettlementCurrencyDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3, max = 3)
    private String iso4217CurrencyCode;

    @NotNull
    private String currencyName;

    @NotNull
    private String country;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIso4217CurrencyCode() {
        return iso4217CurrencyCode;
    }

    public void setIso4217CurrencyCode(String iso4217CurrencyCode) {
        this.iso4217CurrencyCode = iso4217CurrencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SettlementCurrencyDTO)) {
            return false;
        }

        SettlementCurrencyDTO settlementCurrencyDTO = (SettlementCurrencyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, settlementCurrencyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SettlementCurrencyDTO{" +
            "id=" + getId() +
            ", iso4217CurrencyCode='" + getIso4217CurrencyCode() + "'" +
            ", currencyName='" + getCurrencyName() + "'" +
            ", country='" + getCountry() + "'" +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
