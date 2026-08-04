package io.github.erp.domain;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import java.io.Serializable;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Search-only, flattened projection of {@link AssetAccessory} and everything it relates to -
 * same rationale and shape as {@link AssetRegistrationIndex} (see its doc comment): a fixed-name
 * index (not the entity's own, which - unlike AssetRegistration's - already happens to be fixed
 * too), matched-then-hydrated from Postgres, so Elasticsearch never has to hold, or fail to hold,
 * the real, deeply-relational {@code AssetAccessory} graph.
 */
@Document(indexName = "assetaccessoryindex")
public class AssetAccessoryIndex implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Keyword)
    private String assetTag;

    @Field(type = FieldType.Text)
    private String assetDetails;

    @Field(type = FieldType.Keyword)
    private String modelNumber;

    @Field(type = FieldType.Keyword)
    private String serialNumber;

    @Field(type = FieldType.Text)
    private String assetCategoryName;

    @Field(type = FieldType.Text)
    private String dealerName;

    @Field(type = FieldType.Text)
    private String mainServiceOutletName;

    @Field(type = FieldType.Text)
    private List<String> designatedUserNames;

    @Field(type = FieldType.Text)
    private List<String> serviceOutletNames;

    @Field(type = FieldType.Keyword)
    private List<String> assetWarrantyNumbers;

    @Field(type = FieldType.Keyword)
    private List<String> paymentInvoiceNumbers;

    @Field(type = FieldType.Keyword)
    private List<String> purchaseOrderNumbers;

    @Field(type = FieldType.Keyword)
    private List<String> deliveryNoteNumbers;

    @Field(type = FieldType.Keyword)
    private List<String> jobSheetNumbers;

    @Field(type = FieldType.Text)
    private List<String> businessDocumentTitles;

    @Field(type = FieldType.Keyword)
    private List<String> settlementPaymentNumbers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssetTag() {
        return assetTag;
    }

    public void setAssetTag(String assetTag) {
        this.assetTag = assetTag;
    }

    public String getAssetDetails() {
        return assetDetails;
    }

    public void setAssetDetails(String assetDetails) {
        this.assetDetails = assetDetails;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getAssetCategoryName() {
        return assetCategoryName;
    }

    public void setAssetCategoryName(String assetCategoryName) {
        this.assetCategoryName = assetCategoryName;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getMainServiceOutletName() {
        return mainServiceOutletName;
    }

    public void setMainServiceOutletName(String mainServiceOutletName) {
        this.mainServiceOutletName = mainServiceOutletName;
    }

    public List<String> getDesignatedUserNames() {
        return designatedUserNames;
    }

    public void setDesignatedUserNames(List<String> designatedUserNames) {
        this.designatedUserNames = designatedUserNames;
    }

    public List<String> getServiceOutletNames() {
        return serviceOutletNames;
    }

    public void setServiceOutletNames(List<String> serviceOutletNames) {
        this.serviceOutletNames = serviceOutletNames;
    }

    public List<String> getAssetWarrantyNumbers() {
        return assetWarrantyNumbers;
    }

    public void setAssetWarrantyNumbers(List<String> assetWarrantyNumbers) {
        this.assetWarrantyNumbers = assetWarrantyNumbers;
    }

    public List<String> getPaymentInvoiceNumbers() {
        return paymentInvoiceNumbers;
    }

    public void setPaymentInvoiceNumbers(List<String> paymentInvoiceNumbers) {
        this.paymentInvoiceNumbers = paymentInvoiceNumbers;
    }

    public List<String> getPurchaseOrderNumbers() {
        return purchaseOrderNumbers;
    }

    public void setPurchaseOrderNumbers(List<String> purchaseOrderNumbers) {
        this.purchaseOrderNumbers = purchaseOrderNumbers;
    }

    public List<String> getDeliveryNoteNumbers() {
        return deliveryNoteNumbers;
    }

    public void setDeliveryNoteNumbers(List<String> deliveryNoteNumbers) {
        this.deliveryNoteNumbers = deliveryNoteNumbers;
    }

    public List<String> getJobSheetNumbers() {
        return jobSheetNumbers;
    }

    public void setJobSheetNumbers(List<String> jobSheetNumbers) {
        this.jobSheetNumbers = jobSheetNumbers;
    }

    public List<String> getBusinessDocumentTitles() {
        return businessDocumentTitles;
    }

    public void setBusinessDocumentTitles(List<String> businessDocumentTitles) {
        this.businessDocumentTitles = businessDocumentTitles;
    }

    public List<String> getSettlementPaymentNumbers() {
        return settlementPaymentNumbers;
    }

    public void setSettlementPaymentNumbers(List<String> settlementPaymentNumbers) {
        this.settlementPaymentNumbers = settlementPaymentNumbers;
    }
}
