///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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

export class LeaseModelMetadataComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-lease-model-metadata div table .btn-danger'));
  title = element.all(by.css('jhi-lease-model-metadata div h2#page-heading span')).first();
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

export class LeaseModelMetadataUpdatePage {
  pageTitle = element(by.id('jhi-lease-model-metadata-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  modelTitleInput = element(by.id('field_modelTitle'));
  modelVersionInput = element(by.id('field_modelVersion'));
  descriptionInput = element(by.id('field_description'));
  modelNotesInput = element(by.id('file_modelNotes'));
  annualDiscountingRateInput = element(by.id('field_annualDiscountingRate'));
  commencementDateInput = element(by.id('field_commencementDate'));
  terminalDateInput = element(by.id('field_terminalDate'));
  totalReportingPeriodsInput = element(by.id('field_totalReportingPeriods'));
  reportingPeriodsPerYearInput = element(by.id('field_reportingPeriodsPerYear'));
  settlementPeriodsPerYearInput = element(by.id('field_settlementPeriodsPerYear'));
  initialLiabilityAmountInput = element(by.id('field_initialLiabilityAmount'));
  initialROUAmountInput = element(by.id('field_initialROUAmount'));
  totalDepreciationPeriodsInput = element(by.id('field_totalDepreciationPeriods'));

  placeholderSelect = element(by.id('field_placeholder'));
  leaseMappingSelect = element(by.id('field_leaseMapping'));
  leaseContractSelect = element(by.id('field_leaseContract'));
  predecessorSelect = element(by.id('field_predecessor'));
  liabilityCurrencySelect = element(by.id('field_liabilityCurrency'));
  rouAssetCurrencySelect = element(by.id('field_rouAssetCurrency'));
  modelAttachmentsSelect = element(by.id('field_modelAttachments'));
  securityClearanceSelect = element(by.id('field_securityClearance'));
  leaseLiabilityAccountSelect = element(by.id('field_leaseLiabilityAccount'));
  interestPayableAccountSelect = element(by.id('field_interestPayableAccount'));
  interestExpenseAccountSelect = element(by.id('field_interestExpenseAccount'));
  rouAssetAccountSelect = element(by.id('field_rouAssetAccount'));
  rouDepreciationAccountSelect = element(by.id('field_rouDepreciationAccount'));
  accruedDepreciationAccountSelect = element(by.id('field_accruedDepreciationAccount'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setModelTitleInput(modelTitle: string): Promise<void> {
    await this.modelTitleInput.sendKeys(modelTitle);
  }

  async getModelTitleInput(): Promise<string> {
    return await this.modelTitleInput.getAttribute('value');
  }

  async setModelVersionInput(modelVersion: string): Promise<void> {
    await this.modelVersionInput.sendKeys(modelVersion);
  }

  async getModelVersionInput(): Promise<string> {
    return await this.modelVersionInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setModelNotesInput(modelNotes: string): Promise<void> {
    await this.modelNotesInput.sendKeys(modelNotes);
  }

  async getModelNotesInput(): Promise<string> {
    return await this.modelNotesInput.getAttribute('value');
  }

  async setAnnualDiscountingRateInput(annualDiscountingRate: string): Promise<void> {
    await this.annualDiscountingRateInput.sendKeys(annualDiscountingRate);
  }

  async getAnnualDiscountingRateInput(): Promise<string> {
    return await this.annualDiscountingRateInput.getAttribute('value');
  }

  async setCommencementDateInput(commencementDate: string): Promise<void> {
    await this.commencementDateInput.sendKeys(commencementDate);
  }

  async getCommencementDateInput(): Promise<string> {
    return await this.commencementDateInput.getAttribute('value');
  }

  async setTerminalDateInput(terminalDate: string): Promise<void> {
    await this.terminalDateInput.sendKeys(terminalDate);
  }

  async getTerminalDateInput(): Promise<string> {
    return await this.terminalDateInput.getAttribute('value');
  }

  async setTotalReportingPeriodsInput(totalReportingPeriods: string): Promise<void> {
    await this.totalReportingPeriodsInput.sendKeys(totalReportingPeriods);
  }

  async getTotalReportingPeriodsInput(): Promise<string> {
    return await this.totalReportingPeriodsInput.getAttribute('value');
  }

  async setReportingPeriodsPerYearInput(reportingPeriodsPerYear: string): Promise<void> {
    await this.reportingPeriodsPerYearInput.sendKeys(reportingPeriodsPerYear);
  }

  async getReportingPeriodsPerYearInput(): Promise<string> {
    return await this.reportingPeriodsPerYearInput.getAttribute('value');
  }

  async setSettlementPeriodsPerYearInput(settlementPeriodsPerYear: string): Promise<void> {
    await this.settlementPeriodsPerYearInput.sendKeys(settlementPeriodsPerYear);
  }

  async getSettlementPeriodsPerYearInput(): Promise<string> {
    return await this.settlementPeriodsPerYearInput.getAttribute('value');
  }

  async setInitialLiabilityAmountInput(initialLiabilityAmount: string): Promise<void> {
    await this.initialLiabilityAmountInput.sendKeys(initialLiabilityAmount);
  }

  async getInitialLiabilityAmountInput(): Promise<string> {
    return await this.initialLiabilityAmountInput.getAttribute('value');
  }

  async setInitialROUAmountInput(initialROUAmount: string): Promise<void> {
    await this.initialROUAmountInput.sendKeys(initialROUAmount);
  }

  async getInitialROUAmountInput(): Promise<string> {
    return await this.initialROUAmountInput.getAttribute('value');
  }

  async setTotalDepreciationPeriodsInput(totalDepreciationPeriods: string): Promise<void> {
    await this.totalDepreciationPeriodsInput.sendKeys(totalDepreciationPeriods);
  }

  async getTotalDepreciationPeriodsInput(): Promise<string> {
    return await this.totalDepreciationPeriodsInput.getAttribute('value');
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

  async leaseMappingSelectLastOption(): Promise<void> {
    await this.leaseMappingSelect.all(by.tagName('option')).last().click();
  }

  async leaseMappingSelectOption(option: string): Promise<void> {
    await this.leaseMappingSelect.sendKeys(option);
  }

  getLeaseMappingSelect(): ElementFinder {
    return this.leaseMappingSelect;
  }

  async getLeaseMappingSelectedOption(): Promise<string> {
    return await this.leaseMappingSelect.element(by.css('option:checked')).getText();
  }

  async leaseContractSelectLastOption(): Promise<void> {
    await this.leaseContractSelect.all(by.tagName('option')).last().click();
  }

  async leaseContractSelectOption(option: string): Promise<void> {
    await this.leaseContractSelect.sendKeys(option);
  }

  getLeaseContractSelect(): ElementFinder {
    return this.leaseContractSelect;
  }

  async getLeaseContractSelectedOption(): Promise<string> {
    return await this.leaseContractSelect.element(by.css('option:checked')).getText();
  }

  async predecessorSelectLastOption(): Promise<void> {
    await this.predecessorSelect.all(by.tagName('option')).last().click();
  }

  async predecessorSelectOption(option: string): Promise<void> {
    await this.predecessorSelect.sendKeys(option);
  }

  getPredecessorSelect(): ElementFinder {
    return this.predecessorSelect;
  }

  async getPredecessorSelectedOption(): Promise<string> {
    return await this.predecessorSelect.element(by.css('option:checked')).getText();
  }

  async liabilityCurrencySelectLastOption(): Promise<void> {
    await this.liabilityCurrencySelect.all(by.tagName('option')).last().click();
  }

  async liabilityCurrencySelectOption(option: string): Promise<void> {
    await this.liabilityCurrencySelect.sendKeys(option);
  }

  getLiabilityCurrencySelect(): ElementFinder {
    return this.liabilityCurrencySelect;
  }

  async getLiabilityCurrencySelectedOption(): Promise<string> {
    return await this.liabilityCurrencySelect.element(by.css('option:checked')).getText();
  }

  async rouAssetCurrencySelectLastOption(): Promise<void> {
    await this.rouAssetCurrencySelect.all(by.tagName('option')).last().click();
  }

  async rouAssetCurrencySelectOption(option: string): Promise<void> {
    await this.rouAssetCurrencySelect.sendKeys(option);
  }

  getRouAssetCurrencySelect(): ElementFinder {
    return this.rouAssetCurrencySelect;
  }

  async getRouAssetCurrencySelectedOption(): Promise<string> {
    return await this.rouAssetCurrencySelect.element(by.css('option:checked')).getText();
  }

  async modelAttachmentsSelectLastOption(): Promise<void> {
    await this.modelAttachmentsSelect.all(by.tagName('option')).last().click();
  }

  async modelAttachmentsSelectOption(option: string): Promise<void> {
    await this.modelAttachmentsSelect.sendKeys(option);
  }

  getModelAttachmentsSelect(): ElementFinder {
    return this.modelAttachmentsSelect;
  }

  async getModelAttachmentsSelectedOption(): Promise<string> {
    return await this.modelAttachmentsSelect.element(by.css('option:checked')).getText();
  }

  async securityClearanceSelectLastOption(): Promise<void> {
    await this.securityClearanceSelect.all(by.tagName('option')).last().click();
  }

  async securityClearanceSelectOption(option: string): Promise<void> {
    await this.securityClearanceSelect.sendKeys(option);
  }

  getSecurityClearanceSelect(): ElementFinder {
    return this.securityClearanceSelect;
  }

  async getSecurityClearanceSelectedOption(): Promise<string> {
    return await this.securityClearanceSelect.element(by.css('option:checked')).getText();
  }

  async leaseLiabilityAccountSelectLastOption(): Promise<void> {
    await this.leaseLiabilityAccountSelect.all(by.tagName('option')).last().click();
  }

  async leaseLiabilityAccountSelectOption(option: string): Promise<void> {
    await this.leaseLiabilityAccountSelect.sendKeys(option);
  }

  getLeaseLiabilityAccountSelect(): ElementFinder {
    return this.leaseLiabilityAccountSelect;
  }

  async getLeaseLiabilityAccountSelectedOption(): Promise<string> {
    return await this.leaseLiabilityAccountSelect.element(by.css('option:checked')).getText();
  }

  async interestPayableAccountSelectLastOption(): Promise<void> {
    await this.interestPayableAccountSelect.all(by.tagName('option')).last().click();
  }

  async interestPayableAccountSelectOption(option: string): Promise<void> {
    await this.interestPayableAccountSelect.sendKeys(option);
  }

  getInterestPayableAccountSelect(): ElementFinder {
    return this.interestPayableAccountSelect;
  }

  async getInterestPayableAccountSelectedOption(): Promise<string> {
    return await this.interestPayableAccountSelect.element(by.css('option:checked')).getText();
  }

  async interestExpenseAccountSelectLastOption(): Promise<void> {
    await this.interestExpenseAccountSelect.all(by.tagName('option')).last().click();
  }

  async interestExpenseAccountSelectOption(option: string): Promise<void> {
    await this.interestExpenseAccountSelect.sendKeys(option);
  }

  getInterestExpenseAccountSelect(): ElementFinder {
    return this.interestExpenseAccountSelect;
  }

  async getInterestExpenseAccountSelectedOption(): Promise<string> {
    return await this.interestExpenseAccountSelect.element(by.css('option:checked')).getText();
  }

  async rouAssetAccountSelectLastOption(): Promise<void> {
    await this.rouAssetAccountSelect.all(by.tagName('option')).last().click();
  }

  async rouAssetAccountSelectOption(option: string): Promise<void> {
    await this.rouAssetAccountSelect.sendKeys(option);
  }

  getRouAssetAccountSelect(): ElementFinder {
    return this.rouAssetAccountSelect;
  }

  async getRouAssetAccountSelectedOption(): Promise<string> {
    return await this.rouAssetAccountSelect.element(by.css('option:checked')).getText();
  }

  async rouDepreciationAccountSelectLastOption(): Promise<void> {
    await this.rouDepreciationAccountSelect.all(by.tagName('option')).last().click();
  }

  async rouDepreciationAccountSelectOption(option: string): Promise<void> {
    await this.rouDepreciationAccountSelect.sendKeys(option);
  }

  getRouDepreciationAccountSelect(): ElementFinder {
    return this.rouDepreciationAccountSelect;
  }

  async getRouDepreciationAccountSelectedOption(): Promise<string> {
    return await this.rouDepreciationAccountSelect.element(by.css('option:checked')).getText();
  }

  async accruedDepreciationAccountSelectLastOption(): Promise<void> {
    await this.accruedDepreciationAccountSelect.all(by.tagName('option')).last().click();
  }

  async accruedDepreciationAccountSelectOption(option: string): Promise<void> {
    await this.accruedDepreciationAccountSelect.sendKeys(option);
  }

  getAccruedDepreciationAccountSelect(): ElementFinder {
    return this.accruedDepreciationAccountSelect;
  }

  async getAccruedDepreciationAccountSelectedOption(): Promise<string> {
    return await this.accruedDepreciationAccountSelect.element(by.css('option:checked')).getText();
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

export class LeaseModelMetadataDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-leaseModelMetadata-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-leaseModelMetadata'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
