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

export class GdiTransactionDataIndexComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-gdi-transaction-data-index div table .btn-danger'));
  title = element.all(by.css('jhi-gdi-transaction-data-index div h2#page-heading span')).first();
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

export class GdiTransactionDataIndexUpdatePage {
  pageTitle = element(by.id('jhi-gdi-transaction-data-index-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  datasetNameInput = element(by.id('field_datasetName'));
  databaseNameInput = element(by.id('field_databaseName'));
  updateFrequencySelect = element(by.id('field_updateFrequency'));
  datasetBehaviorSelect = element(by.id('field_datasetBehavior'));
  minimumDatarowsPerRequestInput = element(by.id('field_minimumDatarowsPerRequest'));
  maximumDataRowsPerRequestInput = element(by.id('field_maximumDataRowsPerRequest'));
  datasetDescriptionInput = element(by.id('field_datasetDescription'));
  dataTemplateInput = element(by.id('file_dataTemplate'));

  masterDataItemSelect = element(by.id('field_masterDataItem'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setDatasetNameInput(datasetName: string): Promise<void> {
    await this.datasetNameInput.sendKeys(datasetName);
  }

  async getDatasetNameInput(): Promise<string> {
    return await this.datasetNameInput.getAttribute('value');
  }

  async setDatabaseNameInput(databaseName: string): Promise<void> {
    await this.databaseNameInput.sendKeys(databaseName);
  }

  async getDatabaseNameInput(): Promise<string> {
    return await this.databaseNameInput.getAttribute('value');
  }

  async setUpdateFrequencySelect(updateFrequency: string): Promise<void> {
    await this.updateFrequencySelect.sendKeys(updateFrequency);
  }

  async getUpdateFrequencySelect(): Promise<string> {
    return await this.updateFrequencySelect.element(by.css('option:checked')).getText();
  }

  async updateFrequencySelectLastOption(): Promise<void> {
    await this.updateFrequencySelect.all(by.tagName('option')).last().click();
  }

  async setDatasetBehaviorSelect(datasetBehavior: string): Promise<void> {
    await this.datasetBehaviorSelect.sendKeys(datasetBehavior);
  }

  async getDatasetBehaviorSelect(): Promise<string> {
    return await this.datasetBehaviorSelect.element(by.css('option:checked')).getText();
  }

  async datasetBehaviorSelectLastOption(): Promise<void> {
    await this.datasetBehaviorSelect.all(by.tagName('option')).last().click();
  }

  async setMinimumDatarowsPerRequestInput(minimumDatarowsPerRequest: string): Promise<void> {
    await this.minimumDatarowsPerRequestInput.sendKeys(minimumDatarowsPerRequest);
  }

  async getMinimumDatarowsPerRequestInput(): Promise<string> {
    return await this.minimumDatarowsPerRequestInput.getAttribute('value');
  }

  async setMaximumDataRowsPerRequestInput(maximumDataRowsPerRequest: string): Promise<void> {
    await this.maximumDataRowsPerRequestInput.sendKeys(maximumDataRowsPerRequest);
  }

  async getMaximumDataRowsPerRequestInput(): Promise<string> {
    return await this.maximumDataRowsPerRequestInput.getAttribute('value');
  }

  async setDatasetDescriptionInput(datasetDescription: string): Promise<void> {
    await this.datasetDescriptionInput.sendKeys(datasetDescription);
  }

  async getDatasetDescriptionInput(): Promise<string> {
    return await this.datasetDescriptionInput.getAttribute('value');
  }

  async setDataTemplateInput(dataTemplate: string): Promise<void> {
    await this.dataTemplateInput.sendKeys(dataTemplate);
  }

  async getDataTemplateInput(): Promise<string> {
    return await this.dataTemplateInput.getAttribute('value');
  }

  async masterDataItemSelectLastOption(): Promise<void> {
    await this.masterDataItemSelect.all(by.tagName('option')).last().click();
  }

  async masterDataItemSelectOption(option: string): Promise<void> {
    await this.masterDataItemSelect.sendKeys(option);
  }

  getMasterDataItemSelect(): ElementFinder {
    return this.masterDataItemSelect;
  }

  async getMasterDataItemSelectedOption(): Promise<string> {
    return await this.masterDataItemSelect.element(by.css('option:checked')).getText();
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

export class GdiTransactionDataIndexDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-gdiTransactionDataIndex-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-gdiTransactionDataIndex'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
