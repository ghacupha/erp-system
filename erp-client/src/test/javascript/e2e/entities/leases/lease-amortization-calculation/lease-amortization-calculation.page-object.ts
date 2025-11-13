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

export class LeaseAmortizationCalculationComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-lease-amortization-calculation div table .btn-danger'));
  title = element.all(by.css('jhi-lease-amortization-calculation div h2#page-heading span')).first();
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

export class LeaseAmortizationCalculationUpdatePage {
  pageTitle = element(by.id('jhi-lease-amortization-calculation-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  interestRateInput = element(by.id('field_interestRate'));
  periodicityInput = element(by.id('field_periodicity'));
  leaseAmountInput = element(by.id('field_leaseAmount'));
  numberOfPeriodsInput = element(by.id('field_numberOfPeriods'));

  leaseContractSelect = element(by.id('field_leaseContract'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setInterestRateInput(interestRate: string): Promise<void> {
    await this.interestRateInput.sendKeys(interestRate);
  }

  async getInterestRateInput(): Promise<string> {
    return await this.interestRateInput.getAttribute('value');
  }

  async setPeriodicityInput(periodicity: string): Promise<void> {
    await this.periodicityInput.sendKeys(periodicity);
  }

  async getPeriodicityInput(): Promise<string> {
    return await this.periodicityInput.getAttribute('value');
  }

  async setLeaseAmountInput(leaseAmount: string): Promise<void> {
    await this.leaseAmountInput.sendKeys(leaseAmount);
  }

  async getLeaseAmountInput(): Promise<string> {
    return await this.leaseAmountInput.getAttribute('value');
  }

  async setNumberOfPeriodsInput(numberOfPeriods: string): Promise<void> {
    await this.numberOfPeriodsInput.sendKeys(numberOfPeriods);
  }

  async getNumberOfPeriodsInput(): Promise<string> {
    return await this.numberOfPeriodsInput.getAttribute('value');
  }

  async leaseContractSelectLastOption(): Promise<void> {
    await this.leaseContractSelect.all(by.tagName('option')).last().click();
  }

  async leaseContractSelectOption(option: string): Promise<void> {
    await this.leaseContractSelect.sendKeys(option);
  }

  getLeaseContractSelect(): ElementFinder {
    return this.leaseContractSelect;
  }

  async getLeaseContractSelectedOption(): Promise<string> {
    return await this.leaseContractSelect.element(by.css('option:checked')).getText();
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

export class LeaseAmortizationCalculationDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-leaseAmortizationCalculation-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-leaseAmortizationCalculation'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
