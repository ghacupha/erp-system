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

export class ExcelReportExportComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-excel-report-export div table .btn-danger'));
  title = element.all(by.css('jhi-excel-report-export div h2#page-heading span')).first();
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

export class ExcelReportExportUpdatePage {
  pageTitle = element(by.id('jhi-excel-report-export-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  reportNameInput = element(by.id('field_reportName'));
  reportPasswordInput = element(by.id('field_reportPassword'));
  reportNotesInput = element(by.id('file_reportNotes'));
  fileCheckSumInput = element(by.id('field_fileCheckSum'));
  reportFileInput = element(by.id('file_reportFile'));
  reportTimeStampInput = element(by.id('field_reportTimeStamp'));
  reportIdInput = element(by.id('field_reportId'));

  placeholderSelect = element(by.id('field_placeholder'));
  parametersSelect = element(by.id('field_parameters'));
  reportStatusSelect = element(by.id('field_reportStatus'));
  securityClearanceSelect = element(by.id('field_securityClearance'));
  reportCreatorSelect = element(by.id('field_reportCreator'));
  organizationSelect = element(by.id('field_organization'));
  departmentSelect = element(by.id('field_department'));
  systemModuleSelect = element(by.id('field_systemModule'));
  reportDesignSelect = element(by.id('field_reportDesign'));
  fileCheckSumAlgorithmSelect = element(by.id('field_fileCheckSumAlgorithm'));

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

  async setReportPasswordInput(reportPassword: string): Promise<void> {
    await this.reportPasswordInput.sendKeys(reportPassword);
  }

  async getReportPasswordInput(): Promise<string> {
    return await this.reportPasswordInput.getAttribute('value');
  }

  async setReportNotesInput(reportNotes: string): Promise<void> {
    await this.reportNotesInput.sendKeys(reportNotes);
  }

  async getReportNotesInput(): Promise<string> {
    return await this.reportNotesInput.getAttribute('value');
  }

  async setFileCheckSumInput(fileCheckSum: string): Promise<void> {
    await this.fileCheckSumInput.sendKeys(fileCheckSum);
  }

  async getFileCheckSumInput(): Promise<string> {
    return await this.fileCheckSumInput.getAttribute('value');
  }

  async setReportFileInput(reportFile: string): Promise<void> {
    await this.reportFileInput.sendKeys(reportFile);
  }

  async getReportFileInput(): Promise<string> {
    return await this.reportFileInput.getAttribute('value');
  }

  async setReportTimeStampInput(reportTimeStamp: string): Promise<void> {
    await this.reportTimeStampInput.sendKeys(reportTimeStamp);
  }

  async getReportTimeStampInput(): Promise<string> {
    return await this.reportTimeStampInput.getAttribute('value');
  }

  async setReportIdInput(reportId: string): Promise<void> {
    await this.reportIdInput.sendKeys(reportId);
  }

  async getReportIdInput(): Promise<string> {
    return await this.reportIdInput.getAttribute('value');
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

  async reportStatusSelectLastOption(): Promise<void> {
    await this.reportStatusSelect.all(by.tagName('option')).last().click();
  }

  async reportStatusSelectOption(option: string): Promise<void> {
    await this.reportStatusSelect.sendKeys(option);
  }

  getReportStatusSelect(): ElementFinder {
    return this.reportStatusSelect;
  }

  async getReportStatusSelectedOption(): Promise<string> {
    return await this.reportStatusSelect.element(by.css('option:checked')).getText();
  }

  async securityClearanceSelectLastOption(): Promise<void> {
    await this.securityClearanceSelect.all(by.tagName('option')).last().click();
  }

  async securityClearanceSelectOption(option: string): Promise<void> {
    await this.securityClearanceSelect.sendKeys(option);
  }

  getSecurityClearanceSelect(): ElementFinder {
    return this.securityClearanceSelect;
  }

  async getSecurityClearanceSelectedOption(): Promise<string> {
    return await this.securityClearanceSelect.element(by.css('option:checked')).getText();
  }

  async reportCreatorSelectLastOption(): Promise<void> {
    await this.reportCreatorSelect.all(by.tagName('option')).last().click();
  }

  async reportCreatorSelectOption(option: string): Promise<void> {
    await this.reportCreatorSelect.sendKeys(option);
  }

  getReportCreatorSelect(): ElementFinder {
    return this.reportCreatorSelect;
  }

  async getReportCreatorSelectedOption(): Promise<string> {
    return await this.reportCreatorSelect.element(by.css('option:checked')).getText();
  }

  async organizationSelectLastOption(): Promise<void> {
    await this.organizationSelect.all(by.tagName('option')).last().click();
  }

  async organizationSelectOption(option: string): Promise<void> {
    await this.organizationSelect.sendKeys(option);
  }

  getOrganizationSelect(): ElementFinder {
    return this.organizationSelect;
  }

  async getOrganizationSelectedOption(): Promise<string> {
    return await this.organizationSelect.element(by.css('option:checked')).getText();
  }

  async departmentSelectLastOption(): Promise<void> {
    await this.departmentSelect.all(by.tagName('option')).last().click();
  }

  async departmentSelectOption(option: string): Promise<void> {
    await this.departmentSelect.sendKeys(option);
  }

  getDepartmentSelect(): ElementFinder {
    return this.departmentSelect;
  }

  async getDepartmentSelectedOption(): Promise<string> {
    return await this.departmentSelect.element(by.css('option:checked')).getText();
  }

  async systemModuleSelectLastOption(): Promise<void> {
    await this.systemModuleSelect.all(by.tagName('option')).last().click();
  }

  async systemModuleSelectOption(option: string): Promise<void> {
    await this.systemModuleSelect.sendKeys(option);
  }

  getSystemModuleSelect(): ElementFinder {
    return this.systemModuleSelect;
  }

  async getSystemModuleSelectedOption(): Promise<string> {
    return await this.systemModuleSelect.element(by.css('option:checked')).getText();
  }

  async reportDesignSelectLastOption(): Promise<void> {
    await this.reportDesignSelect.all(by.tagName('option')).last().click();
  }

  async reportDesignSelectOption(option: string): Promise<void> {
    await this.reportDesignSelect.sendKeys(option);
  }

  getReportDesignSelect(): ElementFinder {
    return this.reportDesignSelect;
  }

  async getReportDesignSelectedOption(): Promise<string> {
    return await this.reportDesignSelect.element(by.css('option:checked')).getText();
  }

  async fileCheckSumAlgorithmSelectLastOption(): Promise<void> {
    await this.fileCheckSumAlgorithmSelect.all(by.tagName('option')).last().click();
  }

  async fileCheckSumAlgorithmSelectOption(option: string): Promise<void> {
    await this.fileCheckSumAlgorithmSelect.sendKeys(option);
  }

  getFileCheckSumAlgorithmSelect(): ElementFinder {
    return this.fileCheckSumAlgorithmSelect;
  }

  async getFileCheckSumAlgorithmSelectedOption(): Promise<string> {
    return await this.fileCheckSumAlgorithmSelect.element(by.css('option:checked')).getText();
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

export class ExcelReportExportDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-excelReportExport-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-excelReportExport'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
