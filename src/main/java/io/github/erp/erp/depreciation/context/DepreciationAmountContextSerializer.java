package io.github.erp.erp.depreciation.context;


import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.StreamSerializer;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DepreciationAmountContextSerializer implements StreamSerializer<DepreciationAmountContext> {

    @Override
    public void write(ObjectDataOutput out, DepreciationAmountContext depreciationContext) throws IOException {
        out.writeInt(depreciationContext.getNumberOfProcessedItems());
        out.writeInt(depreciationContext.getAmountsByAssetCategoryAndServiceOutlet().size());

        for (Map.Entry<String, Map<String, Double>> entry : depreciationContext.getAmountsByAssetCategoryAndServiceOutlet().entrySet()) {
            out.writeUTF(entry.getKey());

            Map<String, Double> serviceOutletMap = entry.getValue();
            out.writeInt(serviceOutletMap.size());

            for (Map.Entry<String, Double> serviceOutletEntry : serviceOutletMap.entrySet()) {
                out.writeUTF(serviceOutletEntry.getKey());
                out.writeDouble(serviceOutletEntry.getValue().doubleValue());
            }
        }
    }

    @Override
    public DepreciationAmountContext read(ObjectDataInput in) throws IOException {
        DepreciationAmountContext depreciationContext = DepreciationAmountContext.getInstance();

        depreciationContext.setNumberOfProcessedItems(in.readInt());
        int assetCategoryMapSize = in.readInt();

        for (int i = 0; i < assetCategoryMapSize; i++) {
            String assetCategory = in.readUTF();
            int serviceOutletMapSize = in.readInt();

            Map<String, Double> serviceOutletMap = new HashMap<>();
            for (int j = 0; j < serviceOutletMapSize; j++) {
                String serviceOutlet = in.readUTF();
                Double amount = Double.valueOf(in.readDouble());
                serviceOutletMap.put(serviceOutlet, amount);
            }

            depreciationContext.getAmountsByAssetCategoryAndServiceOutlet().put(assetCategory, serviceOutletMap);
        }

        return depreciationContext;
    }

    @Override
    public int getTypeId() {
        // Make sure to choose a unique ID for your serializer
        return 12345;
    }

    @Override
    public void destroy() {
        // No cleanup needed
    }
}

