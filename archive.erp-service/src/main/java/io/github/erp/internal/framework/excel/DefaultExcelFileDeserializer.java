package io.github.erp.internal.framework.excel;

/*-
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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
import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;
import com.poiji.option.PoijiOptions;
import org.slf4j.Logger;

import java.io.InputStream;
import java.util.List;

import static io.github.erp.internal.framework.excel.DeserializationUtils.getFileInputStream;

/**
 * This is the default configuration for excel deserialization for an excel view model of type T
 * @param <T> Type of excel view model
 */
public class DefaultExcelFileDeserializer<T> {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DefaultExcelFileDeserializer.class);

    private final Class<T> deserializationClass;
    private final PoijiOptions poijiOptions;

    public DefaultExcelFileDeserializer(final Class<T> deserializationClass, final PoijiOptions poijiOptions) {
        this.deserializationClass = deserializationClass;
        this.poijiOptions = poijiOptions;
    }

    /**
     * This method opens a byte stream and converts the data file into a list of data items contained in its rows
     *
     * @param excelFile Received containing the data to be deserialized
     * @return List of data of type T
     */
    public List<T> deserialize(final byte[] excelFile) {
        InputStream fileInputStream = getFileInputStream(excelFile);
        long time = System.currentTimeMillis();
        List<T> entries = Poiji.fromExcel(fileInputStream, PoijiExcelType.XLSX, deserializationClass, poijiOptions);
        long readTime = System.currentTimeMillis() - time;
        log.info("\n{} entries deserialized from file: in {} millis", entries.size(), readTime);
        return entries;
    }
}
