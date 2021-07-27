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
import io.github.erp.internal.model.FixedAssetAcquisitionEVM;
import io.github.erp.internal.model.FixedAssetDepreciationEVM;
import io.github.erp.internal.model.FixedAssetNetBookValueEVM;
import io.github.erp.internal.service.ExcelDeserializerContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.github.erp.internal.framework.AppConstants.DATETIME_FORMATTER;
import static io.github.erp.internal.framework.excel.ExcelTestUtil.readFile;
import static io.github.erp.internal.framework.excel.ExcelTestUtil.toBytes;
import static java.time.LocalDate.of;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test shows how the deserializer works inside the ItemReader interface.
 */
public class ExcelFileUtilsTest {

    private ExcelDeserializerContainer container;

    @BeforeEach
    void setUp() {
        container = new ExcelDeserializerContainer();
    }

    @Test
    public void fixedAssetAcquisitionFile() throws Exception {
        var deserializer = container.fixedAssetAcquisitionExcelFileDeserializer();

        List<FixedAssetAcquisitionEVM> evms = deserializer.deserialize(toBytes(readFile("assetAcquisitionList.xlsx")));

        assertThat(evms.size()).isEqualTo(13);

        for (int i = 0; i < 13; i++) {
            String index = String.valueOf(i + 1);
            assertThat(evms.get(i))
                .isEqualTo(
                    FixedAssetAcquisitionEVM
                        .builder()
                        .rowIndex((long) (i + 1))
                        .assetNumber((long) (i + 1))
                        .serviceOutletCode("SOL_ID " + index)
                        .assetTag("ASSET_TAG " + index)
                        .assetDescription("ASSET_DESCRIPTION " + index)
                        .purchaseDate(DATETIME_FORMATTER.format(of(2021, 1, 1).plusDays(Long.parseLong(index)).minusDays(1L)))
                        .assetCategory("ASSET_CATEGORY " + index)
                        .purchasePrice(1.1 + i)
                        .build()
                );
        }
    }

    @Test
    public void fixedAssetDepreciationListFile() throws Exception {
        var deserializer = container.fixedAssetDepreciationExcelFileDeserializer();

        List<FixedAssetDepreciationEVM> evms = deserializer.deserialize(toBytes(readFile("assetDepreciationList.xlsx")));

        assertThat(evms.size()).isEqualTo(13);

        for (int i = 0; i < 13; i++) {
            String index = String.valueOf(i + 1);
            assertThat(evms.get(i))
                .isEqualTo(
                    FixedAssetDepreciationEVM
                        .builder()
                        .rowIndex((long) (i + 1))
                        .assetNumber((long) (i + 1))
                        .serviceOutletCode("SOL_ID " + index)
                        .assetTag("ASSET_TAG " + index)
                        .assetDescription("ASSET_DESCRIPTION " + index)
                        .depreciationDate(DATETIME_FORMATTER.format(of(2021, 1, 1).plusDays(Long.parseLong(index)).minusDays(1L)))
                        .assetCategory("ASSET_CATEGORY " + index)
                        .depreciationAmount(1.1 + i)
                        .depreciationRegime("DEPRECIATION REGIME " + index)
                        .build()
                );
        }
    }

    @Test
    public void fixedAssetNetBookValueListFile() throws Exception {
        var deserializer = container.fixedAssetNetBookValueExcelFileDeserializer();

        List<FixedAssetNetBookValueEVM> evms = deserializer.deserialize(toBytes(readFile("assetNetBookValue.xlsx")));

        assertThat(evms.size()).isEqualTo(13);

        for (int i = 0; i < 13; i++) {
            String index = String.valueOf(i + 1);
            assertThat(evms.get(i))
                .isEqualTo(
                    FixedAssetNetBookValueEVM
                        .builder()
                        .rowIndex((long) (i + 1))
                        .assetNumber((long) (i + 1))
                        .serviceOutletCode("SOL_ID " + index)
                        .assetTag("ASSET_TAG " + index)
                        .assetDescription("ASSET_DESCRIPTION " + index)
                        .netBookValueDate(DATETIME_FORMATTER.format(of(2021, 1, 1).plusDays(Long.parseLong(index)).minusDays(1L)))
                        .assetCategory("ASSET_CATEGORY " + index)
                        .netBookValue(1.1 + i)
                        .depreciationRegime("DEPRECIATION REGIME " + index)
                        .build()
                );
        }
    }
}
