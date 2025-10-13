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

export class FxReceiptPurposeTypeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-fx-receipt-purpose-type div table .btn-danger'));
  title = element.all(by.css('jhi-fx-receipt-purpose-type div h2#page-heading span')).first();
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

export class FxReceiptPurposeTypeUpdatePage {
  pageTitle = element(by.id('jhi-fx-receipt-purpose-type-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  itemCodeInput = element(by.id('field_itemCode'));
  attribute1ReceiptPaymentPurposeCodeInput = element(by.id('field_attribute1ReceiptPaymentPurposeCode'));
  attribute1ReceiptPaymentPurposeTypeInput = element(by.id('field_attribute1ReceiptPaymentPurposeType'));
  attribute2ReceiptPaymentPurposeCodeInput = element(by.id('field_attribute2ReceiptPaymentPurposeCode'));
  attribute2ReceiptPaymentPurposeDescriptionInput = element(by.id('field_attribute2ReceiptPaymentPurposeDescription'));
  attribute3ReceiptPaymentPurposeCodeInput = element(by.id('field_attribute3ReceiptPaymentPurposeCode'));
  attribute3ReceiptPaymentPurposeDescriptionInput = element(by.id('field_attribute3ReceiptPaymentPurposeDescription'));
  attribute4ReceiptPaymentPurposeCodeInput = element(by.id('field_attribute4ReceiptPaymentPurposeCode'));
  attribute4ReceiptPaymentPurposeDescriptionInput = element(by.id('field_attribute4ReceiptPaymentPurposeDescription'));
  attribute5ReceiptPaymentPurposeCodeInput = element(by.id('field_attribute5ReceiptPaymentPurposeCode'));
  attribute5ReceiptPaymentPurposeDescriptionInput = element(by.id('field_attribute5ReceiptPaymentPurposeDescription'));
  lastChildInput = element(by.id('field_lastChild'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setItemCodeInput(itemCode: string): Promise<void> {
    await this.itemCodeInput.sendKeys(itemCode);
  }

  async getItemCodeInput(): Promise<string> {
    return await this.itemCodeInput.getAttribute('value');
  }

  async setAttribute1ReceiptPaymentPurposeCodeInput(attribute1ReceiptPaymentPurposeCode: string): Promise<void> {
    await this.attribute1ReceiptPaymentPurposeCodeInput.sendKeys(attribute1ReceiptPaymentPurposeCode);
  }

  async getAttribute1ReceiptPaymentPurposeCodeInput(): Promise<string> {
    return await this.attribute1ReceiptPaymentPurposeCodeInput.getAttribute('value');
  }

  async setAttribute1ReceiptPaymentPurposeTypeInput(attribute1ReceiptPaymentPurposeType: string): Promise<void> {
    await this.attribute1ReceiptPaymentPurposeTypeInput.sendKeys(attribute1ReceiptPaymentPurposeType);
  }

  async getAttribute1ReceiptPaymentPurposeTypeInput(): Promise<string> {
    return await this.attribute1ReceiptPaymentPurposeTypeInput.getAttribute('value');
  }

  async setAttribute2ReceiptPaymentPurposeCodeInput(attribute2ReceiptPaymentPurposeCode: string): Promise<void> {
    await this.attribute2ReceiptPaymentPurposeCodeInput.sendKeys(attribute2ReceiptPaymentPurposeCode);
  }

  async getAttribute2ReceiptPaymentPurposeCodeInput(): Promise<string> {
    return await this.attribute2ReceiptPaymentPurposeCodeInput.getAttribute('value');
  }

  async setAttribute2ReceiptPaymentPurposeDescriptionInput(attribute2ReceiptPaymentPurposeDescription: string): Promise<void> {
    await this.attribute2ReceiptPaymentPurposeDescriptionInput.sendKeys(attribute2ReceiptPaymentPurposeDescription);
  }

  async getAttribute2ReceiptPaymentPurposeDescriptionInput(): Promise<string> {
    return await this.attribute2ReceiptPaymentPurposeDescriptionInput.getAttribute('value');
  }

  async setAttribute3ReceiptPaymentPurposeCodeInput(attribute3ReceiptPaymentPurposeCode: string): Promise<void> {
    await this.attribute3ReceiptPaymentPurposeCodeInput.sendKeys(attribute3ReceiptPaymentPurposeCode);
  }

  async getAttribute3ReceiptPaymentPurposeCodeInput(): Promise<string> {
    return await this.attribute3ReceiptPaymentPurposeCodeInput.getAttribute('value');
  }

  async setAttribute3ReceiptPaymentPurposeDescriptionInput(attribute3ReceiptPaymentPurposeDescription: string): Promise<void> {
    await this.attribute3ReceiptPaymentPurposeDescriptionInput.sendKeys(attribute3ReceiptPaymentPurposeDescription);
  }

  async getAttribute3ReceiptPaymentPurposeDescriptionInput(): Promise<string> {
    return await this.attribute3ReceiptPaymentPurposeDescriptionInput.getAttribute('value');
  }

  async setAttribute4ReceiptPaymentPurposeCodeInput(attribute4ReceiptPaymentPurposeCode: string): Promise<void> {
    await this.attribute4ReceiptPaymentPurposeCodeInput.sendKeys(attribute4ReceiptPaymentPurposeCode);
  }

  async getAttribute4ReceiptPaymentPurposeCodeInput(): Promise<string> {
    return await this.attribute4ReceiptPaymentPurposeCodeInput.getAttribute('value');
  }

  async setAttribute4ReceiptPaymentPurposeDescriptionInput(attribute4ReceiptPaymentPurposeDescription: string): Promise<void> {
    await this.attribute4ReceiptPaymentPurposeDescriptionInput.sendKeys(attribute4ReceiptPaymentPurposeDescription);
  }

  async getAttribute4ReceiptPaymentPurposeDescriptionInput(): Promise<string> {
    return await this.attribute4ReceiptPaymentPurposeDescriptionInput.getAttribute('value');
  }

  async setAttribute5ReceiptPaymentPurposeCodeInput(attribute5ReceiptPaymentPurposeCode: string): Promise<void> {
    await this.attribute5ReceiptPaymentPurposeCodeInput.sendKeys(attribute5ReceiptPaymentPurposeCode);
  }

  async getAttribute5ReceiptPaymentPurposeCodeInput(): Promise<string> {
    return await this.attribute5ReceiptPaymentPurposeCodeInput.getAttribute('value');
  }

  async setAttribute5ReceiptPaymentPurposeDescriptionInput(attribute5ReceiptPaymentPurposeDescription: string): Promise<void> {
    await this.attribute5ReceiptPaymentPurposeDescriptionInput.sendKeys(attribute5ReceiptPaymentPurposeDescription);
  }

  async getAttribute5ReceiptPaymentPurposeDescriptionInput(): Promise<string> {
    return await this.attribute5ReceiptPaymentPurposeDescriptionInput.getAttribute('value');
  }

  async setLastChildInput(lastChild: string): Promise<void> {
    await this.lastChildInput.sendKeys(lastChild);
  }

  async getLastChildInput(): Promise<string> {
    return await this.lastChildInput.getAttribute('value');
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

export class FxReceiptPurposeTypeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-fxReceiptPurposeType-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-fxReceiptPurposeType'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
