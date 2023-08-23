package io.github.erp.internal.service;

/*-
 * Erp System - Mark IV No 5 (Ehud Series) Server ver 1.3.6
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
import io.github.erp.internal.framework.excel.DefaultExcelFileDeserializer;
import io.github.erp.internal.framework.excel.ExcelFileDeserializer;
import io.github.erp.internal.model.*;
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

    @Bean("paymentLabelExcelFileDeserializer")
    public ExcelFileDeserializer<PaymentLabelEVM> paymentLabelExcelFileDeserializer() {
        return excelFile -> new DefaultExcelFileDeserializer<>(PaymentLabelEVM.class, getDefaultPoijiOptions()).deserialize(excelFile);
    }

    @Bean("paymentCategoryExcelFileDeserializer")
    public ExcelFileDeserializer<PaymentCategoryEVM> paymentCategoryExcelFileDeserializer() {
        return excelFile -> new DefaultExcelFileDeserializer<>(PaymentCategoryEVM.class, getDefaultPoijiOptions()).deserialize(excelFile);
    }

    @Bean("paymentExcelFileDeserializer")
    public ExcelFileDeserializer<PaymentEVM> paymentExcelFileDeserializer() {
        return excelFile -> new DefaultExcelFileDeserializer<>(PaymentEVM.class, getDefaultPoijiOptions()).deserialize(excelFile);
    }

    @Bean("dealerExcelFileDeserializer")
    public ExcelFileDeserializer<DealerEVM> dealerExcelFileDeserializer() {
        return excelFile -> new DefaultExcelFileDeserializer<>(DealerEVM.class, getDefaultPoijiOptions()).deserialize(excelFile);
    }

    @Bean("signedPaymentExcelFileDeserializer")
    public ExcelFileDeserializer<SignedPaymentEVM> signedPaymentExcelFileDeserializer() {
        return excelFile -> new DefaultExcelFileDeserializer<>(SignedPaymentEVM.class, getDefaultPoijiOptions()).deserialize(excelFile);
    }

    @Bean("invoiceExcelFileDeserializer")
    public ExcelFileDeserializer<InvoiceEVM> invoiceExcelFileDeserializer() {
        return excelFile -> new DefaultExcelFileDeserializer<>(InvoiceEVM.class, getDefaultPoijiOptions()).deserialize(excelFile);
    }
}
