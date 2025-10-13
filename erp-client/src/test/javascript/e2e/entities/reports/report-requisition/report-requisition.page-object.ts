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

export class ReportRequisitionComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-report-requisition div table .btn-danger'));
  title = element.all(by.css('jhi-report-requisition div h2#page-heading span')).first();
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

export class ReportRequisitionUpdatePage {
  pageTitle = element(by.id('jhi-report-requisition-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  reportNameInput = element(by.id('field_reportName'));
  reportRequestTimeInput = element(by.id('field_reportRequestTime'));
  reportPasswordInput = element(by.id('field_reportPassword'));
  reportStatusSelect = element(by.id('field_reportStatus'));
  reportIdInput = element(by.id('field_reportId'));
  reportFileAttachmentInput = element(by.id('file_reportFileAttachment'));
  reportFileCheckSumInput = element(by.id('field_reportFileCheckSum'));
  reportNotesInput = element(by.id('file_reportNotes'));

  placeholdersSelect = element(by.id('field_placeholders'));
  parametersSelect = element(by.id('field_parameters'));
  reportTemplateSelect = element(by.id('field_reportTemplate'));
  reportContentTypeSelect = element(by.id('field_reportContentType'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setReportNameInput(reportName: string): Promise<void> {
    await this.reportNameInput.sendKeys(reportName);
  }

  async getReportNameInput(): Promise<string> {
    return await this.reportNameInput.getAttribute('value');
  }

  async setReportRequestTimeInput(reportRequestTime: string): Promise<void> {
    await this.reportRequestTimeInput.sendKeys(reportRequestTime);
  }

  async getReportRequestTimeInput(): Promise<string> {
    return await this.reportRequestTimeInput.getAttribute('value');
  }

  async setReportPasswordInput(reportPassword: string): Promise<void> {
    await this.reportPasswordInput.sendKeys(reportPassword);
  }

  async getReportPasswordInput(): Promise<string> {
    return await this.reportPasswordInput.getAttribute('value');
  }

  async setReportStatusSelect(reportStatus: string): Promise<void> {
    await this.reportStatusSelect.sendKeys(reportStatus);
  }

  async getReportStatusSelect(): Promise<string> {
    return await this.reportStatusSelect.element(by.css('option:checked')).getText();
  }

  async reportStatusSelectLastOption(): Promise<void> {
    await this.reportStatusSelect.all(by.tagName('option')).last().click();
  }

  async setReportIdInput(reportId: string): Promise<void> {
    await this.reportIdInput.sendKeys(reportId);
  }

  async getReportIdInput(): Promise<string> {
    return await this.reportIdInput.getAttribute('value');
  }

  async setReportFileAttachmentInput(reportFileAttachment: string): Promise<void> {
    await this.reportFileAttachmentInput.sendKeys(reportFileAttachment);
  }

  async getReportFileAttachmentInput(): Promise<string> {
    return await this.reportFileAttachmentInput.getAttribute('value');
  }

  async setReportFileCheckSumInput(reportFileCheckSum: string): Promise<void> {
    await this.reportFileCheckSumInput.sendKeys(reportFileCheckSum);
  }

  async getReportFileCheckSumInput(): Promise<string> {
    return await this.reportFileCheckSumInput.getAttribute('value');
  }

  async setReportNotesInput(reportNotes: string): Promise<void> {
    await this.reportNotesInput.sendKeys(reportNotes);
  }

  async getReportNotesInput(): Promise<string> {
    return await this.reportNotesInput.getAttribute('value');
  }

  async placeholdersSelectLastOption(): Promise<void> {
    await this.placeholdersSelect.all(by.tagName('option')).last().click();
  }

  async placeholdersSelectOption(option: string): Promise<void> {
    await this.placeholdersSelect.sendKeys(option);
  }

  getPlaceholdersSelect(): ElementFinder {
    return this.placeholdersSelect;
  }

  async getPlaceholdersSelectedOption(): Promise<string> {
    return await this.placeholdersSelect.element(by.css('option:checked')).getText();
  }

  async parametersSelectLastOption(): Promise<void> {
    await this.parametersSelect.all(by.tagName('option')).last().click();
  }

  async parametersSelectOption(option: string): Promise<void> {
    await this.parametersSelect.sendKeys(option);
  }

  getParametersSelect(): ElementFinder {
    return this.parametersSelect;
  }

  async getParametersSelectedOption(): Promise<string> {
    return await this.parametersSelect.element(by.css('option:checked')).getText();
  }

  async reportTemplateSelectLastOption(): Promise<void> {
    await this.reportTemplateSelect.all(by.tagName('option')).last().click();
  }

  async reportTemplateSelectOption(option: string): Promise<void> {
    await this.reportTemplateSelect.sendKeys(option);
  }

  getReportTemplateSelect(): ElementFinder {
    return this.reportTemplateSelect;
  }

  async getReportTemplateSelectedOption(): Promise<string> {
    return await this.reportTemplateSelect.element(by.css('option:checked')).getText();
  }

  async reportContentTypeSelectLastOption(): Promise<void> {
    await this.reportContentTypeSelect.all(by.tagName('option')).last().click();
  }

  async reportContentTypeSelectOption(option: string): Promise<void> {
    await this.reportContentTypeSelect.sendKeys(option);
  }

  getReportContentTypeSelect(): ElementFinder {
    return this.reportContentTypeSelect;
  }

  async getReportContentTypeSelectedOption(): Promise<string> {
    return await this.reportContentTypeSelect.element(by.css('option:checked')).getText();
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

export class ReportRequisitionDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-reportRequisition-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-reportRequisition'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
