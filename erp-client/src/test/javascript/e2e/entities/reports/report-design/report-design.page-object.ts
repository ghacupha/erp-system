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

export class ReportDesignComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-report-design div table .btn-danger'));
  title = element.all(by.css('jhi-report-design div h2#page-heading span')).first();
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

export class ReportDesignUpdatePage {
  pageTitle = element(by.id('jhi-report-design-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  catalogueNumberInput = element(by.id('field_catalogueNumber'));
  designationInput = element(by.id('field_designation'));
  descriptionInput = element(by.id('field_description'));
  notesInput = element(by.id('file_notes'));
  reportFileInput = element(by.id('file_reportFile'));
  reportFileChecksumInput = element(by.id('field_reportFileChecksum'));

  parametersSelect = element(by.id('field_parameters'));
  securityClearanceSelect = element(by.id('field_securityClearance'));
  reportDesignerSelect = element(by.id('field_reportDesigner'));
  organizationSelect = element(by.id('field_organization'));
  departmentSelect = element(by.id('field_department'));
  placeholderSelect = element(by.id('field_placeholder'));
  systemModuleSelect = element(by.id('field_systemModule'));
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

  async setCatalogueNumberInput(catalogueNumber: string): Promise<void> {
    await this.catalogueNumberInput.sendKeys(catalogueNumber);
  }

  async getCatalogueNumberInput(): Promise<string> {
    return await this.catalogueNumberInput.getAttribute('value');
  }

  async setDesignationInput(designation: string): Promise<void> {
    await this.designationInput.sendKeys(designation);
  }

  async getDesignationInput(): Promise<string> {
    return await this.designationInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setNotesInput(notes: string): Promise<void> {
    await this.notesInput.sendKeys(notes);
  }

  async getNotesInput(): Promise<string> {
    return await this.notesInput.getAttribute('value');
  }

  async setReportFileInput(reportFile: string): Promise<void> {
    await this.reportFileInput.sendKeys(reportFile);
  }

  async getReportFileInput(): Promise<string> {
    return await this.reportFileInput.getAttribute('value');
  }

  async setReportFileChecksumInput(reportFileChecksum: string): Promise<void> {
    await this.reportFileChecksumInput.sendKeys(reportFileChecksum);
  }

  async getReportFileChecksumInput(): Promise<string> {
    return await this.reportFileChecksumInput.getAttribute('value');
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

  async reportDesignerSelectLastOption(): Promise<void> {
    await this.reportDesignerSelect.all(by.tagName('option')).last().click();
  }

  async reportDesignerSelectOption(option: string): Promise<void> {
    await this.reportDesignerSelect.sendKeys(option);
  }

  getReportDesignerSelect(): ElementFinder {
    return this.reportDesignerSelect;
  }

  async getReportDesignerSelectedOption(): Promise<string> {
    return await this.reportDesignerSelect.element(by.css('option:checked')).getText();
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

export class ReportDesignDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-reportDesign-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-reportDesign'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
