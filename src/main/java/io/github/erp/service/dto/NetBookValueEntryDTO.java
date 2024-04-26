package io.github.erp.service.dto;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.NetBookValueEntry} entity.
 */
public class NetBookValueEntryDTO implements Serializable {

    private Long id;

    private String assetNumber;

    private String assetTag;

    private String assetDescription;

    @NotNull
    private UUID nbvIdentifier;

    private UUID compilationJobIdentifier;

    private UUID compilationBatchIdentifier;

    private Integer elapsedMonths;

    private Integer priorMonths;

    private Double usefulLifeYears;

    private BigDecimal netBookValueAmount;

    private BigDecimal previousNetBookValueAmount;

    private BigDecimal historicalCost;

    private LocalDate capitalizationDate;

    private ServiceOutletDTO serviceOutlet;

    private DepreciationPeriodDTO depreciationPeriod;

    private FiscalMonthDTO fiscalMonth;

    private DepreciationMethodDTO depreciationMethod;

    private AssetRegistrationDTO assetRegistration;

    private AssetCategoryDTO assetCategory;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssetNumber() {
        return assetNumber;
    }

    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
    }

    public String getAssetTag() {
        return assetTag;
    }

    public void setAssetTag(String assetTag) {
        this.assetTag = assetTag;
    }

    public String getAssetDescription() {
        return assetDescription;
    }

    public void setAssetDescription(String assetDescription) {
        this.assetDescription = assetDescription;
    }

    public UUID getNbvIdentifier() {
        return nbvIdentifier;
    }

    public void setNbvIdentifier(UUID nbvIdentifier) {
        this.nbvIdentifier = nbvIdentifier;
    }

    public UUID getCompilationJobIdentifier() {
        return compilationJobIdentifier;
    }

    public void setCompilationJobIdentifier(UUID compilationJobIdentifier) {
        this.compilationJobIdentifier = compilationJobIdentifier;
    }

    public UUID getCompilationBatchIdentifier() {
        return compilationBatchIdentifier;
    }

    public void setCompilationBatchIdentifier(UUID compilationBatchIdentifier) {
        this.compilationBatchIdentifier = compilationBatchIdentifier;
    }

    public Integer getElapsedMonths() {
        return elapsedMonths;
    }

    public void setElapsedMonths(Integer elapsedMonths) {
        this.elapsedMonths = elapsedMonths;
    }

    public Integer getPriorMonths() {
        return priorMonths;
    }

    public void setPriorMonths(Integer priorMonths) {
        this.priorMonths = priorMonths;
    }

    public Double getUsefulLifeYears() {
        return usefulLifeYears;
    }

    public void setUsefulLifeYears(Double usefulLifeYears) {
        this.usefulLifeYears = usefulLifeYears;
    }

    public BigDecimal getNetBookValueAmount() {
        return netBookValueAmount;
    }

    public void setNetBookValueAmount(BigDecimal netBookValueAmount) {
        this.netBookValueAmount = netBookValueAmount;
    }

    public BigDecimal getPreviousNetBookValueAmount() {
        return previousNetBookValueAmount;
    }

    public void setPreviousNetBookValueAmount(BigDecimal previousNetBookValueAmount) {
        this.previousNetBookValueAmount = previousNetBookValueAmount;
    }

    public BigDecimal getHistoricalCost() {
        return historicalCost;
    }

    public void setHistoricalCost(BigDecimal historicalCost) {
        this.historicalCost = historicalCost;
    }

    public LocalDate getCapitalizationDate() {
        return capitalizationDate;
    }

    public void setCapitalizationDate(LocalDate capitalizationDate) {
        this.capitalizationDate = capitalizationDate;
    }

    public ServiceOutletDTO getServiceOutlet() {
        return serviceOutlet;
    }

    public void setServiceOutlet(ServiceOutletDTO serviceOutlet) {
        this.serviceOutlet = serviceOutlet;
    }

    public DepreciationPeriodDTO getDepreciationPeriod() {
        return depreciationPeriod;
    }

    public void setDepreciationPeriod(DepreciationPeriodDTO depreciationPeriod) {
        this.depreciationPeriod = depreciationPeriod;
    }

    public FiscalMonthDTO getFiscalMonth() {
        return fiscalMonth;
    }

    public void setFiscalMonth(FiscalMonthDTO fiscalMonth) {
        this.fiscalMonth = fiscalMonth;
    }

    public DepreciationMethodDTO getDepreciationMethod() {
        return depreciationMethod;
    }

    public void setDepreciationMethod(DepreciationMethodDTO depreciationMethod) {
        this.depreciationMethod = depreciationMethod;
    }

    public AssetRegistrationDTO getAssetRegistration() {
        return assetRegistration;
    }

    public void setAssetRegistration(AssetRegistrationDTO assetRegistration) {
        this.assetRegistration = assetRegistration;
    }

    public AssetCategoryDTO getAssetCategory() {
        return assetCategory;
    }

    public void setAssetCategory(AssetCategoryDTO assetCategory) {
        this.assetCategory = assetCategory;
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
        if (!(o instanceof NetBookValueEntryDTO)) {
            return false;
        }

        NetBookValueEntryDTO netBookValueEntryDTO = (NetBookValueEntryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, netBookValueEntryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NetBookValueEntryDTO{" +
            "id=" + getId() +
            ", assetNumber='" + getAssetNumber() + "'" +
            ", assetTag='" + getAssetTag() + "'" +
            ", assetDescription='" + getAssetDescription() + "'" +
            ", nbvIdentifier='" + getNbvIdentifier() + "'" +
            ", compilationJobIdentifier='" + getCompilationJobIdentifier() + "'" +
            ", compilationBatchIdentifier='" + getCompilationBatchIdentifier() + "'" +
            ", elapsedMonths=" + getElapsedMonths() +
            ", priorMonths=" + getPriorMonths() +
            ", usefulLifeYears=" + getUsefulLifeYears() +
            ", netBookValueAmount=" + getNetBookValueAmount() +
            ", previousNetBookValueAmount=" + getPreviousNetBookValueAmount() +
            ", historicalCost=" + getHistoricalCost() +
            ", capitalizationDate='" + getCapitalizationDate() + "'" +
            ", serviceOutlet=" + getServiceOutlet() +
            ", depreciationPeriod=" + getDepreciationPeriod() +
            ", fiscalMonth=" + getFiscalMonth() +
            ", depreciationMethod=" + getDepreciationMethod() +
            ", assetRegistration=" + getAssetRegistration() +
            ", assetCategory=" + getAssetCategory() +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
