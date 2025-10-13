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

export class SourceRemittancePurposeTypeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-source-remittance-purpose-type div table .btn-danger'));
  title = element.all(by.css('jhi-source-remittance-purpose-type div h2#page-heading span')).first();
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

export class SourceRemittancePurposeTypeUpdatePage {
  pageTitle = element(by.id('jhi-source-remittance-purpose-type-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  sourceOrPurposeTypeCodeInput = element(by.id('field_sourceOrPurposeTypeCode'));
  sourceOrPurposeOfRemittanceFlagSelect = element(by.id('field_sourceOrPurposeOfRemittanceFlag'));
  sourceOrPurposeOfRemittanceTypeInput = element(by.id('field_sourceOrPurposeOfRemittanceType'));
  remittancePurposeTypeDetailsInput = element(by.id('field_remittancePurposeTypeDetails'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setSourceOrPurposeTypeCodeInput(sourceOrPurposeTypeCode: string): Promise<void> {
    await this.sourceOrPurposeTypeCodeInput.sendKeys(sourceOrPurposeTypeCode);
  }

  async getSourceOrPurposeTypeCodeInput(): Promise<string> {
    return await this.sourceOrPurposeTypeCodeInput.getAttribute('value');
  }

  async setSourceOrPurposeOfRemittanceFlagSelect(sourceOrPurposeOfRemittanceFlag: string): Promise<void> {
    await this.sourceOrPurposeOfRemittanceFlagSelect.sendKeys(sourceOrPurposeOfRemittanceFlag);
  }

  async getSourceOrPurposeOfRemittanceFlagSelect(): Promise<string> {
    return await this.sourceOrPurposeOfRemittanceFlagSelect.element(by.css('option:checked')).getText();
  }

  async sourceOrPurposeOfRemittanceFlagSelectLastOption(): Promise<void> {
    await this.sourceOrPurposeOfRemittanceFlagSelect.all(by.tagName('option')).last().click();
  }

  async setSourceOrPurposeOfRemittanceTypeInput(sourceOrPurposeOfRemittanceType: string): Promise<void> {
    await this.sourceOrPurposeOfRemittanceTypeInput.sendKeys(sourceOrPurposeOfRemittanceType);
  }

  async getSourceOrPurposeOfRemittanceTypeInput(): Promise<string> {
    return await this.sourceOrPurposeOfRemittanceTypeInput.getAttribute('value');
  }

  async setRemittancePurposeTypeDetailsInput(remittancePurposeTypeDetails: string): Promise<void> {
    await this.remittancePurposeTypeDetailsInput.sendKeys(remittancePurposeTypeDetails);
  }

  async getRemittancePurposeTypeDetailsInput(): Promise<string> {
    return await this.remittancePurposeTypeDetailsInput.getAttribute('value');
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

export class SourceRemittancePurposeTypeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-sourceRemittancePurposeType-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-sourceRemittancePurposeType'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
