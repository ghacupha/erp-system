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

export class AccountAttributeMetadataComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-account-attribute-metadata div table .btn-danger'));
  title = element.all(by.css('jhi-account-attribute-metadata div h2#page-heading span')).first();
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

export class AccountAttributeMetadataUpdatePage {
  pageTitle = element(by.id('jhi-account-attribute-metadata-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  precedenceInput = element(by.id('field_precedence'));
  columnNameInput = element(by.id('field_columnName'));
  shortNameInput = element(by.id('field_shortName'));
  detailedDefinitionInput = element(by.id('field_detailedDefinition'));
  dataTypeInput = element(by.id('field_dataType'));
  lengthInput = element(by.id('field_length'));
  columnIndexInput = element(by.id('field_columnIndex'));
  mandatoryFieldFlagSelect = element(by.id('field_mandatoryFieldFlag'));
  businessValidationInput = element(by.id('field_businessValidation'));
  technicalValidationInput = element(by.id('field_technicalValidation'));
  dbColumnNameInput = element(by.id('field_dbColumnName'));
  metadataVersionInput = element(by.id('field_metadataVersion'));

  standardInputTemplateSelect = element(by.id('field_standardInputTemplate'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setPrecedenceInput(precedence: string): Promise<void> {
    await this.precedenceInput.sendKeys(precedence);
  }

  async getPrecedenceInput(): Promise<string> {
    return await this.precedenceInput.getAttribute('value');
  }

  async setColumnNameInput(columnName: string): Promise<void> {
    await this.columnNameInput.sendKeys(columnName);
  }

  async getColumnNameInput(): Promise<string> {
    return await this.columnNameInput.getAttribute('value');
  }

  async setShortNameInput(shortName: string): Promise<void> {
    await this.shortNameInput.sendKeys(shortName);
  }

  async getShortNameInput(): Promise<string> {
    return await this.shortNameInput.getAttribute('value');
  }

  async setDetailedDefinitionInput(detailedDefinition: string): Promise<void> {
    await this.detailedDefinitionInput.sendKeys(detailedDefinition);
  }

  async getDetailedDefinitionInput(): Promise<string> {
    return await this.detailedDefinitionInput.getAttribute('value');
  }

  async setDataTypeInput(dataType: string): Promise<void> {
    await this.dataTypeInput.sendKeys(dataType);
  }

  async getDataTypeInput(): Promise<string> {
    return await this.dataTypeInput.getAttribute('value');
  }

  async setLengthInput(length: string): Promise<void> {
    await this.lengthInput.sendKeys(length);
  }

  async getLengthInput(): Promise<string> {
    return await this.lengthInput.getAttribute('value');
  }

  async setColumnIndexInput(columnIndex: string): Promise<void> {
    await this.columnIndexInput.sendKeys(columnIndex);
  }

  async getColumnIndexInput(): Promise<string> {
    return await this.columnIndexInput.getAttribute('value');
  }

  async setMandatoryFieldFlagSelect(mandatoryFieldFlag: string): Promise<void> {
    await this.mandatoryFieldFlagSelect.sendKeys(mandatoryFieldFlag);
  }

  async getMandatoryFieldFlagSelect(): Promise<string> {
    return await this.mandatoryFieldFlagSelect.element(by.css('option:checked')).getText();
  }

  async mandatoryFieldFlagSelectLastOption(): Promise<void> {
    await this.mandatoryFieldFlagSelect.all(by.tagName('option')).last().click();
  }

  async setBusinessValidationInput(businessValidation: string): Promise<void> {
    await this.businessValidationInput.sendKeys(businessValidation);
  }

  async getBusinessValidationInput(): Promise<string> {
    return await this.businessValidationInput.getAttribute('value');
  }

  async setTechnicalValidationInput(technicalValidation: string): Promise<void> {
    await this.technicalValidationInput.sendKeys(technicalValidation);
  }

  async getTechnicalValidationInput(): Promise<string> {
    return await this.technicalValidationInput.getAttribute('value');
  }

  async setDbColumnNameInput(dbColumnName: string): Promise<void> {
    await this.dbColumnNameInput.sendKeys(dbColumnName);
  }

  async getDbColumnNameInput(): Promise<string> {
    return await this.dbColumnNameInput.getAttribute('value');
  }

  async setMetadataVersionInput(metadataVersion: string): Promise<void> {
    await this.metadataVersionInput.sendKeys(metadataVersion);
  }

  async getMetadataVersionInput(): Promise<string> {
    return await this.metadataVersionInput.getAttribute('value');
  }

  async standardInputTemplateSelectLastOption(): Promise<void> {
    await this.standardInputTemplateSelect.all(by.tagName('option')).last().click();
  }

  async standardInputTemplateSelectOption(option: string): Promise<void> {
    await this.standardInputTemplateSelect.sendKeys(option);
  }

  getStandardInputTemplateSelect(): ElementFinder {
    return this.standardInputTemplateSelect;
  }

  async getStandardInputTemplateSelectedOption(): Promise<string> {
    return await this.standardInputTemplateSelect.element(by.css('option:checked')).getText();
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

export class AccountAttributeMetadataDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-accountAttributeMetadata-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-accountAttributeMetadata'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
