package io.github.erp.internal.service;

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
import io.github.erp.internal.framework.excel.DefaultExcelFileDeserializer;
import io.github.erp.internal.framework.excel.ExcelFileDeserializer;
import io.github.erp.internal.model.FixedAssetAcquisitionEVM;
import io.github.erp.internal.model.FixedAssetDepreciationEVM;
import io.github.erp.internal.model.FixedAssetNetBookValueEVM;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.github.erp.internal.framework.excel.PoijiOptionsConfig.getDefaultPoijiOptions;

/**
 * This container has configurations for our excel file deserializers and a sample is provided for currency-table
 */
@Configuration
public class ExcelDeserializerContainer {

    @Bean("fixedAssetAcquisitionExcelFileDeserializer")
    public ExcelFileDeserializer<FixedAssetAcquisitionEVM> fixedAssetAcquisitionExcelFileDeserializer() {
        return excelFile -> new DefaultExcelFileDeserializer<>(FixedAssetAcquisitionEVM.class, getDefaultPoijiOptions()).deserialize(excelFile);
    }

    @Bean("fixedAssetDepreciationExcelFileDeserializer")
    public ExcelFileDeserializer<FixedAssetDepreciationEVM> fixedAssetDepreciationExcelFileDeserializer() {
        return excelFile -> new DefaultExcelFileDeserializer<>(FixedAssetDepreciationEVM.class, getDefaultPoijiOptions()).deserialize(excelFile);
    }

    @Bean("fixedAssetNetBookValueExcelFileDeserializer")
    public ExcelFileDeserializer<FixedAssetNetBookValueEVM> fixedAssetNetBookValueExcelFileDeserializer() {
        return excelFile -> new DefaultExcelFileDeserializer<>(FixedAssetNetBookValueEVM.class, getDefaultPoijiOptions()).deserialize(excelFile);
    }
}
