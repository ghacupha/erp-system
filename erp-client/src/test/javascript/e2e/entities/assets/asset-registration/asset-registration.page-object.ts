///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { element, by, ElementFinder } from 'protractor';

export class AssetRegistrationComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-asset-registration div table .btn-danger'));
  title = element.all(by.css('jhi-asset-registration div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getText();
  }
}

export class AssetRegistrationUpdatePage {
  pageTitle = element(by.id('jhi-asset-registration-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  assetNumberInput = element(by.id('field_assetNumber'));
  assetTagInput = element(by.id('field_assetTag'));
  assetDetailsInput = element(by.id('field_assetDetails'));
  assetCostInput = element(by.id('field_assetCost'));
  commentsInput = element(by.id('file_comments'));
  modelNumberInput = element(by.id('field_modelNumber'));
  serialNumberInput = element(by.id('field_serialNumber'));
  remarksInput = element(by.id('field_remarks'));
  capitalizationDateInput = element(by.id('field_capitalizationDate'));
  historicalCostInput = element(by.id('field_historicalCost'));
  registrationDateInput = element(by.id('field_registrationDate'));

  placeholderSelect = element(by.id('field_placeholder'));
  paymentInvoicesSelect = element(by.id('field_paymentInvoices'));
  otherRelatedServiceOutletsSelect = element(by.id('field_otherRelatedServiceOutlets'));
  otherRelatedSettlementsSelect = element(by.id('field_otherRelatedSettlements'));
  assetCategorySelect = element(by.id('field_assetCategory'));
  purchaseOrderSelect = element(by.id('field_purchaseOrder'));
  deliveryNoteSelect = element(by.id('field_deliveryNote'));
  jobSheetSelect = element(by.id('field_jobSheet'));
  dealerSelect = element(by.id('field_dealer'));
  designatedUsersSelect = element(by.id('field_designatedUsers'));
  settlementCurrencySelect = element(by.id('field_settlementCurrency'));
  businessDocumentSelect = element(by.id('field_businessDocument'));
  assetWarrantySelect = element(by.id('field_assetWarranty'));
  universallyUniqueMappingSelect = element(by.id('field_universallyUniqueMapping'));
  assetAccessorySelect = element(by.id('field_assetAccessory'));
  mainServiceOutletSelect = element(by.id('field_mainServiceOutlet'));
  acquiringTransactionSelect = element(by.id('field_acquiringTransaction'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setAssetNumberInput(assetNumber: string): Promise<void> {
    await this.assetNumberInput.sendKeys(assetNumber);
  }

  async getAssetNumberInput(): Promise<string> {
    return await this.assetNumberInput.getAttribute('value');
  }

  async setAssetTagInput(assetTag: string): Promise<void> {
    await this.assetTagInput.sendKeys(assetTag);
  }

  async getAssetTagInput(): Promise<string> {
    return await this.assetTagInput.getAttribute('value');
  }

  async setAssetDetailsInput(assetDetails: string): Promise<void> {
    await this.assetDetailsInput.sendKeys(assetDetails);
  }

  async getAssetDetailsInput(): Promise<string> {
    return await this.assetDetailsInput.getAttribute('value');
  }

  async setAssetCostInput(assetCost: string): Promise<void> {
    await this.assetCostInput.sendKeys(assetCost);
  }

  async getAssetCostInput(): Promise<string> {
    return await this.assetCostInput.getAttribute('value');
  }

  async setCommentsInput(comments: string): Promise<void> {
    await this.commentsInput.sendKeys(comments);
  }

  async getCommentsInput(): Promise<string> {
    return await this.commentsInput.getAttribute('value');
  }

  async setModelNumberInput(modelNumber: string): Promise<void> {
    await this.modelNumberInput.sendKeys(modelNumber);
  }

  async getModelNumberInput(): Promise<string> {
    return await this.modelNumberInput.getAttribute('value');
  }

  async setSerialNumberInput(serialNumber: string): Promise<void> {
    await this.serialNumberInput.sendKeys(serialNumber);
  }

  async getSerialNumberInput(): Promise<string> {
    return await this.serialNumberInput.getAttribute('value');
  }

  async setRemarksInput(remarks: string): Promise<void> {
    await this.remarksInput.sendKeys(remarks);
  }

  async getRemarksInput(): Promise<string> {
    return await this.remarksInput.getAttribute('value');
  }

  async setCapitalizationDateInput(capitalizationDate: string): Promise<void> {
    await this.capitalizationDateInput.sendKeys(capitalizationDate);
  }

  async getCapitalizationDateInput(): Promise<string> {
    return await this.capitalizationDateInput.getAttribute('value');
  }

  async setHistoricalCostInput(historicalCost: string): Promise<void> {
    await this.historicalCostInput.sendKeys(historicalCost);
  }

  async getHistoricalCostInput(): Promise<string> {
    return await this.historicalCostInput.getAttribute('value');
  }

  async setRegistrationDateInput(registrationDate: string): Promise<void> {
    await this.registrationDateInput.sendKeys(registrationDate);
  }

  async getRegistrationDateInput(): Promise<string> {
    return await this.registrationDateInput.getAttribute('value');
  }

  async placeholderSelectLastOption(): Promise<void> {
    await this.placeholderSelect.all(by.tagName('option')).last().click();
  }

  async placeholderSelectOption(option: string): Promise<void> {
    await this.placeholderSelect.sendKeys(option);
  }

  getPlaceholderSelect(): ElementFinder {
    return this.placeholderSelect;
  }

  async getPlaceholderSelectedOption(): Promise<string> {
    return await this.placeholderSelect.element(by.css('option:checked')).getText();
  }

  async paymentInvoicesSelectLastOption(): Promise<void> {
    await this.paymentInvoicesSelect.all(by.tagName('option')).last().click();
  }

  async paymentInvoicesSelectOption(option: string): Promise<void> {
    await this.paymentInvoicesSelect.sendKeys(option);
  }

  getPaymentInvoicesSelect(): ElementFinder {
    return this.paymentInvoicesSelect;
  }

  async getPaymentInvoicesSelectedOption(): Promise<string> {
    return await this.paymentInvoicesSelect.element(by.css('option:checked')).getText();
  }

  async otherRelatedServiceOutletsSelectLastOption(): Promise<void> {
    await this.otherRelatedServiceOutletsSelect.all(by.tagName('option')).last().click();
  }

  async otherRelatedServiceOutletsSelectOption(option: string): Promise<void> {
    await this.otherRelatedServiceOutletsSelect.sendKeys(option);
  }

  getOtherRelatedServiceOutletsSelect(): ElementFinder {
    return this.otherRelatedServiceOutletsSelect;
  }

  async getOtherRelatedServiceOutletsSelectedOption(): Promise<string> {
    return await this.otherRelatedServiceOutletsSelect.element(by.css('option:checked')).getText();
  }

  async otherRelatedSettlementsSelectLastOption(): Promise<void> {
    await this.otherRelatedSettlementsSelect.all(by.tagName('option')).last().click();
  }

  async otherRelatedSettlementsSelectOption(option: string): Promise<void> {
    await this.otherRelatedSettlementsSelect.sendKeys(option);
  }

  getOtherRelatedSettlementsSelect(): ElementFinder {
    return this.otherRelatedSettlementsSelect;
  }

  async getOtherRelatedSettlementsSelectedOption(): Promise<string> {
    return await this.otherRelatedSettlementsSelect.element(by.css('option:checked')).getText();
  }

  async assetCategorySelectLastOption(): Promise<void> {
    await this.assetCategorySelect.all(by.tagName('option')).last().click();
  }

  async assetCategorySelectOption(option: string): Promise<void> {
    await this.assetCategorySelect.sendKeys(option);
  }

  getAssetCategorySelect(): ElementFinder {
    return this.assetCategorySelect;
  }

  async getAssetCategorySelectedOption(): Promise<string> {
    return await this.assetCategorySelect.element(by.css('option:checked')).getText();
  }

  async purchaseOrderSelectLastOption(): Promise<void> {
    await this.purchaseOrderSelect.all(by.tagName('option')).last().click();
  }

  async purchaseOrderSelectOption(option: string): Promise<void> {
    await this.purchaseOrderSelect.sendKeys(option);
  }

  getPurchaseOrderSelect(): ElementFinder {
    return this.purchaseOrderSelect;
  }

  async getPurchaseOrderSelectedOption(): Promise<string> {
    return await this.purchaseOrderSelect.element(by.css('option:checked')).getText();
  }

  async deliveryNoteSelectLastOption(): Promise<void> {
    await this.deliveryNoteSelect.all(by.tagName('option')).last().click();
  }

  async deliveryNoteSelectOption(option: string): Promise<void> {
    await this.deliveryNoteSelect.sendKeys(option);
  }

  getDeliveryNoteSelect(): ElementFinder {
    return this.deliveryNoteSelect;
  }

  async getDeliveryNoteSelectedOption(): Promise<string> {
    return await this.deliveryNoteSelect.element(by.css('option:checked')).getText();
  }

  async jobSheetSelectLastOption(): Promise<void> {
    await this.jobSheetSelect.all(by.tagName('option')).last().click();
  }

  async jobSheetSelectOption(option: string): Promise<void> {
    await this.jobSheetSelect.sendKeys(option);
  }

  getJobSheetSelect(): ElementFinder {
    return this.jobSheetSelect;
  }

  async getJobSheetSelectedOption(): Promise<string> {
    return await this.jobSheetSelect.element(by.css('option:checked')).getText();
  }

  async dealerSelectLastOption(): Promise<void> {
    await this.dealerSelect.all(by.tagName('option')).last().click();
  }

  async dealerSelectOption(option: string): Promise<void> {
    await this.dealerSelect.sendKeys(option);
  }

  getDealerSelect(): ElementFinder {
    return this.dealerSelect;
  }

  async getDealerSelectedOption(): Promise<string> {
    return await this.dealerSelect.element(by.css('option:checked')).getText();
  }

  async designatedUsersSelectLastOption(): Promise<void> {
    await this.designatedUsersSelect.all(by.tagName('option')).last().click();
  }

  async designatedUsersSelectOption(option: string): Promise<void> {
    await this.designatedUsersSelect.sendKeys(option);
  }

  getDesignatedUsersSelect(): ElementFinder {
    return this.designatedUsersSelect;
  }

  async getDesignatedUsersSelectedOption(): Promise<string> {
    return await this.designatedUsersSelect.element(by.css('option:checked')).getText();
  }

  async settlementCurrencySelectLastOption(): Promise<void> {
    await this.settlementCurrencySelect.all(by.tagName('option')).last().click();
  }

  async settlementCurrencySelectOption(option: string): Promise<void> {
    await this.settlementCurrencySelect.sendKeys(option);
  }

  getSettlementCurrencySelect(): ElementFinder {
    return this.settlementCurrencySelect;
  }

  async getSettlementCurrencySelectedOption(): Promise<string> {
    return await this.settlementCurrencySelect.element(by.css('option:checked')).getText();
  }

  async businessDocumentSelectLastOption(): Promise<void> {
    await this.businessDocumentSelect.all(by.tagName('option')).last().click();
  }

  async businessDocumentSelectOption(option: string): Promise<void> {
    await this.businessDocumentSelect.sendKeys(option);
  }

  getBusinessDocumentSelect(): ElementFinder {
    return this.businessDocumentSelect;
  }

  async getBusinessDocumentSelectedOption(): Promise<string> {
    return await this.businessDocumentSelect.element(by.css('option:checked')).getText();
  }

  async assetWarrantySelectLastOption(): Promise<void> {
    await this.assetWarrantySelect.all(by.tagName('option')).last().click();
  }

  async assetWarrantySelectOption(option: string): Promise<void> {
    await this.assetWarrantySelect.sendKeys(option);
  }

  getAssetWarrantySelect(): ElementFinder {
    return this.assetWarrantySelect;
  }

  async getAssetWarrantySelectedOption(): Promise<string> {
    return await this.assetWarrantySelect.element(by.css('option:checked')).getText();
  }

  async universallyUniqueMappingSelectLastOption(): Promise<void> {
    await this.universallyUniqueMappingSelect.all(by.tagName('option')).last().click();
  }

  async universallyUniqueMappingSelectOption(option: string): Promise<void> {
    await this.universallyUniqueMappingSelect.sendKeys(option);
  }

  getUniversallyUniqueMappingSelect(): ElementFinder {
    return this.universallyUniqueMappingSelect;
  }

  async getUniversallyUniqueMappingSelectedOption(): Promise<string> {
    return await this.universallyUniqueMappingSelect.element(by.css('option:checked')).getText();
  }

  async assetAccessorySelectLastOption(): Promise<void> {
    await this.assetAccessorySelect.all(by.tagName('option')).last().click();
  }

  async assetAccessorySelectOption(option: string): Promise<void> {
    await this.assetAccessorySelect.sendKeys(option);
  }

  getAssetAccessorySelect(): ElementFinder {
    return this.assetAccessorySelect;
  }

  async getAssetAccessorySelectedOption(): Promise<string> {
    return await this.assetAccessorySelect.element(by.css('option:checked')).getText();
  }

  async mainServiceOutletSelectLastOption(): Promise<void> {
    await this.mainServiceOutletSelect.all(by.tagName('option')).last().click();
  }

  async mainServiceOutletSelectOption(option: string): Promise<void> {
    await this.mainServiceOutletSelect.sendKeys(option);
  }

  getMainServiceOutletSelect(): ElementFinder {
    return this.mainServiceOutletSelect;
  }

  async getMainServiceOutletSelectedOption(): Promise<string> {
    return await this.mainServiceOutletSelect.element(by.css('option:checked')).getText();
  }

  async acquiringTransactionSelectLastOption(): Promise<void> {
    await this.acquiringTransactionSelect.all(by.tagName('option')).last().click();
  }

  async acquiringTransactionSelectOption(option: string): Promise<void> {
    await this.acquiringTransactionSelect.sendKeys(option);
  }

  getAcquiringTransactionSelect(): ElementFinder {
    return this.acquiringTransactionSelect;
  }

  async getAcquiringTransactionSelectedOption(): Promise<string> {
    return await this.acquiringTransactionSelect.element(by.css('option:checked')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class AssetRegistrationDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-assetRegistration-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-assetRegistration'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
