package io.github.erp.erp.assets.accessoryindex;

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

import io.github.erp.domain.AssetAccessory;
import io.github.erp.domain.AssetAccessoryIndex;
import io.github.erp.domain.Dealer;
import io.github.erp.domain.ServiceOutlet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * Flattens an {@link AssetAccessory} - loaded with its relationships eagerly fetched, see
 * {@code AssetAccessoryRepository#findOneWithEagerRelationships} - into an
 * {@link AssetAccessoryIndex} document. Mirrors AssetRegistrationIndexMapper's approach: only
 * scalar identifying fields from each related entity, never the related entities themselves.
 */
@Component
public class AssetAccessoryIndexMapper {

    public AssetAccessoryIndex toIndex(AssetAccessory assetAccessory) {
        AssetAccessoryIndex index = new AssetAccessoryIndex();

        index.setId(assetAccessory.getId());
        index.setAssetTag(assetAccessory.getAssetTag());
        index.setAssetDetails(assetAccessory.getAssetDetails());
        index.setModelNumber(assetAccessory.getModelNumber());
        index.setSerialNumber(assetAccessory.getSerialNumber());

        if (assetAccessory.getAssetCategory() != null) {
            index.setAssetCategoryName(assetAccessory.getAssetCategory().getAssetCategoryName());
        }
        if (assetAccessory.getDealer() != null) {
            index.setDealerName(assetAccessory.getDealer().getDealerName());
        }
        if (assetAccessory.getMainServiceOutlet() != null) {
            index.setMainServiceOutletName(assetAccessory.getMainServiceOutlet().getOutletName());
        }

        index.setDesignatedUserNames(names(assetAccessory.getDesignatedUsers(), Dealer::getDealerName));
        index.setServiceOutletNames(names(assetAccessory.getServiceOutlets(), ServiceOutlet::getOutletName));
        index.setAssetWarrantyNumbers(names(assetAccessory.getAssetWarranties(), w -> w.getAssetTag()));
        index.setPaymentInvoiceNumbers(names(assetAccessory.getPaymentInvoices(), p -> p.getInvoiceNumber()));
        index.setPurchaseOrderNumbers(names(assetAccessory.getPurchaseOrders(), p -> p.getPurchaseOrderNumber()));
        index.setDeliveryNoteNumbers(names(assetAccessory.getDeliveryNotes(), d -> d.getDeliveryNoteNumber()));
        index.setJobSheetNumbers(names(assetAccessory.getJobSheets(), j -> j.getSerialNumber()));
        index.setBusinessDocumentTitles(names(assetAccessory.getBusinessDocuments(), b -> b.getDocumentTitle()));
        index.setSettlementPaymentNumbers(names(assetAccessory.getSettlements(), s -> s.getPaymentNumber()));

        return index;
    }

    private <T> List<String> names(Set<T> entities, Function<T, String> nameExtractor) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream().map(nameExtractor).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
