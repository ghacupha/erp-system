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

export class SnaSectorCodeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-sna-sector-code div table .btn-danger'));
  title = element.all(by.css('jhi-sna-sector-code div h2#page-heading span')).first();
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

export class SnaSectorCodeUpdatePage {
  pageTitle = element(by.id('jhi-sna-sector-code-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  sectorTypeCodeInput = element(by.id('field_sectorTypeCode'));
  mainSectorCodeInput = element(by.id('field_mainSectorCode'));
  mainSectorTypeNameInput = element(by.id('field_mainSectorTypeName'));
  subSectorCodeInput = element(by.id('field_subSectorCode'));
  subSectorNameInput = element(by.id('field_subSectorName'));
  subSubSectorCodeInput = element(by.id('field_subSubSectorCode'));
  subSubSectorNameInput = element(by.id('field_subSubSectorName'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setSectorTypeCodeInput(sectorTypeCode: string): Promise<void> {
    await this.sectorTypeCodeInput.sendKeys(sectorTypeCode);
  }

  async getSectorTypeCodeInput(): Promise<string> {
    return await this.sectorTypeCodeInput.getAttribute('value');
  }

  async setMainSectorCodeInput(mainSectorCode: string): Promise<void> {
    await this.mainSectorCodeInput.sendKeys(mainSectorCode);
  }

  async getMainSectorCodeInput(): Promise<string> {
    return await this.mainSectorCodeInput.getAttribute('value');
  }

  async setMainSectorTypeNameInput(mainSectorTypeName: string): Promise<void> {
    await this.mainSectorTypeNameInput.sendKeys(mainSectorTypeName);
  }

  async getMainSectorTypeNameInput(): Promise<string> {
    return await this.mainSectorTypeNameInput.getAttribute('value');
  }

  async setSubSectorCodeInput(subSectorCode: string): Promise<void> {
    await this.subSectorCodeInput.sendKeys(subSectorCode);
  }

  async getSubSectorCodeInput(): Promise<string> {
    return await this.subSectorCodeInput.getAttribute('value');
  }

  async setSubSectorNameInput(subSectorName: string): Promise<void> {
    await this.subSectorNameInput.sendKeys(subSectorName);
  }

  async getSubSectorNameInput(): Promise<string> {
    return await this.subSectorNameInput.getAttribute('value');
  }

  async setSubSubSectorCodeInput(subSubSectorCode: string): Promise<void> {
    await this.subSubSectorCodeInput.sendKeys(subSubSectorCode);
  }

  async getSubSubSectorCodeInput(): Promise<string> {
    return await this.subSubSectorCodeInput.getAttribute('value');
  }

  async setSubSubSectorNameInput(subSubSectorName: string): Promise<void> {
    await this.subSubSectorNameInput.sendKeys(subSubSectorName);
  }

  async getSubSubSectorNameInput(): Promise<string> {
    return await this.subSubSectorNameInput.getAttribute('value');
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

export class SnaSectorCodeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-snaSectorCode-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-snaSectorCode'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
