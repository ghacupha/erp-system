package io.github.erp.internal.framework.excel;

/*-
 * Erp System - Mark II No 27 (Baruch Series) Server ver 0.0.7-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import com.poiji.option.PoijiOptions;
import com.poiji.option.PoijiOptions.PoijiOptionsBuilder;

import java.time.format.DateTimeFormatter;

/**
 * POIJI Configuration
 */
public class PoijiOptionsConfig {

    public static PoijiOptions getDefaultPoijiOptions() {
        return PoijiOptionsHolder.getInstance();
    }

    /**
     * PoijiOptions is a configurations object. As it may appear to bewildered eye that the PoijiOptionsHolder
     * is an instance of over-engineering, but I would sooner ensure that I only have one PoijiOptions in the
     * entire applications because engineering for changes in this configuration would be too hard.
     * It is better to have an on demand initialization singleton that is also a double-locking checked
     * PoijiOptions as this object is used from reactive components running on Kafka generating messaging
     * events at any time during the application run, so there...
     */
    private static class PoijiOptionsHolder {
        private static volatile PoijiOptions INSTANCE;
        private static PoijiOptions getInstance() {
            if (INSTANCE == null) {
                synchronized (PoijiOptionsHolder.class) {
                    if (INSTANCE == null) {
                        // @formatter:off
                        INSTANCE = PoijiOptionsBuilder.settings()
                                                         .ignoreHiddenSheets(true)
                                                         .preferNullOverDefault(true)
                                                         .datePattern("yyyy/MM/dd")
                                                         .dateTimeFormatter(DateTimeFormatter.ISO_DATE_TIME)
                                                         .build();
                        // @formatter:on
                    }
                }
            }
            return INSTANCE;
        }
    }

}
