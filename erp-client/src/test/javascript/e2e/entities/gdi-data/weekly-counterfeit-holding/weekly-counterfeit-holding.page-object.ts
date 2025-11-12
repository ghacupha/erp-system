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

export class WeeklyCounterfeitHoldingComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-weekly-counterfeit-holding div table .btn-danger'));
  title = element.all(by.css('jhi-weekly-counterfeit-holding div h2#page-heading span')).first();
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

export class WeeklyCounterfeitHoldingUpdatePage {
  pageTitle = element(by.id('jhi-weekly-counterfeit-holding-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  reportingDateInput = element(by.id('field_reportingDate'));
  dateConfiscatedInput = element(by.id('field_dateConfiscated'));
  serialNumberInput = element(by.id('field_serialNumber'));
  depositorsNamesInput = element(by.id('field_depositorsNames'));
  tellersNamesInput = element(by.id('field_tellersNames'));
  dateSubmittedToCBKInput = element(by.id('field_dateSubmittedToCBK'));
  remarksInput = element(by.id('field_remarks'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setReportingDateInput(reportingDate: string): Promise<void> {
    await this.reportingDateInput.sendKeys(reportingDate);
  }

  async getReportingDateInput(): Promise<string> {
    return await this.reportingDateInput.getAttribute('value');
  }

  async setDateConfiscatedInput(dateConfiscated: string): Promise<void> {
    await this.dateConfiscatedInput.sendKeys(dateConfiscated);
  }

  async getDateConfiscatedInput(): Promise<string> {
    return await this.dateConfiscatedInput.getAttribute('value');
  }

  async setSerialNumberInput(serialNumber: string): Promise<void> {
    await this.serialNumberInput.sendKeys(serialNumber);
  }

  async getSerialNumberInput(): Promise<string> {
    return await this.serialNumberInput.getAttribute('value');
  }

  async setDepositorsNamesInput(depositorsNames: string): Promise<void> {
    await this.depositorsNamesInput.sendKeys(depositorsNames);
  }

  async getDepositorsNamesInput(): Promise<string> {
    return await this.depositorsNamesInput.getAttribute('value');
  }

  async setTellersNamesInput(tellersNames: string): Promise<void> {
    await this.tellersNamesInput.sendKeys(tellersNames);
  }

  async getTellersNamesInput(): Promise<string> {
    return await this.tellersNamesInput.getAttribute('value');
  }

  async setDateSubmittedToCBKInput(dateSubmittedToCBK: string): Promise<void> {
    await this.dateSubmittedToCBKInput.sendKeys(dateSubmittedToCBK);
  }

  async getDateSubmittedToCBKInput(): Promise<string> {
    return await this.dateSubmittedToCBKInput.getAttribute('value');
  }

  async setRemarksInput(remarks: string): Promise<void> {
    await this.remarksInput.sendKeys(remarks);
  }

  async getRemarksInput(): Promise<string> {
    return await this.remarksInput.getAttribute('value');
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

export class WeeklyCounterfeitHoldingDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-weeklyCounterfeitHolding-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-weeklyCounterfeitHolding'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
