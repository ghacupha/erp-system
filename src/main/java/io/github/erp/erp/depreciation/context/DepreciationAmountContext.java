package io.github.erp.erp.depreciation.context;

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

    public void setNumberOfProcessedItems(int numberOfProcessedItems) {
        this.numberOfProcessedItems = numberOfProcessedItems;
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
