package io.github.erp.internal.framework.batch;

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
import io.github.erp.internal.framework.FileUploadsProperties;
import io.github.erp.internal.framework.excel.ExcelFileDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract implementation for reading data from an excel data file and then introducing the
 * same data into the batch process in small chunks of pre-configured chunks
 *
 * @param <EVM> Type of object represented in a row of the file in the data transfer object
 */
public class EntityItemsReader<EVM> implements ItemReader<List<EVM>> {

    private static final Logger log = LoggerFactory.getLogger(EntityItemsReader.class);

    private final FileUploadsProperties fileUploadsProperties;

    private final ExcelFileDeserializer<EVM> deserializer;
    private final BatchPersistentFileUploadService fileUploadService;
    private final long fileId;

    private ListPartition<EVM> evmListPartition;

    public EntityItemsReader(
        final ExcelFileDeserializer<EVM> deserializer,
        final BatchPersistentFileUploadService fileUploadService,
        @Value("#{jobParameters['fileId']}") long fileId,
        final FileUploadsProperties fileUploadsProperties
    ) {
        this.deserializer = deserializer;
        this.fileUploadService = fileUploadService;
        this.fileId = fileId;
        this.fileUploadsProperties = fileUploadsProperties;
    }

    @PostConstruct
    private void resetIndex() {
        final List<EVM> unProcessedItems = new ArrayList<>();

        fileUploadService
            .findOne(fileId)
            .ifPresent(
                fileUpload -> {
                    unProcessedItems.addAll(deserializer.deserialize(fileUpload.getDataFile()));
                }
            );

        // Going for a big chunk due to expected file size
        evmListPartition = new ListPartition<>(fileUploadsProperties.getListSize(), unProcessedItems);

        log.info("List items deserialized : {}", unProcessedItems.size());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Every time this method is called, it will return a List of unprocessed items the size of which is dictated by the maximumPageSize;
     * <p>
     * Once the list of unprocessed items hits zero, the method call will return null;
     * </p>
     */
    @Override
    public List<EVM> read() throws Exception {
        List<EVM> forProcessing = evmListPartition.getPartition();

        log.info("Returning list of {} items", forProcessing.size());

        return forProcessing.size() == 0 ? null : forProcessing;
    }
}
