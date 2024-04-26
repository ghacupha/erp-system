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
import com.poiji.annotation.ExcelCell;
import io.github.erp.internal.model.*;
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
        ExcelFileDeserializer<FixedAssetAcquisitionEVM> deserializer = container.fixedAssetAcquisitionExcelFileDeserializer();

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
        ExcelFileDeserializer<FixedAssetDepreciationEVM> deserializer = container.fixedAssetDepreciationExcelFileDeserializer();

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
        ExcelFileDeserializer<FixedAssetNetBookValueEVM> deserializer = container.fixedAssetNetBookValueExcelFileDeserializer();

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

    @Test
    public void paymentLabelListFile() throws Exception {
        ExcelFileDeserializer<PaymentLabelEVM> deserializer = container.paymentLabelExcelFileDeserializer();

        List<PaymentLabelEVM> evms = deserializer.deserialize(toBytes(readFile("paymentLabel.xlsx")));

        assertThat(evms.size()).isEqualTo(13);

        for (int i = 0; i < 13; i++) {
            String index = String.valueOf(i + 1);
            assertThat(evms.get(i))
                .isEqualTo(
                    PaymentLabelEVM
                        .builder()
                        .rowIndex((long) (i + 1))
                        .description("paymentDescription" + index)
                        .comments("paymentComment" + index)
                        .build()
                );
        }
    }
    @Test
    public void paymentCategoryListFile() throws Exception {
        ExcelFileDeserializer<PaymentCategoryEVM> deserializer = container.paymentCategoryExcelFileDeserializer();

        List<PaymentCategoryEVM> evms = deserializer.deserialize(toBytes(readFile("paymentCategory.xlsx")));

        assertThat(evms.size()).isEqualTo(13);

        for (int i = 0; i < 13; i++) {
            String index = String.valueOf(i + 1);
            assertThat(evms.get(i))
                .isEqualTo(
                    PaymentCategoryEVM
                        .builder()
                        .rowIndex((long) (i + 1))
                        .categoryName("categoryName" + index)
                        .categoryDescription("categoryDescription" + index)
                        .categoryType("categoryType" + index)
                        .build()
                );
        }
    }
    @Test
    public void paymentListFile() throws Exception {
        ExcelFileDeserializer<PaymentEVM> deserializer = container.paymentExcelFileDeserializer();

        List<PaymentEVM> evms = deserializer.deserialize(toBytes(readFile("payment.xlsx")));

        assertThat(evms.size()).isEqualTo(13);

        for (int i = 0; i < 13; i++) {
            String index = String.valueOf(i + 1);
            assertThat(evms.get(i))
                .isEqualTo(
                    PaymentEVM
                        .builder()
                        .rowIndex((long) (i + 1))
                        .paymentNumber("paymentNumber" + index)
                        .paymentDate(DATETIME_FORMATTER.format(of(2021, 1, 1).plusDays(Long.parseLong(index)).minusDays(1L)))
                        .invoicedAmount(1.1 + i)
                        .paymentAmount(1.1 + i)
                        .description("description" + index)
                        .settlementCurrency("settlementCurrency" + index)
                        .dealerName("dealerName" + index)
                        .purchaseOrderNumber("purchaseOrderNumber" + index)
                        .build()
                );
        }
    }
    @Test
    public void signedPaymentListFile() throws Exception {
        ExcelFileDeserializer<SignedPaymentEVM> deserializer = container.signedPaymentExcelFileDeserializer();

        List<SignedPaymentEVM> evms = deserializer.deserialize(toBytes(readFile("signedPayment.xlsx")));

        assertThat(evms.size()).isEqualTo(13);

        for (int i = 0; i < 13; i++) {
            String index = String.valueOf(i + 1);
            assertThat(evms.get(i))
                .isEqualTo(
                    SignedPaymentEVM
                        .builder()
                        .rowIndex((long) (i + 1))
                        .transactionNumber("transactionNumber" + index)
                        .transactionDate(DATETIME_FORMATTER.format(of(2021, 1, 1).plusDays(Long.parseLong(index)).minusDays(1L)))
                        .transactionCurrency("transactionCurrency" + index)
                        .transactionAmount(1.1 + i)
                        .dealerName("dealerName" + index)
                        .build()
                );
        }
    }
    @Test
    public void dealersListFile() throws Exception {
        ExcelFileDeserializer<DealerEVM> deserializer = container.dealerExcelFileDeserializer();

        List<DealerEVM> evms = deserializer.deserialize(toBytes(readFile("dealer.xlsx")));

        assertThat(evms.size()).isEqualTo(13);

        for (int i = 0; i < 13; i++) {
            String index = String.valueOf(i + 1);
            assertThat(evms.get(i))
                .isEqualTo(
                    DealerEVM
                        .builder()
                        .rowIndex((long) (i + 1))
                        .dealerName("dealerName" + index)
                        .taxNumber("taxNumber" + index)
                        .postalAddress("postalAddress" + index)
                        .physicalAddress("physicalAddress" + index)
                        .accountName("accountName" + index)
                        .accountNumber("accountNumber" + index)
                        .bankersName("bankersName" + index)
                        .bankersBranch("bankersBranch" + index)
                        .bankersSwiftCode("bankersSwiftCode" + index)
                        .build()
                );
        }
    }
    @Test
    public void invoiceListFile() throws Exception {
        ExcelFileDeserializer<InvoiceEVM> deserializer = container.invoiceExcelFileDeserializer();

        List<InvoiceEVM> evms = deserializer.deserialize(toBytes(readFile("invoice.xlsx")));

        assertThat(evms.size()).isEqualTo(13);

        for (int i = 0; i < 13; i++) {
            String index = String.valueOf(i + 1);
            assertThat(evms.get(i))
                .isEqualTo(
                    InvoiceEVM
                        .builder()
                        .rowIndex((long) (i + 1))
                        .invoiceNumber("invoiceNumber" + index)
                        .invoiceDate(DATETIME_FORMATTER.format(of(2021, 1, 1).plusDays(Long.parseLong(index)).minusDays(1L)))
                        .invoiceAmount(1.1 + i)
                        .currency("currency" + index)
                        .paymentReference("paymentReference" + index)
                        .dealerName("dealerName" + index)
                        .build()
                );
        }
    }
}
