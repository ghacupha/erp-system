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

export class TACompilationRequestComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-ta-compilation-request div table .btn-danger'));
  title = element.all(by.css('jhi-ta-compilation-request div h2#page-heading span')).first();
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

export class TACompilationRequestUpdatePage {
  pageTitle = element(by.id('jhi-ta-compilation-request-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  requisitionIdInput = element(by.id('field_requisitionId'));
  timeOfRequestInput = element(by.id('field_timeOfRequest'));
  compilationProcessStatusSelect = element(by.id('field_compilationProcessStatus'));
  numberOfEnumeratedItemsInput = element(by.id('field_numberOfEnumeratedItems'));
  batchJobIdentifierInput = element(by.id('field_batchJobIdentifier'));
  compilationTimeInput = element(by.id('field_compilationTime'));
  invalidatedInput = element(by.id('field_invalidated'));

  initiatedBySelect = element(by.id('field_initiatedBy'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setRequisitionIdInput(requisitionId: string): Promise<void> {
    await this.requisitionIdInput.sendKeys(requisitionId);
  }

  async getRequisitionIdInput(): Promise<string> {
    return await this.requisitionIdInput.getAttribute('value');
  }

  async setTimeOfRequestInput(timeOfRequest: string): Promise<void> {
    await this.timeOfRequestInput.sendKeys(timeOfRequest);
  }

  async getTimeOfRequestInput(): Promise<string> {
    return await this.timeOfRequestInput.getAttribute('value');
  }

  async setCompilationProcessStatusSelect(compilationProcessStatus: string): Promise<void> {
    await this.compilationProcessStatusSelect.sendKeys(compilationProcessStatus);
  }

  async getCompilationProcessStatusSelect(): Promise<string> {
    return await this.compilationProcessStatusSelect.element(by.css('option:checked')).getText();
  }

  async compilationProcessStatusSelectLastOption(): Promise<void> {
    await this.compilationProcessStatusSelect.all(by.tagName('option')).last().click();
  }

  async setNumberOfEnumeratedItemsInput(numberOfEnumeratedItems: string): Promise<void> {
    await this.numberOfEnumeratedItemsInput.sendKeys(numberOfEnumeratedItems);
  }

  async getNumberOfEnumeratedItemsInput(): Promise<string> {
    return await this.numberOfEnumeratedItemsInput.getAttribute('value');
  }

  async setBatchJobIdentifierInput(batchJobIdentifier: string): Promise<void> {
    await this.batchJobIdentifierInput.sendKeys(batchJobIdentifier);
  }

  async getBatchJobIdentifierInput(): Promise<string> {
    return await this.batchJobIdentifierInput.getAttribute('value');
  }

  async setCompilationTimeInput(compilationTime: string): Promise<void> {
    await this.compilationTimeInput.sendKeys(compilationTime);
  }

  async getCompilationTimeInput(): Promise<string> {
    return await this.compilationTimeInput.getAttribute('value');
  }

  getInvalidatedInput(): ElementFinder {
    return this.invalidatedInput;
  }

  async initiatedBySelectLastOption(): Promise<void> {
    await this.initiatedBySelect.all(by.tagName('option')).last().click();
  }

  async initiatedBySelectOption(option: string): Promise<void> {
    await this.initiatedBySelect.sendKeys(option);
  }

  getInitiatedBySelect(): ElementFinder {
    return this.initiatedBySelect;
  }

  async getInitiatedBySelectedOption(): Promise<string> {
    return await this.initiatedBySelect.element(by.css('option:checked')).getText();
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

export class TACompilationRequestDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-tACompilationRequest-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-tACompilationRequest'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
