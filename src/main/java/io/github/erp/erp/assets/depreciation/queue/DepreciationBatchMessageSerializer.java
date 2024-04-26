package io.github.erp.erp.assets.depreciation.queue;

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
import io.github.erp.erp.assets.depreciation.model.DepreciationBatchMessage;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class DepreciationBatchMessageSerializer implements Serializer<DepreciationBatchMessage> {

    public static final ObjectMapper mapper = JsonMapper.builder()
        .findAndAddModules()
        .build();

    @Override
    public byte[] serialize(String topic, DepreciationBatchMessage message) {
        try {
            return mapper.writeValueAsBytes(message);
        } catch (JsonProcessingException e) {
            throw new SerializationException(e);
        }
    }
}
