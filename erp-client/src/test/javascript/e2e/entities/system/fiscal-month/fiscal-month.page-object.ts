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

export class FiscalMonthComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-fiscal-month div table .btn-danger'));
  title = element.all(by.css('jhi-fiscal-month div h2#page-heading span')).first();
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

export class FiscalMonthUpdatePage {
  pageTitle = element(by.id('jhi-fiscal-month-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  monthNumberInput = element(by.id('field_monthNumber'));
  startDateInput = element(by.id('field_startDate'));
  endDateInput = element(by.id('field_endDate'));
  fiscalMonthCodeInput = element(by.id('field_fiscalMonthCode'));

  fiscalYearSelect = element(by.id('field_fiscalYear'));
  placeholderSelect = element(by.id('field_placeholder'));
  universallyUniqueMappingSelect = element(by.id('field_universallyUniqueMapping'));
  fiscalQuarterSelect = element(by.id('field_fiscalQuarter'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setMonthNumberInput(monthNumber: string): Promise<void> {
    await this.monthNumberInput.sendKeys(monthNumber);
  }

  async getMonthNumberInput(): Promise<string> {
    return await this.monthNumberInput.getAttribute('value');
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

  async setFiscalMonthCodeInput(fiscalMonthCode: string): Promise<void> {
    await this.fiscalMonthCodeInput.sendKeys(fiscalMonthCode);
  }

  async getFiscalMonthCodeInput(): Promise<string> {
    return await this.fiscalMonthCodeInput.getAttribute('value');
  }

  async fiscalYearSelectLastOption(): Promise<void> {
    await this.fiscalYearSelect.all(by.tagName('option')).last().click();
  }

  async fiscalYearSelectOption(option: string): Promise<void> {
    await this.fiscalYearSelect.sendKeys(option);
  }

  getFiscalYearSelect(): ElementFinder {
    return this.fiscalYearSelect;
  }

  async getFiscalYearSelectedOption(): Promise<string> {
    return await this.fiscalYearSelect.element(by.css('option:checked')).getText();
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

  async fiscalQuarterSelectLastOption(): Promise<void> {
    await this.fiscalQuarterSelect.all(by.tagName('option')).last().click();
  }

  async fiscalQuarterSelectOption(option: string): Promise<void> {
    await this.fiscalQuarterSelect.sendKeys(option);
  }

  getFiscalQuarterSelect(): ElementFinder {
    return this.fiscalQuarterSelect;
  }

  async getFiscalQuarterSelectedOption(): Promise<string> {
    return await this.fiscalQuarterSelect.element(by.css('option:checked')).getText();
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

export class FiscalMonthDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-fiscalMonth-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-fiscalMonth'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
