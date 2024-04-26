package io.github.erp.internal.framework.excel;

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
import com.poiji.option.PoijiOptions;
import com.poiji.option.PoijiOptions.PoijiOptionsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ExcelTestUtil {

    private final static Logger log = LoggerFactory.getLogger(ExcelTestUtil.class);

    public static byte[] toBytes(File file) throws IOException {

        return Files.readAllBytes(file.toPath());
    }


    public static PoijiOptions getDefaultPoijiOptions() {

        // @formatter:off
        return PoijiOptionsBuilder.settings()
                           .ignoreHiddenSheets(true)
                           .preferNullOverDefault(true)
                           .datePattern("yyyy/MM/dd")
                           .dateTimeFormatter(DateTimeFormatter.ISO_DATE_TIME)
                           .build();
        // @formatter:on
    }

    public static File readFile(String filename) {

        log.info("\nReading file : {}...", filename);

    // @formatter:off
    return new File(
            Objects.requireNonNull(
                ClassLoader.getSystemClassLoader()
                           .getResource("files/" + filename))
                   .getFile());
    // @formatter:on
    }

}
