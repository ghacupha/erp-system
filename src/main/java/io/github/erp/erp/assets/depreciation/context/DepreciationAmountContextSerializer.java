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

