package io.github.erp.erp.assets.depreciation.context;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DepreciationAmountContext implements Serializable {

    private static final HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
    private static final DepreciationAmountContext INSTANCE = new DepreciationAmountContext();

    private static final String CONTEXT_ID = "depreciationAmountContext";

    private int numberOfProcessedItems;
    private int numberOfPendingItems;
    private Map<String, Map<String, Double>> amountsByAssetCategoryAndServiceOutlet;

    public static DepreciationAmountContext getInstance() {
        return INSTANCE;
    }

    private DepreciationAmountContext() {
        this.amountsByAssetCategoryAndServiceOutlet = new HashMap<>();
    }

    public UUID createDepreciationAmountContext() {
        UUID uuid = UUID.randomUUID();
        DepreciationAmountContext depreciationContext = new DepreciationAmountContext();
        hazelcastInstance.getMap(CONTEXT_ID).put(uuid, depreciationContext);
        return uuid;
    }

    public static DepreciationAmountContext getDepreciationAmountContext(UUID contextId) {
        return (DepreciationAmountContext) hazelcastInstance.getMap(CONTEXT_ID).get(contextId);
    }

    public int getNumberOfProcessedItems() {
        return numberOfProcessedItems;
    }

    public int getNumberOfPendingItems() {
        return numberOfPendingItems;
    }

    public int reduceNumberOfPendingItems() {
        return numberOfPendingItems - 1;
    }

    public int reduceNumberOfPendingItemsBy(int numberOfProcessedItems) {
        return numberOfPendingItems - numberOfProcessedItems;
    }

    public void setNumberOfProcessedItems(int numberOfProcessedItems) {
        this.numberOfProcessedItems = numberOfProcessedItems;
    }

    public void addNumberOfPendingItems(int numberOfPendingItems) {
        this.numberOfPendingItems =+ numberOfPendingItems;
    }

    public Map<String, Map<String, Double>> getAmountsByAssetCategoryAndServiceOutlet() {
        return amountsByAssetCategoryAndServiceOutlet;
    }

    public void setAmountsByAssetCategoryAndServiceOutlet(Map<String, Map<String, Double>> amountsByAssetCategoryAndServiceOutlet) {
        this.amountsByAssetCategoryAndServiceOutlet = amountsByAssetCategoryAndServiceOutlet;
    }

    // Update amounts for a given service outlet within an asset category
    public void updateAmountForServiceOutlet(String assetCategory, String serviceOutlet, Double amount) {
        amountsByAssetCategoryAndServiceOutlet
            .computeIfAbsent(assetCategory, k -> new HashMap<>())
            .merge(serviceOutlet, amount, Double::sum);
    }

}
