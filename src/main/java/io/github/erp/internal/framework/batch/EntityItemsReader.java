package io.github.erp.internal.framework.batch;

/*-
 * Erp System - Mark III No 12 (Caleb Series) Server ver 0.9.0
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
