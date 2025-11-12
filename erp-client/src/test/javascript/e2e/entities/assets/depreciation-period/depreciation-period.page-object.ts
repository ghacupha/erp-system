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

export class DepreciationPeriodComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-depreciation-period div table .btn-danger'));
  title = element.all(by.css('jhi-depreciation-period div h2#page-heading span')).first();
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

export class DepreciationPeriodUpdatePage {
  pageTitle = element(by.id('jhi-depreciation-period-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  startDateInput = element(by.id('field_startDate'));
  endDateInput = element(by.id('field_endDate'));
  depreciationPeriodStatusSelect = element(by.id('field_depreciationPeriodStatus'));
  periodCodeInput = element(by.id('field_periodCode'));
  processLockedInput = element(by.id('field_processLocked'));

  previousPeriodSelect = element(by.id('field_previousPeriod'));
  createdBySelect = element(by.id('field_createdBy'));
  fiscalYearSelect = element(by.id('field_fiscalYear'));
  fiscalMonthSelect = element(by.id('field_fiscalMonth'));
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

  async setDepreciationPeriodStatusSelect(depreciationPeriodStatus: string): Promise<void> {
    await this.depreciationPeriodStatusSelect.sendKeys(depreciationPeriodStatus);
  }

  async getDepreciationPeriodStatusSelect(): Promise<string> {
    return await this.depreciationPeriodStatusSelect.element(by.css('option:checked')).getText();
  }

  async depreciationPeriodStatusSelectLastOption(): Promise<void> {
    await this.depreciationPeriodStatusSelect.all(by.tagName('option')).last().click();
  }

  async setPeriodCodeInput(periodCode: string): Promise<void> {
    await this.periodCodeInput.sendKeys(periodCode);
  }

  async getPeriodCodeInput(): Promise<string> {
    return await this.periodCodeInput.getAttribute('value');
  }

  getProcessLockedInput(): ElementFinder {
    return this.processLockedInput;
  }

  async previousPeriodSelectLastOption(): Promise<void> {
    await this.previousPeriodSelect.all(by.tagName('option')).last().click();
  }

  async previousPeriodSelectOption(option: string): Promise<void> {
    await this.previousPeriodSelect.sendKeys(option);
  }

  getPreviousPeriodSelect(): ElementFinder {
    return this.previousPeriodSelect;
  }

  async getPreviousPeriodSelectedOption(): Promise<string> {
    return await this.previousPeriodSelect.element(by.css('option:checked')).getText();
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

  async fiscalMonthSelectLastOption(): Promise<void> {
    await this.fiscalMonthSelect.all(by.tagName('option')).last().click();
  }

  async fiscalMonthSelectOption(option: string): Promise<void> {
    await this.fiscalMonthSelect.sendKeys(option);
  }

  getFiscalMonthSelect(): ElementFinder {
    return this.fiscalMonthSelect;
  }

  async getFiscalMonthSelectedOption(): Promise<string> {
    return await this.fiscalMonthSelect.element(by.css('option:checked')).getText();
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

export class DepreciationPeriodDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-depreciationPeriod-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-depreciationPeriod'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
