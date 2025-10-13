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

export class DepreciationBatchSequenceComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-depreciation-batch-sequence div table .btn-danger'));
  title = element.all(by.css('jhi-depreciation-batch-sequence div h2#page-heading span')).first();
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

export class DepreciationBatchSequenceUpdatePage {
  pageTitle = element(by.id('jhi-depreciation-batch-sequence-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  startIndexInput = element(by.id('field_startIndex'));
  endIndexInput = element(by.id('field_endIndex'));
  createdAtInput = element(by.id('field_createdAt'));
  depreciationBatchStatusSelect = element(by.id('field_depreciationBatchStatus'));
  batchSizeInput = element(by.id('field_batchSize'));
  processedItemsInput = element(by.id('field_processedItems'));
  sequenceNumberInput = element(by.id('field_sequenceNumber'));
  isLastBatchInput = element(by.id('field_isLastBatch'));
  processingTimeInput = element(by.id('field_processingTime'));
  totalItemsInput = element(by.id('field_totalItems'));

  depreciationJobSelect = element(by.id('field_depreciationJob'));

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

  async setCreatedAtInput(createdAt: string): Promise<void> {
    await this.createdAtInput.sendKeys(createdAt);
  }

  async getCreatedAtInput(): Promise<string> {
    return await this.createdAtInput.getAttribute('value');
  }

  async setDepreciationBatchStatusSelect(depreciationBatchStatus: string): Promise<void> {
    await this.depreciationBatchStatusSelect.sendKeys(depreciationBatchStatus);
  }

  async getDepreciationBatchStatusSelect(): Promise<string> {
    return await this.depreciationBatchStatusSelect.element(by.css('option:checked')).getText();
  }

  async depreciationBatchStatusSelectLastOption(): Promise<void> {
    await this.depreciationBatchStatusSelect.all(by.tagName('option')).last().click();
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

  async depreciationJobSelectLastOption(): Promise<void> {
    await this.depreciationJobSelect.all(by.tagName('option')).last().click();
  }

  async depreciationJobSelectOption(option: string): Promise<void> {
    await this.depreciationJobSelect.sendKeys(option);
  }

  getDepreciationJobSelect(): ElementFinder {
    return this.depreciationJobSelect;
  }

  async getDepreciationJobSelectedOption(): Promise<string> {
    return await this.depreciationJobSelect.element(by.css('option:checked')).getText();
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

export class DepreciationBatchSequenceDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-depreciationBatchSequence-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-depreciationBatchSequence'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
