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

