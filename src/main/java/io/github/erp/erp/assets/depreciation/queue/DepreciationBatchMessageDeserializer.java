package io.github.erp.erp.assets.depreciation.queue;

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
import java.io.IOException;

import io.github.erp.erp.assets.depreciation.model.DepreciationBatchMessage;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class DepreciationBatchMessageDeserializer implements Deserializer<DepreciationBatchMessage> {

    public static final ObjectMapper mapper = JsonMapper.builder()
        .findAndAddModules()
        .build();

    @Override
    public DepreciationBatchMessage deserialize(String topic, byte[] data) {
        try {
            return mapper.readValue(data, DepreciationBatchMessage.class);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }
}
