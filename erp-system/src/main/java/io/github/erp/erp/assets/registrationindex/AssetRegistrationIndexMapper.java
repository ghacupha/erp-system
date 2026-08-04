package io.github.erp.erp.assets.registrationindex;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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

import io.github.erp.domain.AssetRegistration;
import io.github.erp.domain.AssetRegistrationIndex;
import io.github.erp.domain.Dealer;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * Flattens an {@link AssetRegistration} - loaded with its relationships eagerly fetched, see
 * {@code InternalAssetRegistrationRepository#findOneWithEagerRelationships} - into an
 * {@link AssetRegistrationIndex} document. Only scalar identifying fields are pulled from each
 * related entity (names, numbers, titles), never the related entities themselves, so the result
 * stays flat regardless of how deep the real entity graph goes.
 */
@Component
public class AssetRegistrationIndexMapper {

    public AssetRegistrationIndex toIndex(AssetRegistration assetRegistration) {
        AssetRegistrationIndex index = new AssetRegistrationIndex();

        index.setId(assetRegistration.getId());
        index.setAssetNumber(assetRegistration.getAssetNumber());
        index.setAssetTag(assetRegistration.getAssetTag());
        index.setAssetDetails(assetRegistration.getAssetDetails());
        index.setModelNumber(assetRegistration.getModelNumber());
        index.setSerialNumber(assetRegistration.getSerialNumber());
        index.setRemarks(assetRegistration.getRemarks());
        index.setAssetCost(assetRegistration.getAssetCost());
        index.setHistoricalCost(assetRegistration.getHistoricalCost());
        index.setCapitalizationDate(assetRegistration.getCapitalizationDate());
        index.setRegistrationDate(assetRegistration.getRegistrationDate());

        if (assetRegistration.getAssetCategory() != null) {
            index.setAssetCategoryName(assetRegistration.getAssetCategory().getAssetCategoryName());
        }
        if (assetRegistration.getDealer() != null) {
            index.setDealerName(assetRegistration.getDealer().getDealerName());
        }
        if (assetRegistration.getMainServiceOutlet() != null) {
            index.setMainServiceOutletName(assetRegistration.getMainServiceOutlet().getOutletName());
        }
        if (assetRegistration.getAcquiringTransaction() != null) {
            index.setAcquiringTransactionPaymentNumber(assetRegistration.getAcquiringTransaction().getPaymentNumber());
        }

        index.setDesignatedUserNames(names(assetRegistration.getDesignatedUsers(), Dealer::getDealerName));
        index.setAccessoryDescriptions(names(assetRegistration.getAssetAccessories(), a -> a.getAssetDetails()));
        index.setPaymentInvoiceNumbers(names(assetRegistration.getPaymentInvoices(), p -> p.getInvoiceNumber()));
        index.setPurchaseOrderNumbers(names(assetRegistration.getPurchaseOrders(), p -> p.getPurchaseOrderNumber()));
        index.setDeliveryNoteNumbers(names(assetRegistration.getDeliveryNotes(), d -> d.getDeliveryNoteNumber()));
        index.setJobSheetNumbers(names(assetRegistration.getJobSheets(), j -> j.getSerialNumber()));
        index.setBusinessDocumentTitles(names(assetRegistration.getBusinessDocuments(), b -> b.getDocumentTitle()));
        index.setAssetWarrantyNumbers(names(assetRegistration.getAssetWarranties(), w -> w.getAssetTag()));

        return index;
    }

    private <T> List<String> names(Set<T> entities, java.util.function.Function<T, String> nameExtractor) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream().map(nameExtractor).filter(java.util.Objects::nonNull).collect(Collectors.toList());
    }
}
