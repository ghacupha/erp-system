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

export class XlsxReportRequisitionComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-xlsx-report-requisition div table .btn-danger'));
  title = element.all(by.css('jhi-xlsx-report-requisition div h2#page-heading span')).first();
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

export class XlsxReportRequisitionUpdatePage {
  pageTitle = element(by.id('jhi-xlsx-report-requisition-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  reportNameInput = element(by.id('field_reportName'));
  reportDateInput = element(by.id('field_reportDate'));
  userPasswordInput = element(by.id('field_userPassword'));
  reportFileChecksumInput = element(by.id('field_reportFileChecksum'));
  reportStatusSelect = element(by.id('field_reportStatus'));
  reportIdInput = element(by.id('field_reportId'));

  reportTemplateSelect = element(by.id('field_reportTemplate'));
  placeholderSelect = element(by.id('field_placeholder'));
  parametersSelect = element(by.id('field_parameters'));

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

  async setReportDateInput(reportDate: string): Promise<void> {
    await this.reportDateInput.sendKeys(reportDate);
  }

  async getReportDateInput(): Promise<string> {
    return await this.reportDateInput.getAttribute('value');
  }

  async setUserPasswordInput(userPassword: string): Promise<void> {
    await this.userPasswordInput.sendKeys(userPassword);
  }

  async getUserPasswordInput(): Promise<string> {
    return await this.userPasswordInput.getAttribute('value');
  }

  async setReportFileChecksumInput(reportFileChecksum: string): Promise<void> {
    await this.reportFileChecksumInput.sendKeys(reportFileChecksum);
  }

  async getReportFileChecksumInput(): Promise<string> {
    return await this.reportFileChecksumInput.getAttribute('value');
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

export class XlsxReportRequisitionDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-xlsxReportRequisition-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-xlsxReportRequisition'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
