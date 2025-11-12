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

export class PerformanceOfForeignSubsidiariesComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-performance-of-foreign-subsidiaries div table .btn-danger'));
  title = element.all(by.css('jhi-performance-of-foreign-subsidiaries div h2#page-heading span')).first();
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

export class PerformanceOfForeignSubsidiariesUpdatePage {
  pageTitle = element(by.id('jhi-performance-of-foreign-subsidiaries-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  subsidiaryNameInput = element(by.id('field_subsidiaryName'));
  reportingDateInput = element(by.id('field_reportingDate'));
  subsidiaryIdInput = element(by.id('field_subsidiaryId'));
  grossLoansAmountInput = element(by.id('field_grossLoansAmount'));
  grossNPALoanAmountInput = element(by.id('field_grossNPALoanAmount'));
  grossAssetsAmountInput = element(by.id('field_grossAssetsAmount'));
  grossDepositsAmountInput = element(by.id('field_grossDepositsAmount'));
  profitBeforeTaxInput = element(by.id('field_profitBeforeTax'));
  totalCapitalAdequacyRatioInput = element(by.id('field_totalCapitalAdequacyRatio'));
  liquidityRatioInput = element(by.id('field_liquidityRatio'));
  generalProvisionsInput = element(by.id('field_generalProvisions'));
  specificProvisionsInput = element(by.id('field_specificProvisions'));
  interestInSuspenseAmountInput = element(by.id('field_interestInSuspenseAmount'));
  totalNumberOfStaffInput = element(by.id('field_totalNumberOfStaff'));
  numberOfBranchesInput = element(by.id('field_numberOfBranches'));

  bankCodeSelect = element(by.id('field_bankCode'));
  subsidiaryCountryCodeSelect = element(by.id('field_subsidiaryCountryCode'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setSubsidiaryNameInput(subsidiaryName: string): Promise<void> {
    await this.subsidiaryNameInput.sendKeys(subsidiaryName);
  }

  async getSubsidiaryNameInput(): Promise<string> {
    return await this.subsidiaryNameInput.getAttribute('value');
  }

  async setReportingDateInput(reportingDate: string): Promise<void> {
    await this.reportingDateInput.sendKeys(reportingDate);
  }

  async getReportingDateInput(): Promise<string> {
    return await this.reportingDateInput.getAttribute('value');
  }

  async setSubsidiaryIdInput(subsidiaryId: string): Promise<void> {
    await this.subsidiaryIdInput.sendKeys(subsidiaryId);
  }

  async getSubsidiaryIdInput(): Promise<string> {
    return await this.subsidiaryIdInput.getAttribute('value');
  }

  async setGrossLoansAmountInput(grossLoansAmount: string): Promise<void> {
    await this.grossLoansAmountInput.sendKeys(grossLoansAmount);
  }

  async getGrossLoansAmountInput(): Promise<string> {
    return await this.grossLoansAmountInput.getAttribute('value');
  }

  async setGrossNPALoanAmountInput(grossNPALoanAmount: string): Promise<void> {
    await this.grossNPALoanAmountInput.sendKeys(grossNPALoanAmount);
  }

  async getGrossNPALoanAmountInput(): Promise<string> {
    return await this.grossNPALoanAmountInput.getAttribute('value');
  }

  async setGrossAssetsAmountInput(grossAssetsAmount: string): Promise<void> {
    await this.grossAssetsAmountInput.sendKeys(grossAssetsAmount);
  }

  async getGrossAssetsAmountInput(): Promise<string> {
    return await this.grossAssetsAmountInput.getAttribute('value');
  }

  async setGrossDepositsAmountInput(grossDepositsAmount: string): Promise<void> {
    await this.grossDepositsAmountInput.sendKeys(grossDepositsAmount);
  }

  async getGrossDepositsAmountInput(): Promise<string> {
    return await this.grossDepositsAmountInput.getAttribute('value');
  }

  async setProfitBeforeTaxInput(profitBeforeTax: string): Promise<void> {
    await this.profitBeforeTaxInput.sendKeys(profitBeforeTax);
  }

  async getProfitBeforeTaxInput(): Promise<string> {
    return await this.profitBeforeTaxInput.getAttribute('value');
  }

  async setTotalCapitalAdequacyRatioInput(totalCapitalAdequacyRatio: string): Promise<void> {
    await this.totalCapitalAdequacyRatioInput.sendKeys(totalCapitalAdequacyRatio);
  }

  async getTotalCapitalAdequacyRatioInput(): Promise<string> {
    return await this.totalCapitalAdequacyRatioInput.getAttribute('value');
  }

  async setLiquidityRatioInput(liquidityRatio: string): Promise<void> {
    await this.liquidityRatioInput.sendKeys(liquidityRatio);
  }

  async getLiquidityRatioInput(): Promise<string> {
    return await this.liquidityRatioInput.getAttribute('value');
  }

  async setGeneralProvisionsInput(generalProvisions: string): Promise<void> {
    await this.generalProvisionsInput.sendKeys(generalProvisions);
  }

  async getGeneralProvisionsInput(): Promise<string> {
    return await this.generalProvisionsInput.getAttribute('value');
  }

  async setSpecificProvisionsInput(specificProvisions: string): Promise<void> {
    await this.specificProvisionsInput.sendKeys(specificProvisions);
  }

  async getSpecificProvisionsInput(): Promise<string> {
    return await this.specificProvisionsInput.getAttribute('value');
  }

  async setInterestInSuspenseAmountInput(interestInSuspenseAmount: string): Promise<void> {
    await this.interestInSuspenseAmountInput.sendKeys(interestInSuspenseAmount);
  }

  async getInterestInSuspenseAmountInput(): Promise<string> {
    return await this.interestInSuspenseAmountInput.getAttribute('value');
  }

  async setTotalNumberOfStaffInput(totalNumberOfStaff: string): Promise<void> {
    await this.totalNumberOfStaffInput.sendKeys(totalNumberOfStaff);
  }

  async getTotalNumberOfStaffInput(): Promise<string> {
    return await this.totalNumberOfStaffInput.getAttribute('value');
  }

  async setNumberOfBranchesInput(numberOfBranches: string): Promise<void> {
    await this.numberOfBranchesInput.sendKeys(numberOfBranches);
  }

  async getNumberOfBranchesInput(): Promise<string> {
    return await this.numberOfBranchesInput.getAttribute('value');
  }

  async bankCodeSelectLastOption(): Promise<void> {
    await this.bankCodeSelect.all(by.tagName('option')).last().click();
  }

  async bankCodeSelectOption(option: string): Promise<void> {
    await this.bankCodeSelect.sendKeys(option);
  }

  getBankCodeSelect(): ElementFinder {
    return this.bankCodeSelect;
  }

  async getBankCodeSelectedOption(): Promise<string> {
    return await this.bankCodeSelect.element(by.css('option:checked')).getText();
  }

  async subsidiaryCountryCodeSelectLastOption(): Promise<void> {
    await this.subsidiaryCountryCodeSelect.all(by.tagName('option')).last().click();
  }

  async subsidiaryCountryCodeSelectOption(option: string): Promise<void> {
    await this.subsidiaryCountryCodeSelect.sendKeys(option);
  }

  getSubsidiaryCountryCodeSelect(): ElementFinder {
    return this.subsidiaryCountryCodeSelect;
  }

  async getSubsidiaryCountryCodeSelectedOption(): Promise<string> {
    return await this.subsidiaryCountryCodeSelect.element(by.css('option:checked')).getText();
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

export class PerformanceOfForeignSubsidiariesDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-performanceOfForeignSubsidiaries-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-performanceOfForeignSubsidiaries'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
