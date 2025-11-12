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

export class DealerComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-dealer div table .btn-danger'));
  title = element.all(by.css('jhi-dealer div h2#page-heading span')).first();
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

export class DealerUpdatePage {
  pageTitle = element(by.id('jhi-dealer-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  dealerNameInput = element(by.id('field_dealerName'));
  taxNumberInput = element(by.id('field_taxNumber'));
  identificationDocumentNumberInput = element(by.id('field_identificationDocumentNumber'));
  organizationNameInput = element(by.id('field_organizationName'));
  departmentInput = element(by.id('field_department'));
  positionInput = element(by.id('field_position'));
  postalAddressInput = element(by.id('field_postalAddress'));
  physicalAddressInput = element(by.id('field_physicalAddress'));
  accountNameInput = element(by.id('field_accountName'));
  accountNumberInput = element(by.id('field_accountNumber'));
  bankersNameInput = element(by.id('field_bankersName'));
  bankersBranchInput = element(by.id('field_bankersBranch'));
  bankersSwiftCodeInput = element(by.id('field_bankersSwiftCode'));
  fileUploadTokenInput = element(by.id('field_fileUploadToken'));
  compilationTokenInput = element(by.id('field_compilationToken'));
  remarksInput = element(by.id('field_remarks'));
  otherNamesInput = element(by.id('field_otherNames'));

  paymentLabelSelect = element(by.id('field_paymentLabel'));
  dealerGroupSelect = element(by.id('field_dealerGroup'));
  placeholderSelect = element(by.id('field_placeholder'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setDealerNameInput(dealerName: string): Promise<void> {
    await this.dealerNameInput.sendKeys(dealerName);
  }

  async getDealerNameInput(): Promise<string> {
    return await this.dealerNameInput.getAttribute('value');
  }

  async setTaxNumberInput(taxNumber: string): Promise<void> {
    await this.taxNumberInput.sendKeys(taxNumber);
  }

  async getTaxNumberInput(): Promise<string> {
    return await this.taxNumberInput.getAttribute('value');
  }

  async setIdentificationDocumentNumberInput(identificationDocumentNumber: string): Promise<void> {
    await this.identificationDocumentNumberInput.sendKeys(identificationDocumentNumber);
  }

  async getIdentificationDocumentNumberInput(): Promise<string> {
    return await this.identificationDocumentNumberInput.getAttribute('value');
  }

  async setOrganizationNameInput(organizationName: string): Promise<void> {
    await this.organizationNameInput.sendKeys(organizationName);
  }

  async getOrganizationNameInput(): Promise<string> {
    return await this.organizationNameInput.getAttribute('value');
  }

  async setDepartmentInput(department: string): Promise<void> {
    await this.departmentInput.sendKeys(department);
  }

  async getDepartmentInput(): Promise<string> {
    return await this.departmentInput.getAttribute('value');
  }

  async setPositionInput(position: string): Promise<void> {
    await this.positionInput.sendKeys(position);
  }

  async getPositionInput(): Promise<string> {
    return await this.positionInput.getAttribute('value');
  }

  async setPostalAddressInput(postalAddress: string): Promise<void> {
    await this.postalAddressInput.sendKeys(postalAddress);
  }

  async getPostalAddressInput(): Promise<string> {
    return await this.postalAddressInput.getAttribute('value');
  }

  async setPhysicalAddressInput(physicalAddress: string): Promise<void> {
    await this.physicalAddressInput.sendKeys(physicalAddress);
  }

  async getPhysicalAddressInput(): Promise<string> {
    return await this.physicalAddressInput.getAttribute('value');
  }

  async setAccountNameInput(accountName: string): Promise<void> {
    await this.accountNameInput.sendKeys(accountName);
  }

  async getAccountNameInput(): Promise<string> {
    return await this.accountNameInput.getAttribute('value');
  }

  async setAccountNumberInput(accountNumber: string): Promise<void> {
    await this.accountNumberInput.sendKeys(accountNumber);
  }

  async getAccountNumberInput(): Promise<string> {
    return await this.accountNumberInput.getAttribute('value');
  }

  async setBankersNameInput(bankersName: string): Promise<void> {
    await this.bankersNameInput.sendKeys(bankersName);
  }

  async getBankersNameInput(): Promise<string> {
    return await this.bankersNameInput.getAttribute('value');
  }

  async setBankersBranchInput(bankersBranch: string): Promise<void> {
    await this.bankersBranchInput.sendKeys(bankersBranch);
  }

  async getBankersBranchInput(): Promise<string> {
    return await this.bankersBranchInput.getAttribute('value');
  }

  async setBankersSwiftCodeInput(bankersSwiftCode: string): Promise<void> {
    await this.bankersSwiftCodeInput.sendKeys(bankersSwiftCode);
  }

  async getBankersSwiftCodeInput(): Promise<string> {
    return await this.bankersSwiftCodeInput.getAttribute('value');
  }

  async setFileUploadTokenInput(fileUploadToken: string): Promise<void> {
    await this.fileUploadTokenInput.sendKeys(fileUploadToken);
  }

  async getFileUploadTokenInput(): Promise<string> {
    return await this.fileUploadTokenInput.getAttribute('value');
  }

  async setCompilationTokenInput(compilationToken: string): Promise<void> {
    await this.compilationTokenInput.sendKeys(compilationToken);
  }

  async getCompilationTokenInput(): Promise<string> {
    return await this.compilationTokenInput.getAttribute('value');
  }

  async setRemarksInput(remarks: string): Promise<void> {
    await this.remarksInput.sendKeys(remarks);
  }

  async getRemarksInput(): Promise<string> {
    return await this.remarksInput.getAttribute('value');
  }

  async setOtherNamesInput(otherNames: string): Promise<void> {
    await this.otherNamesInput.sendKeys(otherNames);
  }

  async getOtherNamesInput(): Promise<string> {
    return await this.otherNamesInput.getAttribute('value');
  }

  async paymentLabelSelectLastOption(): Promise<void> {
    await this.paymentLabelSelect.all(by.tagName('option')).last().click();
  }

  async paymentLabelSelectOption(option: string): Promise<void> {
    await this.paymentLabelSelect.sendKeys(option);
  }

  getPaymentLabelSelect(): ElementFinder {
    return this.paymentLabelSelect;
  }

  async getPaymentLabelSelectedOption(): Promise<string> {
    return await this.paymentLabelSelect.element(by.css('option:checked')).getText();
  }

  async dealerGroupSelectLastOption(): Promise<void> {
    await this.dealerGroupSelect.all(by.tagName('option')).last().click();
  }

  async dealerGroupSelectOption(option: string): Promise<void> {
    await this.dealerGroupSelect.sendKeys(option);
  }

  getDealerGroupSelect(): ElementFinder {
    return this.dealerGroupSelect;
  }

  async getDealerGroupSelectedOption(): Promise<string> {
    return await this.dealerGroupSelect.element(by.css('option:checked')).getText();
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

export class DealerDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-dealer-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-dealer'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
