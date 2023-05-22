package io.github.erp.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.CountyCode} entity.
 */
public class CountyCodeDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer countyCode;

    @NotNull
    private String countyName;

    @NotNull
    private Integer subCountyCode;

    @NotNull
    private String subCountyName;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(Integer countyCode) {
        this.countyCode = countyCode;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public Integer getSubCountyCode() {
        return subCountyCode;
    }

    public void setSubCountyCode(Integer subCountyCode) {
        this.subCountyCode = subCountyCode;
    }

    public String getSubCountyName() {
        return subCountyName;
    }

    public void setSubCountyName(String subCountyName) {
        this.subCountyName = subCountyName;
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
        if (!(o instanceof CountyCodeDTO)) {
            return false;
        }

        CountyCodeDTO countyCodeDTO = (CountyCodeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, countyCodeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountyCodeDTO{" +
            "id=" + getId() +
            ", countyCode=" + getCountyCode() +
            ", countyName='" + getCountyName() + "'" +
            ", subCountyCode=" + getSubCountyCode() +
            ", subCountyName='" + getSubCountyName() + "'" +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
