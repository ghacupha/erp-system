///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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

export class LeaseContractComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-lease-contract div table .btn-danger'));
  title = element.all(by.css('jhi-lease-contract div h2#page-heading span')).first();
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

export class LeaseContractUpdatePage {
  pageTitle = element(by.id('jhi-lease-contract-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  bookingIdInput = element(by.id('field_bookingId'));
  leaseTitleInput = element(by.id('field_leaseTitle'));
  identifierInput = element(by.id('field_identifier'));
  descriptionInput = element(by.id('field_description'));
  commencementDateInput = element(by.id('field_commencementDate'));
  terminalDateInput = element(by.id('field_terminalDate'));

  placeholderSelect = element(by.id('field_placeholder'));
  systemMappingsSelect = element(by.id('field_systemMappings'));
  businessDocumentSelect = element(by.id('field_businessDocument'));
  contractMetadataSelect = element(by.id('field_contractMetadata'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setBookingIdInput(bookingId: string): Promise<void> {
    await this.bookingIdInput.sendKeys(bookingId);
  }

  async getBookingIdInput(): Promise<string> {
    return await this.bookingIdInput.getAttribute('value');
  }

  async setLeaseTitleInput(leaseTitle: string): Promise<void> {
    await this.leaseTitleInput.sendKeys(leaseTitle);
  }

  async getLeaseTitleInput(): Promise<string> {
    return await this.leaseTitleInput.getAttribute('value');
  }

  async setIdentifierInput(identifier: string): Promise<void> {
    await this.identifierInput.sendKeys(identifier);
  }

  async getIdentifierInput(): Promise<string> {
    return await this.identifierInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
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

  async systemMappingsSelectLastOption(): Promise<void> {
    await this.systemMappingsSelect.all(by.tagName('option')).last().click();
  }

  async systemMappingsSelectOption(option: string): Promise<void> {
    await this.systemMappingsSelect.sendKeys(option);
  }

  getSystemMappingsSelect(): ElementFinder {
    return this.systemMappingsSelect;
  }

  async getSystemMappingsSelectedOption(): Promise<string> {
    return await this.systemMappingsSelect.element(by.css('option:checked')).getText();
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

  async contractMetadataSelectLastOption(): Promise<void> {
    await this.contractMetadataSelect.all(by.tagName('option')).last().click();
  }

  async contractMetadataSelectOption(option: string): Promise<void> {
    await this.contractMetadataSelect.sendKeys(option);
  }

  getContractMetadataSelect(): ElementFinder {
    return this.contractMetadataSelect;
  }

  async getContractMetadataSelectedOption(): Promise<string> {
    return await this.contractMetadataSelect.element(by.css('option:checked')).getText();
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

export class LeaseContractDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-leaseContract-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-leaseContract'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
