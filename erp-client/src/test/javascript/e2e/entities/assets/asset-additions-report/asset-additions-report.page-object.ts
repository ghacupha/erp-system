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

export class AssetAdditionsReportComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-asset-additions-report div table .btn-danger'));
  title = element.all(by.css('jhi-asset-additions-report div h2#page-heading span')).first();
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

export class AssetAdditionsReportUpdatePage {
  pageTitle = element(by.id('jhi-asset-additions-report-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  timeOfRequestInput = element(by.id('field_timeOfRequest'));
  reportStartDateInput = element(by.id('field_reportStartDate'));
  reportEndDateInput = element(by.id('field_reportEndDate'));
  requestIdInput = element(by.id('field_requestId'));
  tamperedInput = element(by.id('field_tampered'));
  reportParametersInput = element(by.id('field_reportParameters'));
  reportFileInput = element(by.id('file_reportFile'));

  requestedBySelect = element(by.id('field_requestedBy'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setTimeOfRequestInput(timeOfRequest: string): Promise<void> {
    await this.timeOfRequestInput.sendKeys(timeOfRequest);
  }

  async getTimeOfRequestInput(): Promise<string> {
    return await this.timeOfRequestInput.getAttribute('value');
  }

  async setReportStartDateInput(reportStartDate: string): Promise<void> {
    await this.reportStartDateInput.sendKeys(reportStartDate);
  }

  async getReportStartDateInput(): Promise<string> {
    return await this.reportStartDateInput.getAttribute('value');
  }

  async setReportEndDateInput(reportEndDate: string): Promise<void> {
    await this.reportEndDateInput.sendKeys(reportEndDate);
  }

  async getReportEndDateInput(): Promise<string> {
    return await this.reportEndDateInput.getAttribute('value');
  }

  async setRequestIdInput(requestId: string): Promise<void> {
    await this.requestIdInput.sendKeys(requestId);
  }

  async getRequestIdInput(): Promise<string> {
    return await this.requestIdInput.getAttribute('value');
  }

  getTamperedInput(): ElementFinder {
    return this.tamperedInput;
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

export class AssetAdditionsReportDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-assetAdditionsReport-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-assetAdditionsReport'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
