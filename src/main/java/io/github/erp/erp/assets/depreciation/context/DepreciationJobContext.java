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

import java.math.BigDecimal;
import java.util.UUID;

public class DepreciationJobContext {
    private static final HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
    private static final DepreciationJobContext INSTANCE = new DepreciationJobContext();

    private static final String CONTEXT_ID = "depreciationProcessContext";

    private DepreciationJobContext() {
        // Private constructor to prevent instantiation
    }

    public static DepreciationJobContext getInstance() {
        return INSTANCE;
    }

    public UUID createContext(int numberOfProcessedItems) {
        UUID uuid = UUID.randomUUID();
        hazelcastInstance.getMap(CONTEXT_ID).put(uuid, numberOfProcessedItems);
        return uuid;
    }

    public int getNumberOfProcessedItems(UUID uuid) {
        return (int) hazelcastInstance.getMap(CONTEXT_ID).getOrDefault(uuid, 0);
    }

    public BigDecimal getAmount(UUID uuid) {
        return (BigDecimal) hazelcastInstance.getMap(CONTEXT_ID).getOrDefault(uuid, BigDecimal.ZERO);
    }

    public void updateNumberOfProcessedItems(UUID uuid, int incrementBy) {
        int currentCount = getNumberOfProcessedItems(uuid);
        hazelcastInstance.getMap(CONTEXT_ID).put(uuid, currentCount + incrementBy);
    }

    public void updateAmount(UUID uuid, BigDecimal incrementBy) {
        BigDecimal currentAmount = getAmount(uuid);
        hazelcastInstance.getMap(CONTEXT_ID).put(uuid, currentAmount.add(incrementBy));
    }

    public void removeContext(UUID uuid) {
        hazelcastInstance.getMap(CONTEXT_ID).remove(uuid);
    }

}

