package io.github.erp.internal.framework.excel;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.IntegrationTest;
import io.github.erp.internal.model.DealerEVM;
import io.github.erp.internal.model.FixedAssetAcquisitionEVM;
import io.github.erp.internal.model.FixedAssetDepreciationEVM;
import io.github.erp.internal.model.FixedAssetNetBookValueEVM;
import io.github.erp.internal.model.InvoiceEVM;
import io.github.erp.internal.model.PaymentEVM;
import io.github.erp.internal.model.SignedPaymentEVM;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static io.github.erp.internal.framework.AppConstants.DATETIME_FORMATTER;
import static io.github.erp.internal.framework.excel.ExcelTestUtil.readFile;
import static io.github.erp.internal.framework.excel.ExcelTestUtil.toBytes;
import static java.time.LocalDate.of;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * If nothing is added in value for this test it confirms that the excel deserializer beans
 * are correctly configured. We are using the sample currency-table model from the internal
 * package but and if the user wishes additional tests can be added for custom data models.
 * The deserializer should work after being successfully injected into this test from a spring container.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ExcelFileUtilsIT {

    @Autowired
    private ExcelFileDeserializer<FixedAssetAcquisitionEVM> fixedAssetAcquisitionExcelFileDeserializer;

    @Autowired
    private ExcelFileDeserializer<FixedAssetDepreciationEVM> fixedAssetDepreciationExcelFileDeserializer;

    @Autowired
    private ExcelFileDeserializer<FixedAssetNetBookValueEVM> fixedAssetNetBookValueEVMExcelFileDeserializer;

    @Test
    public void fixedAssetAcquisitionFile() throws Exception {

        List<FixedAssetAcquisitionEVM> evms = fixedAssetAcquisitionExcelFileDeserializer.deserialize(toBytes(readFile("assetAcquisitionList.xlsx")));

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

        List<FixedAssetDepreciationEVM> evms = fixedAssetDepreciationExcelFileDeserializer.deserialize(toBytes(readFile("assetDepreciationList.xlsx")));

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

        List<FixedAssetNetBookValueEVM> evms = fixedAssetNetBookValueEVMExcelFileDeserializer.deserialize(toBytes(readFile("assetNetBookValue.xlsx")));

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

    @Autowired
    private ExcelFileDeserializer<PaymentEVM> paymentExcelFileDeserializer;

    @Test
    public void paymentListFile() throws Exception {
        List<PaymentEVM> evms = paymentExcelFileDeserializer.deserialize(toBytes(readFile("payment.xlsx")));

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

    @Autowired
    private ExcelFileDeserializer<SignedPaymentEVM> signedPaymentExcelFileDeserializer;

    @Test
    public void signedPaymentListFile() throws Exception {

        List<SignedPaymentEVM> evms = signedPaymentExcelFileDeserializer.deserialize(toBytes(readFile("signedPayment.xlsx")));

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

    @Autowired
    private ExcelFileDeserializer<DealerEVM> dealerExcelFileDeserializer;

    @Test
    public void dealersListFile() throws Exception {

        List<DealerEVM> evms = dealerExcelFileDeserializer.deserialize(toBytes(readFile("dealer.xlsx")));

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

    @Autowired
    private ExcelFileDeserializer<InvoiceEVM> invoiceExcelFileDeserializer;

    @Test
    public void invoiceListFile() throws Exception {

        List<InvoiceEVM> evms = invoiceExcelFileDeserializer.deserialize(toBytes(readFile("invoice.xlsx")));

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
