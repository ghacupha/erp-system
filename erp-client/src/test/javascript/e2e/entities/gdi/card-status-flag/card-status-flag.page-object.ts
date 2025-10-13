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

export class CardStatusFlagComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-card-status-flag div table .btn-danger'));
  title = element.all(by.css('jhi-card-status-flag div h2#page-heading span')).first();
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

export class CardStatusFlagUpdatePage {
  pageTitle = element(by.id('jhi-card-status-flag-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  cardStatusFlagSelect = element(by.id('field_cardStatusFlag'));
  cardStatusFlagDescriptionInput = element(by.id('field_cardStatusFlagDescription'));
  cardStatusFlagDetailsInput = element(by.id('field_cardStatusFlagDetails'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setCardStatusFlagSelect(cardStatusFlag: string): Promise<void> {
    await this.cardStatusFlagSelect.sendKeys(cardStatusFlag);
  }

  async getCardStatusFlagSelect(): Promise<string> {
    return await this.cardStatusFlagSelect.element(by.css('option:checked')).getText();
  }

  async cardStatusFlagSelectLastOption(): Promise<void> {
    await this.cardStatusFlagSelect.all(by.tagName('option')).last().click();
  }

  async setCardStatusFlagDescriptionInput(cardStatusFlagDescription: string): Promise<void> {
    await this.cardStatusFlagDescriptionInput.sendKeys(cardStatusFlagDescription);
  }

  async getCardStatusFlagDescriptionInput(): Promise<string> {
    return await this.cardStatusFlagDescriptionInput.getAttribute('value');
  }

  async setCardStatusFlagDetailsInput(cardStatusFlagDetails: string): Promise<void> {
    await this.cardStatusFlagDetailsInput.sendKeys(cardStatusFlagDetails);
  }

  async getCardStatusFlagDetailsInput(): Promise<string> {
    return await this.cardStatusFlagDetailsInput.getAttribute('value');
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

export class CardStatusFlagDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-cardStatusFlag-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-cardStatusFlag'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
