package io.github.erp.erp.assets.depreciation.context;

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
