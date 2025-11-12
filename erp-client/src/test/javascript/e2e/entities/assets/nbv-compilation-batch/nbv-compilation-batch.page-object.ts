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

export class NbvCompilationBatchComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-nbv-compilation-batch div table .btn-danger'));
  title = element.all(by.css('jhi-nbv-compilation-batch div h2#page-heading span')).first();
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

export class NbvCompilationBatchUpdatePage {
  pageTitle = element(by.id('jhi-nbv-compilation-batch-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  startIndexInput = element(by.id('field_startIndex'));
  endIndexInput = element(by.id('field_endIndex'));
  compilationBatchStatusSelect = element(by.id('field_compilationBatchStatus'));
  compilationBatchIdentifierInput = element(by.id('field_compilationBatchIdentifier'));
  compilationJobidentifierInput = element(by.id('field_compilationJobidentifier'));
  depreciationPeriodIdentifierInput = element(by.id('field_depreciationPeriodIdentifier'));
  fiscalMonthIdentifierInput = element(by.id('field_fiscalMonthIdentifier'));
  batchSizeInput = element(by.id('field_batchSize'));
  processedItemsInput = element(by.id('field_processedItems'));
  sequenceNumberInput = element(by.id('field_sequenceNumber'));
  isLastBatchInput = element(by.id('field_isLastBatch'));
  processingTimeInput = element(by.id('field_processingTime'));
  totalItemsInput = element(by.id('field_totalItems'));

  nbvCompilationJobSelect = element(by.id('field_nbvCompilationJob'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setStartIndexInput(startIndex: string): Promise<void> {
    await this.startIndexInput.sendKeys(startIndex);
  }

  async getStartIndexInput(): Promise<string> {
    return await this.startIndexInput.getAttribute('value');
  }

  async setEndIndexInput(endIndex: string): Promise<void> {
    await this.endIndexInput.sendKeys(endIndex);
  }

  async getEndIndexInput(): Promise<string> {
    return await this.endIndexInput.getAttribute('value');
  }

  async setCompilationBatchStatusSelect(compilationBatchStatus: string): Promise<void> {
    await this.compilationBatchStatusSelect.sendKeys(compilationBatchStatus);
  }

  async getCompilationBatchStatusSelect(): Promise<string> {
    return await this.compilationBatchStatusSelect.element(by.css('option:checked')).getText();
  }

  async compilationBatchStatusSelectLastOption(): Promise<void> {
    await this.compilationBatchStatusSelect.all(by.tagName('option')).last().click();
  }

  async setCompilationBatchIdentifierInput(compilationBatchIdentifier: string): Promise<void> {
    await this.compilationBatchIdentifierInput.sendKeys(compilationBatchIdentifier);
  }

  async getCompilationBatchIdentifierInput(): Promise<string> {
    return await this.compilationBatchIdentifierInput.getAttribute('value');
  }

  async setCompilationJobidentifierInput(compilationJobidentifier: string): Promise<void> {
    await this.compilationJobidentifierInput.sendKeys(compilationJobidentifier);
  }

  async getCompilationJobidentifierInput(): Promise<string> {
    return await this.compilationJobidentifierInput.getAttribute('value');
  }

  async setDepreciationPeriodIdentifierInput(depreciationPeriodIdentifier: string): Promise<void> {
    await this.depreciationPeriodIdentifierInput.sendKeys(depreciationPeriodIdentifier);
  }

  async getDepreciationPeriodIdentifierInput(): Promise<string> {
    return await this.depreciationPeriodIdentifierInput.getAttribute('value');
  }

  async setFiscalMonthIdentifierInput(fiscalMonthIdentifier: string): Promise<void> {
    await this.fiscalMonthIdentifierInput.sendKeys(fiscalMonthIdentifier);
  }

  async getFiscalMonthIdentifierInput(): Promise<string> {
    return await this.fiscalMonthIdentifierInput.getAttribute('value');
  }

  async setBatchSizeInput(batchSize: string): Promise<void> {
    await this.batchSizeInput.sendKeys(batchSize);
  }

  async getBatchSizeInput(): Promise<string> {
    return await this.batchSizeInput.getAttribute('value');
  }

  async setProcessedItemsInput(processedItems: string): Promise<void> {
    await this.processedItemsInput.sendKeys(processedItems);
  }

  async getProcessedItemsInput(): Promise<string> {
    return await this.processedItemsInput.getAttribute('value');
  }

  async setSequenceNumberInput(sequenceNumber: string): Promise<void> {
    await this.sequenceNumberInput.sendKeys(sequenceNumber);
  }

  async getSequenceNumberInput(): Promise<string> {
    return await this.sequenceNumberInput.getAttribute('value');
  }

  getIsLastBatchInput(): ElementFinder {
    return this.isLastBatchInput;
  }

  async setProcessingTimeInput(processingTime: string): Promise<void> {
    await this.processingTimeInput.sendKeys(processingTime);
  }

  async getProcessingTimeInput(): Promise<string> {
    return await this.processingTimeInput.getAttribute('value');
  }

  async setTotalItemsInput(totalItems: string): Promise<void> {
    await this.totalItemsInput.sendKeys(totalItems);
  }

  async getTotalItemsInput(): Promise<string> {
    return await this.totalItemsInput.getAttribute('value');
  }

  async nbvCompilationJobSelectLastOption(): Promise<void> {
    await this.nbvCompilationJobSelect.all(by.tagName('option')).last().click();
  }

  async nbvCompilationJobSelectOption(option: string): Promise<void> {
    await this.nbvCompilationJobSelect.sendKeys(option);
  }

  getNbvCompilationJobSelect(): ElementFinder {
    return this.nbvCompilationJobSelect;
  }

  async getNbvCompilationJobSelectedOption(): Promise<string> {
    return await this.nbvCompilationJobSelect.element(by.css('option:checked')).getText();
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

export class NbvCompilationBatchDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-nbvCompilationBatch-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-nbvCompilationBatch'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
