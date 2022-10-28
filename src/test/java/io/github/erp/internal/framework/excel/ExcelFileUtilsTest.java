package io.github.erp.internal.framework.excel;

/*-
 * Erp System - Mark III No 3 (Caleb Series) Server ver 0.1.3-SNAPSHOT
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
