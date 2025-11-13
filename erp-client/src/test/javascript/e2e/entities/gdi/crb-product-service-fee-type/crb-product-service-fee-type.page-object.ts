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

export class CrbProductServiceFeeTypeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-crb-product-service-fee-type div table .btn-danger'));
  title = element.all(by.css('jhi-crb-product-service-fee-type div h2#page-heading span')).first();
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

export class CrbProductServiceFeeTypeUpdatePage {
  pageTitle = element(by.id('jhi-crb-product-service-fee-type-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  chargeTypeCodeInput = element(by.id('field_chargeTypeCode'));
  chargeTypeDescriptionInput = element(by.id('field_chargeTypeDescription'));
  chargeTypeCategoryInput = element(by.id('field_chargeTypeCategory'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setChargeTypeCodeInput(chargeTypeCode: string): Promise<void> {
    await this.chargeTypeCodeInput.sendKeys(chargeTypeCode);
  }

  async getChargeTypeCodeInput(): Promise<string> {
    return await this.chargeTypeCodeInput.getAttribute('value');
  }

  async setChargeTypeDescriptionInput(chargeTypeDescription: string): Promise<void> {
    await this.chargeTypeDescriptionInput.sendKeys(chargeTypeDescription);
  }

  async getChargeTypeDescriptionInput(): Promise<string> {
    return await this.chargeTypeDescriptionInput.getAttribute('value');
  }

  async setChargeTypeCategoryInput(chargeTypeCategory: string): Promise<void> {
    await this.chargeTypeCategoryInput.sendKeys(chargeTypeCategory);
  }

  async getChargeTypeCategoryInput(): Promise<string> {
    return await this.chargeTypeCategoryInput.getAttribute('value');
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

export class CrbProductServiceFeeTypeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-crbProductServiceFeeType-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-crbProductServiceFeeType'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
