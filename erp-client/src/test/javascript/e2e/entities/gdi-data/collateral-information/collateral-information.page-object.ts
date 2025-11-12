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

export class CollateralInformationComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-collateral-information div table .btn-danger'));
  title = element.all(by.css('jhi-collateral-information div h2#page-heading span')).first();
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

export class CollateralInformationUpdatePage {
  pageTitle = element(by.id('jhi-collateral-information-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  reportingDateInput = element(by.id('field_reportingDate'));
  collateralIdInput = element(by.id('field_collateralId'));
  loanContractIdInput = element(by.id('field_loanContractId'));
  customerIdInput = element(by.id('field_customerId'));
  registrationPropertyNumberInput = element(by.id('field_registrationPropertyNumber'));
  collateralOMVInCCYInput = element(by.id('field_collateralOMVInCCY'));
  collateralFSVInLCYInput = element(by.id('field_collateralFSVInLCY'));
  collateralDiscountedValueInput = element(by.id('field_collateralDiscountedValue'));
  amountChargedInput = element(by.id('field_amountCharged'));
  collateralDiscountRateInput = element(by.id('field_collateralDiscountRate'));
  loanToValueRatioInput = element(by.id('field_loanToValueRatio'));
  nameOfPropertyValuerInput = element(by.id('field_nameOfPropertyValuer'));
  collateralLastValuationDateInput = element(by.id('field_collateralLastValuationDate'));
  insuredFlagSelect = element(by.id('field_insuredFlag'));
  nameOfInsurerInput = element(by.id('field_nameOfInsurer'));
  amountInsuredInput = element(by.id('field_amountInsured'));
  insuranceExpiryDateInput = element(by.id('field_insuranceExpiryDate'));
  guaranteeInsurersInput = element(by.id('field_guaranteeInsurers'));

  bankCodeSelect = element(by.id('field_bankCode'));
  branchCodeSelect = element(by.id('field_branchCode'));
  collateralTypeSelect = element(by.id('field_collateralType'));
  countyCodeSelect = element(by.id('field_countyCode'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setReportingDateInput(reportingDate: string): Promise<void> {
    await this.reportingDateInput.sendKeys(reportingDate);
  }

  async getReportingDateInput(): Promise<string> {
    return await this.reportingDateInput.getAttribute('value');
  }

  async setCollateralIdInput(collateralId: string): Promise<void> {
    await this.collateralIdInput.sendKeys(collateralId);
  }

  async getCollateralIdInput(): Promise<string> {
    return await this.collateralIdInput.getAttribute('value');
  }

  async setLoanContractIdInput(loanContractId: string): Promise<void> {
    await this.loanContractIdInput.sendKeys(loanContractId);
  }

  async getLoanContractIdInput(): Promise<string> {
    return await this.loanContractIdInput.getAttribute('value');
  }

  async setCustomerIdInput(customerId: string): Promise<void> {
    await this.customerIdInput.sendKeys(customerId);
  }

  async getCustomerIdInput(): Promise<string> {
    return await this.customerIdInput.getAttribute('value');
  }

  async setRegistrationPropertyNumberInput(registrationPropertyNumber: string): Promise<void> {
    await this.registrationPropertyNumberInput.sendKeys(registrationPropertyNumber);
  }

  async getRegistrationPropertyNumberInput(): Promise<string> {
    return await this.registrationPropertyNumberInput.getAttribute('value');
  }

  async setCollateralOMVInCCYInput(collateralOMVInCCY: string): Promise<void> {
    await this.collateralOMVInCCYInput.sendKeys(collateralOMVInCCY);
  }

  async getCollateralOMVInCCYInput(): Promise<string> {
    return await this.collateralOMVInCCYInput.getAttribute('value');
  }

  async setCollateralFSVInLCYInput(collateralFSVInLCY: string): Promise<void> {
    await this.collateralFSVInLCYInput.sendKeys(collateralFSVInLCY);
  }

  async getCollateralFSVInLCYInput(): Promise<string> {
    return await this.collateralFSVInLCYInput.getAttribute('value');
  }

  async setCollateralDiscountedValueInput(collateralDiscountedValue: string): Promise<void> {
    await this.collateralDiscountedValueInput.sendKeys(collateralDiscountedValue);
  }

  async getCollateralDiscountedValueInput(): Promise<string> {
    return await this.collateralDiscountedValueInput.getAttribute('value');
  }

  async setAmountChargedInput(amountCharged: string): Promise<void> {
    await this.amountChargedInput.sendKeys(amountCharged);
  }

  async getAmountChargedInput(): Promise<string> {
    return await this.amountChargedInput.getAttribute('value');
  }

  async setCollateralDiscountRateInput(collateralDiscountRate: string): Promise<void> {
    await this.collateralDiscountRateInput.sendKeys(collateralDiscountRate);
  }

  async getCollateralDiscountRateInput(): Promise<string> {
    return await this.collateralDiscountRateInput.getAttribute('value');
  }

  async setLoanToValueRatioInput(loanToValueRatio: string): Promise<void> {
    await this.loanToValueRatioInput.sendKeys(loanToValueRatio);
  }

  async getLoanToValueRatioInput(): Promise<string> {
    return await this.loanToValueRatioInput.getAttribute('value');
  }

  async setNameOfPropertyValuerInput(nameOfPropertyValuer: string): Promise<void> {
    await this.nameOfPropertyValuerInput.sendKeys(nameOfPropertyValuer);
  }

  async getNameOfPropertyValuerInput(): Promise<string> {
    return await this.nameOfPropertyValuerInput.getAttribute('value');
  }

  async setCollateralLastValuationDateInput(collateralLastValuationDate: string): Promise<void> {
    await this.collateralLastValuationDateInput.sendKeys(collateralLastValuationDate);
  }

  async getCollateralLastValuationDateInput(): Promise<string> {
    return await this.collateralLastValuationDateInput.getAttribute('value');
  }

  async setInsuredFlagSelect(insuredFlag: string): Promise<void> {
    await this.insuredFlagSelect.sendKeys(insuredFlag);
  }

  async getInsuredFlagSelect(): Promise<string> {
    return await this.insuredFlagSelect.element(by.css('option:checked')).getText();
  }

  async insuredFlagSelectLastOption(): Promise<void> {
    await this.insuredFlagSelect.all(by.tagName('option')).last().click();
  }

  async setNameOfInsurerInput(nameOfInsurer: string): Promise<void> {
    await this.nameOfInsurerInput.sendKeys(nameOfInsurer);
  }

  async getNameOfInsurerInput(): Promise<string> {
    return await this.nameOfInsurerInput.getAttribute('value');
  }

  async setAmountInsuredInput(amountInsured: string): Promise<void> {
    await this.amountInsuredInput.sendKeys(amountInsured);
  }

  async getAmountInsuredInput(): Promise<string> {
    return await this.amountInsuredInput.getAttribute('value');
  }

  async setInsuranceExpiryDateInput(insuranceExpiryDate: string): Promise<void> {
    await this.insuranceExpiryDateInput.sendKeys(insuranceExpiryDate);
  }

  async getInsuranceExpiryDateInput(): Promise<string> {
    return await this.insuranceExpiryDateInput.getAttribute('value');
  }

  async setGuaranteeInsurersInput(guaranteeInsurers: string): Promise<void> {
    await this.guaranteeInsurersInput.sendKeys(guaranteeInsurers);
  }

  async getGuaranteeInsurersInput(): Promise<string> {
    return await this.guaranteeInsurersInput.getAttribute('value');
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

  async branchCodeSelectLastOption(): Promise<void> {
    await this.branchCodeSelect.all(by.tagName('option')).last().click();
  }

  async branchCodeSelectOption(option: string): Promise<void> {
    await this.branchCodeSelect.sendKeys(option);
  }

  getBranchCodeSelect(): ElementFinder {
    return this.branchCodeSelect;
  }

  async getBranchCodeSelectedOption(): Promise<string> {
    return await this.branchCodeSelect.element(by.css('option:checked')).getText();
  }

  async collateralTypeSelectLastOption(): Promise<void> {
    await this.collateralTypeSelect.all(by.tagName('option')).last().click();
  }

  async collateralTypeSelectOption(option: string): Promise<void> {
    await this.collateralTypeSelect.sendKeys(option);
  }

  getCollateralTypeSelect(): ElementFinder {
    return this.collateralTypeSelect;
  }

  async getCollateralTypeSelectedOption(): Promise<string> {
    return await this.collateralTypeSelect.element(by.css('option:checked')).getText();
  }

  async countyCodeSelectLastOption(): Promise<void> {
    await this.countyCodeSelect.all(by.tagName('option')).last().click();
  }

  async countyCodeSelectOption(option: string): Promise<void> {
    await this.countyCodeSelect.sendKeys(option);
  }

  getCountyCodeSelect(): ElementFinder {
    return this.countyCodeSelect;
  }

  async getCountyCodeSelectedOption(): Promise<string> {
    return await this.countyCodeSelect.element(by.css('option:checked')).getText();
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

export class CollateralInformationDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-collateralInformation-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-collateralInformation'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
