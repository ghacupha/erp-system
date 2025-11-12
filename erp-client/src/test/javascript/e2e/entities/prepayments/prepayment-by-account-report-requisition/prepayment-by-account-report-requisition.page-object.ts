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

export class PrepaymentByAccountReportRequisitionComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-prepayment-by-account-report-requisition div table .btn-danger'));
  title = element.all(by.css('jhi-prepayment-by-account-report-requisition div h2#page-heading span')).first();
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

export class PrepaymentByAccountReportRequisitionUpdatePage {
  pageTitle = element(by.id('jhi-prepayment-by-account-report-requisition-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  requestIdInput = element(by.id('field_requestId'));
  timeOfRequisitionInput = element(by.id('field_timeOfRequisition'));
  fileChecksumInput = element(by.id('field_fileChecksum'));
  filenameInput = element(by.id('field_filename'));
  reportParametersInput = element(by.id('field_reportParameters'));
  reportFileInput = element(by.id('file_reportFile'));
  reportDateInput = element(by.id('field_reportDate'));
  tamperedInput = element(by.id('field_tampered'));

  requestedBySelect = element(by.id('field_requestedBy'));
  lastAccessedBySelect = element(by.id('field_lastAccessedBy'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setRequestIdInput(requestId: string): Promise<void> {
    await this.requestIdInput.sendKeys(requestId);
  }

  async getRequestIdInput(): Promise<string> {
    return await this.requestIdInput.getAttribute('value');
  }

  async setTimeOfRequisitionInput(timeOfRequisition: string): Promise<void> {
    await this.timeOfRequisitionInput.sendKeys(timeOfRequisition);
  }

  async getTimeOfRequisitionInput(): Promise<string> {
    return await this.timeOfRequisitionInput.getAttribute('value');
  }

  async setFileChecksumInput(fileChecksum: string): Promise<void> {
    await this.fileChecksumInput.sendKeys(fileChecksum);
  }

  async getFileChecksumInput(): Promise<string> {
    return await this.fileChecksumInput.getAttribute('value');
  }

  async setFilenameInput(filename: string): Promise<void> {
    await this.filenameInput.sendKeys(filename);
  }

  async getFilenameInput(): Promise<string> {
    return await this.filenameInput.getAttribute('value');
  }

  async setReportParametersInput(reportParameters: string): Promise<void> {
    await this.reportParametersInput.sendKeys(reportParameters);
  }

  async getReportParametersInput(): Promise<string> {
    return await this.reportParametersInput.getAttribute('value');
  }

  async setReportFileInput(reportFile: string): Promise<void> {
    await this.reportFileInput.sendKeys(reportFile);
  }

  async getReportFileInput(): Promise<string> {
    return await this.reportFileInput.getAttribute('value');
  }

  async setReportDateInput(reportDate: string): Promise<void> {
    await this.reportDateInput.sendKeys(reportDate);
  }

  async getReportDateInput(): Promise<string> {
    return await this.reportDateInput.getAttribute('value');
  }

  getTamperedInput(): ElementFinder {
    return this.tamperedInput;
  }

  async requestedBySelectLastOption(): Promise<void> {
    await this.requestedBySelect.all(by.tagName('option')).last().click();
  }

  async requestedBySelectOption(option: string): Promise<void> {
    await this.requestedBySelect.sendKeys(option);
  }

  getRequestedBySelect(): ElementFinder {
    return this.requestedBySelect;
  }

  async getRequestedBySelectedOption(): Promise<string> {
    return await this.requestedBySelect.element(by.css('option:checked')).getText();
  }

  async lastAccessedBySelectLastOption(): Promise<void> {
    await this.lastAccessedBySelect.all(by.tagName('option')).last().click();
  }

  async lastAccessedBySelectOption(option: string): Promise<void> {
    await this.lastAccessedBySelect.sendKeys(option);
  }

  getLastAccessedBySelect(): ElementFinder {
    return this.lastAccessedBySelect;
  }

  async getLastAccessedBySelectedOption(): Promise<string> {
    return await this.lastAccessedBySelect.element(by.css('option:checked')).getText();
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

export class PrepaymentByAccountReportRequisitionDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-prepaymentByAccountReportRequisition-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-prepaymentByAccountReportRequisition'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
