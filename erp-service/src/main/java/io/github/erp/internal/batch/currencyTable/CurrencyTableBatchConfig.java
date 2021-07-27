
/*-
 *  Server - Leases and assets management platform
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
package io.github.erp.internal.batch.currencyTable;

import com.google.common.collect.ImmutableList;
import io.github.erp.internal.framework.BatchService;
import io.github.erp.internal.framework.FileUploadsProperties;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.framework.excel.ExcelFileDeserializer;
import io.github.erp.internal.model.sampleDataModel.CurrencyTableEVM;
import io.github.erp.service.FileUploadService;
import io.github.erp.service.dto.CurrencyTableDTO;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * This is a sample batch configuration for the currency-table
 */
@Configuration
public class CurrencyTableBatchConfig {


    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ExcelFileDeserializer<CurrencyTableEVM> deserializer;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private FileUploadsProperties fileUploadsProperties;

    @Value("#{jobParameters['fileId']}")
    private static long fileId;

    @Value("#{jobParameters['startUpTime']}")
    private static long startUpTime;

    @Autowired
    private JobExecutionListener persistenceJobListener;

    @Autowired
    private BatchService<CurrencyTableDTO> currencyTableDTOBatchService;

    @Autowired
    private Mapping<CurrencyTableEVM, CurrencyTableDTO> mapping;


    @Bean("currencyTablePersistenceJob")
    public Job currencyTablePersistenceJob() {
        // @formatter:off
        return jobBuilderFactory.get("currencyTablePersistenceJob")
                                .preventRestart()
                                .listener(persistenceJobListener)
                                .incrementer(new RunIdIncrementer())
                                .flow(readCurrencyListFromFile())
                                .end()
                                .build();
        // @formatter:on
    }

    @Bean
    public ItemWriter<List<CurrencyTableDTO>> currencyTableItemWriter() {

        return items -> items.stream().peek(currencyTableDTOBatchService::save).forEach(currencyTableDTOBatchService::index);
    }

    @Bean
    public ItemProcessor<List<CurrencyTableEVM>, List<CurrencyTableDTO>> currencyTableItemProcessor() {

        return evms -> evms.stream().map(mapping::toValue2).collect(ImmutableList.toImmutableList());
    }

    @Bean("readCurrencyListFromFile")
    public Step readCurrencyListFromFile() {
        // @formatter:off
        return stepBuilderFactory.get("readCurrencyListFromFile")
            .<List<CurrencyTableEVM>, List<CurrencyTableDTO>>chunk(2)
            .reader(currencyTableItemReader(fileId))
            .processor(currencyTableItemProcessor())
            .writer(currencyTableItemWriter())
            .build();
        // @formatter:off
    }

    @Bean("currencyTableItemReader")
    @JobScope
    public CurrencyTableListItemReader currencyTableItemReader(@Value("#{jobParameters['fileId']}") long fileId) {

        return new CurrencyTableListItemReader(deserializer, fileUploadService, fileId, fileUploadsProperties);
    }
}
