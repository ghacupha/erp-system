package io.github.erp.internal.framework.excel;

/*-
 * Erp System - Mark II No 7 (Artaxerxes Series)
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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
