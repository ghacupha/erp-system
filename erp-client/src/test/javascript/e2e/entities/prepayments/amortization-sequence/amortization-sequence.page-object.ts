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

export class AmortizationSequenceComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-amortization-sequence div table .btn-danger'));
  title = element.all(by.css('jhi-amortization-sequence div h2#page-heading span')).first();
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

export class AmortizationSequenceUpdatePage {
  pageTitle = element(by.id('jhi-amortization-sequence-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  prepaymentAccountGuidInput = element(by.id('field_prepaymentAccountGuid'));
  recurrenceGuidInput = element(by.id('field_recurrenceGuid'));
  sequenceNumberInput = element(by.id('field_sequenceNumber'));
  particularsInput = element(by.id('field_particulars'));
  currentAmortizationDateInput = element(by.id('field_currentAmortizationDate'));
  previousAmortizationDateInput = element(by.id('field_previousAmortizationDate'));
  nextAmortizationDateInput = element(by.id('field_nextAmortizationDate'));
  isCommencementSequenceInput = element(by.id('field_isCommencementSequence'));
  isTerminalSequenceInput = element(by.id('field_isTerminalSequence'));
  amortizationAmountInput = element(by.id('field_amortizationAmount'));
  sequenceGuidInput = element(by.id('field_sequenceGuid'));

  prepaymentAccountSelect = element(by.id('field_prepaymentAccount'));
  amortizationRecurrenceSelect = element(by.id('field_amortizationRecurrence'));
  placeholderSelect = element(by.id('field_placeholder'));
  prepaymentMappingSelect = element(by.id('field_prepaymentMapping'));
  applicationParametersSelect = element(by.id('field_applicationParameters'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setPrepaymentAccountGuidInput(prepaymentAccountGuid: string): Promise<void> {
    await this.prepaymentAccountGuidInput.sendKeys(prepaymentAccountGuid);
  }

  async getPrepaymentAccountGuidInput(): Promise<string> {
    return await this.prepaymentAccountGuidInput.getAttribute('value');
  }

  async setRecurrenceGuidInput(recurrenceGuid: string): Promise<void> {
    await this.recurrenceGuidInput.sendKeys(recurrenceGuid);
  }

  async getRecurrenceGuidInput(): Promise<string> {
    return await this.recurrenceGuidInput.getAttribute('value');
  }

  async setSequenceNumberInput(sequenceNumber: string): Promise<void> {
    await this.sequenceNumberInput.sendKeys(sequenceNumber);
  }

  async getSequenceNumberInput(): Promise<string> {
    return await this.sequenceNumberInput.getAttribute('value');
  }

  async setParticularsInput(particulars: string): Promise<void> {
    await this.particularsInput.sendKeys(particulars);
  }

  async getParticularsInput(): Promise<string> {
    return await this.particularsInput.getAttribute('value');
  }

  async setCurrentAmortizationDateInput(currentAmortizationDate: string): Promise<void> {
    await this.currentAmortizationDateInput.sendKeys(currentAmortizationDate);
  }

  async getCurrentAmortizationDateInput(): Promise<string> {
    return await this.currentAmortizationDateInput.getAttribute('value');
  }

  async setPreviousAmortizationDateInput(previousAmortizationDate: string): Promise<void> {
    await this.previousAmortizationDateInput.sendKeys(previousAmortizationDate);
  }

  async getPreviousAmortizationDateInput(): Promise<string> {
    return await this.previousAmortizationDateInput.getAttribute('value');
  }

  async setNextAmortizationDateInput(nextAmortizationDate: string): Promise<void> {
    await this.nextAmortizationDateInput.sendKeys(nextAmortizationDate);
  }

  async getNextAmortizationDateInput(): Promise<string> {
    return await this.nextAmortizationDateInput.getAttribute('value');
  }

  getIsCommencementSequenceInput(): ElementFinder {
    return this.isCommencementSequenceInput;
  }

  getIsTerminalSequenceInput(): ElementFinder {
    return this.isTerminalSequenceInput;
  }

  async setAmortizationAmountInput(amortizationAmount: string): Promise<void> {
    await this.amortizationAmountInput.sendKeys(amortizationAmount);
  }

  async getAmortizationAmountInput(): Promise<string> {
    return await this.amortizationAmountInput.getAttribute('value');
  }

  async setSequenceGuidInput(sequenceGuid: string): Promise<void> {
    await this.sequenceGuidInput.sendKeys(sequenceGuid);
  }

  async getSequenceGuidInput(): Promise<string> {
    return await this.sequenceGuidInput.getAttribute('value');
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

  async amortizationRecurrenceSelectLastOption(): Promise<void> {
    await this.amortizationRecurrenceSelect.all(by.tagName('option')).last().click();
  }

  async amortizationRecurrenceSelectOption(option: string): Promise<void> {
    await this.amortizationRecurrenceSelect.sendKeys(option);
  }

  getAmortizationRecurrenceSelect(): ElementFinder {
    return this.amortizationRecurrenceSelect;
  }

  async getAmortizationRecurrenceSelectedOption(): Promise<string> {
    return await this.amortizationRecurrenceSelect.element(by.css('option:checked')).getText();
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

  async prepaymentMappingSelectLastOption(): Promise<void> {
    await this.prepaymentMappingSelect.all(by.tagName('option')).last().click();
  }

  async prepaymentMappingSelectOption(option: string): Promise<void> {
    await this.prepaymentMappingSelect.sendKeys(option);
  }

  getPrepaymentMappingSelect(): ElementFinder {
    return this.prepaymentMappingSelect;
  }

  async getPrepaymentMappingSelectedOption(): Promise<string> {
    return await this.prepaymentMappingSelect.element(by.css('option:checked')).getText();
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

export class AmortizationSequenceDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-amortizationSequence-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-amortizationSequence'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
