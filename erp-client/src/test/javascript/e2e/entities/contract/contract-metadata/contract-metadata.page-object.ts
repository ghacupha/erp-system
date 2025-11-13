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

export class ContractMetadataComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-contract-metadata div table .btn-danger'));
  title = element.all(by.css('jhi-contract-metadata div h2#page-heading span')).first();
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

export class ContractMetadataUpdatePage {
  pageTitle = element(by.id('jhi-contract-metadata-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  descriptionInput = element(by.id('field_description'));
  typeOfContractSelect = element(by.id('field_typeOfContract'));
  contractStatusSelect = element(by.id('field_contractStatus'));
  startDateInput = element(by.id('field_startDate'));
  terminationDateInput = element(by.id('field_terminationDate'));
  commentsAndAttachmentInput = element(by.id('field_commentsAndAttachment'));
  contractTitleInput = element(by.id('field_contractTitle'));
  contractIdentifierInput = element(by.id('field_contractIdentifier'));
  contractIdentifierShortInput = element(by.id('field_contractIdentifierShort'));

  relatedContractsSelect = element(by.id('field_relatedContracts'));
  departmentSelect = element(by.id('field_department'));
  contractPartnerSelect = element(by.id('field_contractPartner'));
  responsiblePersonSelect = element(by.id('field_responsiblePerson'));
  signatorySelect = element(by.id('field_signatory'));
  securityClearanceSelect = element(by.id('field_securityClearance'));
  placeholderSelect = element(by.id('field_placeholder'));
  contractDocumentFileSelect = element(by.id('field_contractDocumentFile'));
  contractMappingsSelect = element(by.id('field_contractMappings'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setTypeOfContractSelect(typeOfContract: string): Promise<void> {
    await this.typeOfContractSelect.sendKeys(typeOfContract);
  }

  async getTypeOfContractSelect(): Promise<string> {
    return await this.typeOfContractSelect.element(by.css('option:checked')).getText();
  }

  async typeOfContractSelectLastOption(): Promise<void> {
    await this.typeOfContractSelect.all(by.tagName('option')).last().click();
  }

  async setContractStatusSelect(contractStatus: string): Promise<void> {
    await this.contractStatusSelect.sendKeys(contractStatus);
  }

  async getContractStatusSelect(): Promise<string> {
    return await this.contractStatusSelect.element(by.css('option:checked')).getText();
  }

  async contractStatusSelectLastOption(): Promise<void> {
    await this.contractStatusSelect.all(by.tagName('option')).last().click();
  }

  async setStartDateInput(startDate: string): Promise<void> {
    await this.startDateInput.sendKeys(startDate);
  }

  async getStartDateInput(): Promise<string> {
    return await this.startDateInput.getAttribute('value');
  }

  async setTerminationDateInput(terminationDate: string): Promise<void> {
    await this.terminationDateInput.sendKeys(terminationDate);
  }

  async getTerminationDateInput(): Promise<string> {
    return await this.terminationDateInput.getAttribute('value');
  }

  async setCommentsAndAttachmentInput(commentsAndAttachment: string): Promise<void> {
    await this.commentsAndAttachmentInput.sendKeys(commentsAndAttachment);
  }

  async getCommentsAndAttachmentInput(): Promise<string> {
    return await this.commentsAndAttachmentInput.getAttribute('value');
  }

  async setContractTitleInput(contractTitle: string): Promise<void> {
    await this.contractTitleInput.sendKeys(contractTitle);
  }

  async getContractTitleInput(): Promise<string> {
    return await this.contractTitleInput.getAttribute('value');
  }

  async setContractIdentifierInput(contractIdentifier: string): Promise<void> {
    await this.contractIdentifierInput.sendKeys(contractIdentifier);
  }

  async getContractIdentifierInput(): Promise<string> {
    return await this.contractIdentifierInput.getAttribute('value');
  }

  async setContractIdentifierShortInput(contractIdentifierShort: string): Promise<void> {
    await this.contractIdentifierShortInput.sendKeys(contractIdentifierShort);
  }

  async getContractIdentifierShortInput(): Promise<string> {
    return await this.contractIdentifierShortInput.getAttribute('value');
  }

  async relatedContractsSelectLastOption(): Promise<void> {
    await this.relatedContractsSelect.all(by.tagName('option')).last().click();
  }

  async relatedContractsSelectOption(option: string): Promise<void> {
    await this.relatedContractsSelect.sendKeys(option);
  }

  getRelatedContractsSelect(): ElementFinder {
    return this.relatedContractsSelect;
  }

  async getRelatedContractsSelectedOption(): Promise<string> {
    return await this.relatedContractsSelect.element(by.css('option:checked')).getText();
  }

  async departmentSelectLastOption(): Promise<void> {
    await this.departmentSelect.all(by.tagName('option')).last().click();
  }

  async departmentSelectOption(option: string): Promise<void> {
    await this.departmentSelect.sendKeys(option);
  }

  getDepartmentSelect(): ElementFinder {
    return this.departmentSelect;
  }

  async getDepartmentSelectedOption(): Promise<string> {
    return await this.departmentSelect.element(by.css('option:checked')).getText();
  }

  async contractPartnerSelectLastOption(): Promise<void> {
    await this.contractPartnerSelect.all(by.tagName('option')).last().click();
  }

  async contractPartnerSelectOption(option: string): Promise<void> {
    await this.contractPartnerSelect.sendKeys(option);
  }

  getContractPartnerSelect(): ElementFinder {
    return this.contractPartnerSelect;
  }

  async getContractPartnerSelectedOption(): Promise<string> {
    return await this.contractPartnerSelect.element(by.css('option:checked')).getText();
  }

  async responsiblePersonSelectLastOption(): Promise<void> {
    await this.responsiblePersonSelect.all(by.tagName('option')).last().click();
  }

  async responsiblePersonSelectOption(option: string): Promise<void> {
    await this.responsiblePersonSelect.sendKeys(option);
  }

  getResponsiblePersonSelect(): ElementFinder {
    return this.responsiblePersonSelect;
  }

  async getResponsiblePersonSelectedOption(): Promise<string> {
    return await this.responsiblePersonSelect.element(by.css('option:checked')).getText();
  }

  async signatorySelectLastOption(): Promise<void> {
    await this.signatorySelect.all(by.tagName('option')).last().click();
  }

  async signatorySelectOption(option: string): Promise<void> {
    await this.signatorySelect.sendKeys(option);
  }

  getSignatorySelect(): ElementFinder {
    return this.signatorySelect;
  }

  async getSignatorySelectedOption(): Promise<string> {
    return await this.signatorySelect.element(by.css('option:checked')).getText();
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

  async contractDocumentFileSelectLastOption(): Promise<void> {
    await this.contractDocumentFileSelect.all(by.tagName('option')).last().click();
  }

  async contractDocumentFileSelectOption(option: string): Promise<void> {
    await this.contractDocumentFileSelect.sendKeys(option);
  }

  getContractDocumentFileSelect(): ElementFinder {
    return this.contractDocumentFileSelect;
  }

  async getContractDocumentFileSelectedOption(): Promise<string> {
    return await this.contractDocumentFileSelect.element(by.css('option:checked')).getText();
  }

  async contractMappingsSelectLastOption(): Promise<void> {
    await this.contractMappingsSelect.all(by.tagName('option')).last().click();
  }

  async contractMappingsSelectOption(option: string): Promise<void> {
    await this.contractMappingsSelect.sendKeys(option);
  }

  getContractMappingsSelect(): ElementFinder {
    return this.contractMappingsSelect;
  }

  async getContractMappingsSelectedOption(): Promise<string> {
    return await this.contractMappingsSelect.element(by.css('option:checked')).getText();
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

export class ContractMetadataDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-contractMetadata-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-contractMetadata'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
