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

export class FiscalYearComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-fiscal-year div table .btn-danger'));
  title = element.all(by.css('jhi-fiscal-year div h2#page-heading span')).first();
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

export class FiscalYearUpdatePage {
  pageTitle = element(by.id('jhi-fiscal-year-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  fiscalYearCodeInput = element(by.id('field_fiscalYearCode'));
  startDateInput = element(by.id('field_startDate'));
  endDateInput = element(by.id('field_endDate'));
  fiscalYearStatusSelect = element(by.id('field_fiscalYearStatus'));

  placeholderSelect = element(by.id('field_placeholder'));
  universallyUniqueMappingSelect = element(by.id('field_universallyUniqueMapping'));
  createdBySelect = element(by.id('field_createdBy'));
  lastUpdatedBySelect = element(by.id('field_lastUpdatedBy'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setFiscalYearCodeInput(fiscalYearCode: string): Promise<void> {
    await this.fiscalYearCodeInput.sendKeys(fiscalYearCode);
  }

  async getFiscalYearCodeInput(): Promise<string> {
    return await this.fiscalYearCodeInput.getAttribute('value');
  }

  async setStartDateInput(startDate: string): Promise<void> {
    await this.startDateInput.sendKeys(startDate);
  }

  async getStartDateInput(): Promise<string> {
    return await this.startDateInput.getAttribute('value');
  }

  async setEndDateInput(endDate: string): Promise<void> {
    await this.endDateInput.sendKeys(endDate);
  }

  async getEndDateInput(): Promise<string> {
    return await this.endDateInput.getAttribute('value');
  }

  async setFiscalYearStatusSelect(fiscalYearStatus: string): Promise<void> {
    await this.fiscalYearStatusSelect.sendKeys(fiscalYearStatus);
  }

  async getFiscalYearStatusSelect(): Promise<string> {
    return await this.fiscalYearStatusSelect.element(by.css('option:checked')).getText();
  }

  async fiscalYearStatusSelectLastOption(): Promise<void> {
    await this.fiscalYearStatusSelect.all(by.tagName('option')).last().click();
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

  async universallyUniqueMappingSelectLastOption(): Promise<void> {
    await this.universallyUniqueMappingSelect.all(by.tagName('option')).last().click();
  }

  async universallyUniqueMappingSelectOption(option: string): Promise<void> {
    await this.universallyUniqueMappingSelect.sendKeys(option);
  }

  getUniversallyUniqueMappingSelect(): ElementFinder {
    return this.universallyUniqueMappingSelect;
  }

  async getUniversallyUniqueMappingSelectedOption(): Promise<string> {
    return await this.universallyUniqueMappingSelect.element(by.css('option:checked')).getText();
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

  async lastUpdatedBySelectLastOption(): Promise<void> {
    await this.lastUpdatedBySelect.all(by.tagName('option')).last().click();
  }

  async lastUpdatedBySelectOption(option: string): Promise<void> {
    await this.lastUpdatedBySelect.sendKeys(option);
  }

  getLastUpdatedBySelect(): ElementFinder {
    return this.lastUpdatedBySelect;
  }

  async getLastUpdatedBySelectedOption(): Promise<string> {
    return await this.lastUpdatedBySelect.element(by.css('option:checked')).getText();
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

export class FiscalYearDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-fiscalYear-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-fiscalYear'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
