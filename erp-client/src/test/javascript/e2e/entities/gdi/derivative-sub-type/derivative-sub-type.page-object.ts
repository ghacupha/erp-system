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

export class DerivativeSubTypeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-derivative-sub-type div table .btn-danger'));
  title = element.all(by.css('jhi-derivative-sub-type div h2#page-heading span')).first();
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

export class DerivativeSubTypeUpdatePage {
  pageTitle = element(by.id('jhi-derivative-sub-type-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  financialDerivativeSubTypeCodeInput = element(by.id('field_financialDerivativeSubTypeCode'));
  financialDerivativeSubTyeInput = element(by.id('field_financialDerivativeSubTye'));
  financialDerivativeSubtypeDetailsInput = element(by.id('field_financialDerivativeSubtypeDetails'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setFinancialDerivativeSubTypeCodeInput(financialDerivativeSubTypeCode: string): Promise<void> {
    await this.financialDerivativeSubTypeCodeInput.sendKeys(financialDerivativeSubTypeCode);
  }

  async getFinancialDerivativeSubTypeCodeInput(): Promise<string> {
    return await this.financialDerivativeSubTypeCodeInput.getAttribute('value');
  }

  async setFinancialDerivativeSubTyeInput(financialDerivativeSubTye: string): Promise<void> {
    await this.financialDerivativeSubTyeInput.sendKeys(financialDerivativeSubTye);
  }

  async getFinancialDerivativeSubTyeInput(): Promise<string> {
    return await this.financialDerivativeSubTyeInput.getAttribute('value');
  }

  async setFinancialDerivativeSubtypeDetailsInput(financialDerivativeSubtypeDetails: string): Promise<void> {
    await this.financialDerivativeSubtypeDetailsInput.sendKeys(financialDerivativeSubtypeDetails);
  }

  async getFinancialDerivativeSubtypeDetailsInput(): Promise<string> {
    return await this.financialDerivativeSubtypeDetailsInput.getAttribute('value');
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

export class DerivativeSubTypeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-derivativeSubType-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-derivativeSubType'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
