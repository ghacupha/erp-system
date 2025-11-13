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

export class SourcesOfFundsTypeCodeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-sources-of-funds-type-code div table .btn-danger'));
  title = element.all(by.css('jhi-sources-of-funds-type-code div h2#page-heading span')).first();
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

export class SourcesOfFundsTypeCodeUpdatePage {
  pageTitle = element(by.id('jhi-sources-of-funds-type-code-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  sourceOfFundsTypeCodeInput = element(by.id('field_sourceOfFundsTypeCode'));
  sourceOfFundsTypeInput = element(by.id('field_sourceOfFundsType'));
  sourceOfFundsTypeDetailsInput = element(by.id('field_sourceOfFundsTypeDetails'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setSourceOfFundsTypeCodeInput(sourceOfFundsTypeCode: string): Promise<void> {
    await this.sourceOfFundsTypeCodeInput.sendKeys(sourceOfFundsTypeCode);
  }

  async getSourceOfFundsTypeCodeInput(): Promise<string> {
    return await this.sourceOfFundsTypeCodeInput.getAttribute('value');
  }

  async setSourceOfFundsTypeInput(sourceOfFundsType: string): Promise<void> {
    await this.sourceOfFundsTypeInput.sendKeys(sourceOfFundsType);
  }

  async getSourceOfFundsTypeInput(): Promise<string> {
    return await this.sourceOfFundsTypeInput.getAttribute('value');
  }

  async setSourceOfFundsTypeDetailsInput(sourceOfFundsTypeDetails: string): Promise<void> {
    await this.sourceOfFundsTypeDetailsInput.sendKeys(sourceOfFundsTypeDetails);
  }

  async getSourceOfFundsTypeDetailsInput(): Promise<string> {
    return await this.sourceOfFundsTypeDetailsInput.getAttribute('value');
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

export class SourcesOfFundsTypeCodeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-sourcesOfFundsTypeCode-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-sourcesOfFundsTypeCode'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
