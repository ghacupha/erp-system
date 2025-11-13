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

export class DepreciationJobComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-depreciation-job div table .btn-danger'));
  title = element.all(by.css('jhi-depreciation-job div h2#page-heading span')).first();
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

export class DepreciationJobUpdatePage {
  pageTitle = element(by.id('jhi-depreciation-job-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  timeOfCommencementInput = element(by.id('field_timeOfCommencement'));
  depreciationJobStatusSelect = element(by.id('field_depreciationJobStatus'));
  descriptionInput = element(by.id('field_description'));
  numberOfBatchesInput = element(by.id('field_numberOfBatches'));
  processedBatchesInput = element(by.id('field_processedBatches'));
  lastBatchSizeInput = element(by.id('field_lastBatchSize'));
  processedItemsInput = element(by.id('field_processedItems'));
  processingTimeInput = element(by.id('field_processingTime'));
  totalItemsInput = element(by.id('field_totalItems'));

  createdBySelect = element(by.id('field_createdBy'));
  depreciationPeriodSelect = element(by.id('field_depreciationPeriod'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setTimeOfCommencementInput(timeOfCommencement: string): Promise<void> {
    await this.timeOfCommencementInput.sendKeys(timeOfCommencement);
  }

  async getTimeOfCommencementInput(): Promise<string> {
    return await this.timeOfCommencementInput.getAttribute('value');
  }

  async setDepreciationJobStatusSelect(depreciationJobStatus: string): Promise<void> {
    await this.depreciationJobStatusSelect.sendKeys(depreciationJobStatus);
  }

  async getDepreciationJobStatusSelect(): Promise<string> {
    return await this.depreciationJobStatusSelect.element(by.css('option:checked')).getText();
  }

  async depreciationJobStatusSelectLastOption(): Promise<void> {
    await this.depreciationJobStatusSelect.all(by.tagName('option')).last().click();
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setNumberOfBatchesInput(numberOfBatches: string): Promise<void> {
    await this.numberOfBatchesInput.sendKeys(numberOfBatches);
  }

  async getNumberOfBatchesInput(): Promise<string> {
    return await this.numberOfBatchesInput.getAttribute('value');
  }

  async setProcessedBatchesInput(processedBatches: string): Promise<void> {
    await this.processedBatchesInput.sendKeys(processedBatches);
  }

  async getProcessedBatchesInput(): Promise<string> {
    return await this.processedBatchesInput.getAttribute('value');
  }

  async setLastBatchSizeInput(lastBatchSize: string): Promise<void> {
    await this.lastBatchSizeInput.sendKeys(lastBatchSize);
  }

  async getLastBatchSizeInput(): Promise<string> {
    return await this.lastBatchSizeInput.getAttribute('value');
  }

  async setProcessedItemsInput(processedItems: string): Promise<void> {
    await this.processedItemsInput.sendKeys(processedItems);
  }

  async getProcessedItemsInput(): Promise<string> {
    return await this.processedItemsInput.getAttribute('value');
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

  async createdBySelectLastOption(): Promise<void> {
    await this.createdBySelect.all(by.tagName('option')).last().click();
  }

  async createdBySelectOption(option: string): Promise<void> {
    await this.createdBySelect.sendKeys(option);
  }

  getCreatedBySelect(): ElementFinder {
    return this.createdBySelect;
  }

  async getCreatedBySelectedOption(): Promise<string> {
    return await this.createdBySelect.element(by.css('option:checked')).getText();
  }

  async depreciationPeriodSelectLastOption(): Promise<void> {
    await this.depreciationPeriodSelect.all(by.tagName('option')).last().click();
  }

  async depreciationPeriodSelectOption(option: string): Promise<void> {
    await this.depreciationPeriodSelect.sendKeys(option);
  }

  getDepreciationPeriodSelect(): ElementFinder {
    return this.depreciationPeriodSelect;
  }

  async getDepreciationPeriodSelectedOption(): Promise<string> {
    return await this.depreciationPeriodSelect.element(by.css('option:checked')).getText();
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

export class DepreciationJobDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-depreciationJob-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-depreciationJob'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
