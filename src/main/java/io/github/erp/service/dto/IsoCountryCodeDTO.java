package io.github.erp.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link io.github.erp.domain.IsoCountryCode} entity.
 */
public class IsoCountryCodeDTO implements Serializable {

    private Long id;

    private String countryCode;

    private String countryDescription;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryDescription() {
        return countryDescription;
    }

    public void setCountryDescription(String countryDescription) {
        this.countryDescription = countryDescription;
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
        if (!(o instanceof IsoCountryCodeDTO)) {
            return false;
        }

        IsoCountryCodeDTO isoCountryCodeDTO = (IsoCountryCodeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, isoCountryCodeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IsoCountryCodeDTO{" +
            "id=" + getId() +
            ", countryCode='" + getCountryCode() + "'" +
            ", countryDescription='" + getCountryDescription() + "'" +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
