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
