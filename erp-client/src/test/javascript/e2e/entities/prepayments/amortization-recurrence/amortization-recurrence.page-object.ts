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

export class AmortizationRecurrenceComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-amortization-recurrence div table .btn-danger'));
  title = element.all(by.css('jhi-amortization-recurrence div h2#page-heading span')).first();
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

export class AmortizationRecurrenceUpdatePage {
  pageTitle = element(by.id('jhi-amortization-recurrence-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  firstAmortizationDateInput = element(by.id('field_firstAmortizationDate'));
  amortizationFrequencySelect = element(by.id('field_amortizationFrequency'));
  numberOfRecurrencesInput = element(by.id('field_numberOfRecurrences'));
  notesInput = element(by.id('file_notes'));
  particularsInput = element(by.id('field_particulars'));
  isActiveInput = element(by.id('field_isActive'));
  isOverWrittenInput = element(by.id('field_isOverWritten'));
  timeOfInstallationInput = element(by.id('field_timeOfInstallation'));
  recurrenceGuidInput = element(by.id('field_recurrenceGuid'));
  prepaymentAccountGuidInput = element(by.id('field_prepaymentAccountGuid'));

  placeholderSelect = element(by.id('field_placeholder'));
  parametersSelect = element(by.id('field_parameters'));
  applicationParametersSelect = element(by.id('field_applicationParameters'));
  depreciationMethodSelect = element(by.id('field_depreciationMethod'));
  prepaymentAccountSelect = element(by.id('field_prepaymentAccount'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setFirstAmortizationDateInput(firstAmortizationDate: string): Promise<void> {
    await this.firstAmortizationDateInput.sendKeys(firstAmortizationDate);
  }

  async getFirstAmortizationDateInput(): Promise<string> {
    return await this.firstAmortizationDateInput.getAttribute('value');
  }

  async setAmortizationFrequencySelect(amortizationFrequency: string): Promise<void> {
    await this.amortizationFrequencySelect.sendKeys(amortizationFrequency);
  }

  async getAmortizationFrequencySelect(): Promise<string> {
    return await this.amortizationFrequencySelect.element(by.css('option:checked')).getText();
  }

  async amortizationFrequencySelectLastOption(): Promise<void> {
    await this.amortizationFrequencySelect.all(by.tagName('option')).last().click();
  }

  async setNumberOfRecurrencesInput(numberOfRecurrences: string): Promise<void> {
    await this.numberOfRecurrencesInput.sendKeys(numberOfRecurrences);
  }

  async getNumberOfRecurrencesInput(): Promise<string> {
    return await this.numberOfRecurrencesInput.getAttribute('value');
  }

  async setNotesInput(notes: string): Promise<void> {
    await this.notesInput.sendKeys(notes);
  }

  async getNotesInput(): Promise<string> {
    return await this.notesInput.getAttribute('value');
  }

  async setParticularsInput(particulars: string): Promise<void> {
    await this.particularsInput.sendKeys(particulars);
  }

  async getParticularsInput(): Promise<string> {
    return await this.particularsInput.getAttribute('value');
  }

  getIsActiveInput(): ElementFinder {
    return this.isActiveInput;
  }

  getIsOverWrittenInput(): ElementFinder {
    return this.isOverWrittenInput;
  }

  async setTimeOfInstallationInput(timeOfInstallation: string): Promise<void> {
    await this.timeOfInstallationInput.sendKeys(timeOfInstallation);
  }

  async getTimeOfInstallationInput(): Promise<string> {
    return await this.timeOfInstallationInput.getAttribute('value');
  }

  async setRecurrenceGuidInput(recurrenceGuid: string): Promise<void> {
    await this.recurrenceGuidInput.sendKeys(recurrenceGuid);
  }

  async getRecurrenceGuidInput(): Promise<string> {
    return await this.recurrenceGuidInput.getAttribute('value');
  }

  async setPrepaymentAccountGuidInput(prepaymentAccountGuid: string): Promise<void> {
    await this.prepaymentAccountGuidInput.sendKeys(prepaymentAccountGuid);
  }

  async getPrepaymentAccountGuidInput(): Promise<string> {
    return await this.prepaymentAccountGuidInput.getAttribute('value');
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

  async applicationParametersSelectLastOption(): Promise<void> {
    await this.applicationParametersSelect.all(by.tagName('option')).last().click();
  }

  async applicationParametersSelectOption(option: string): Promise<void> {
    await this.applicationParametersSelect.sendKeys(option);
  }

  getApplicationParametersSelect(): ElementFinder {
    return this.applicationParametersSelect;
  }

  async getApplicationParametersSelectedOption(): Promise<string> {
    return await this.applicationParametersSelect.element(by.css('option:checked')).getText();
  }

  async depreciationMethodSelectLastOption(): Promise<void> {
    await this.depreciationMethodSelect.all(by.tagName('option')).last().click();
  }

  async depreciationMethodSelectOption(option: string): Promise<void> {
    await this.depreciationMethodSelect.sendKeys(option);
  }

  getDepreciationMethodSelect(): ElementFinder {
    return this.depreciationMethodSelect;
  }

  async getDepreciationMethodSelectedOption(): Promise<string> {
    return await this.depreciationMethodSelect.element(by.css('option:checked')).getText();
  }

  async prepaymentAccountSelectLastOption(): Promise<void> {
    await this.prepaymentAccountSelect.all(by.tagName('option')).last().click();
  }

  async prepaymentAccountSelectOption(option: string): Promise<void> {
    await this.prepaymentAccountSelect.sendKeys(option);
  }

  getPrepaymentAccountSelect(): ElementFinder {
    return this.prepaymentAccountSelect;
  }

  async getPrepaymentAccountSelectedOption(): Promise<string> {
    return await this.prepaymentAccountSelect.element(by.css('option:checked')).getText();
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

export class AmortizationRecurrenceDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-amortizationRecurrence-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-amortizationRecurrence'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
