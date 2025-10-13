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

export class WorkProjectRegisterComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-work-project-register div table .btn-danger'));
  title = element.all(by.css('jhi-work-project-register div h2#page-heading span')).first();
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

export class WorkProjectRegisterUpdatePage {
  pageTitle = element(by.id('jhi-work-project-register-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  catalogueNumberInput = element(by.id('field_catalogueNumber'));
  projectTitleInput = element(by.id('field_projectTitle'));
  descriptionInput = element(by.id('field_description'));
  detailsInput = element(by.id('file_details'));
  totalProjectCostInput = element(by.id('field_totalProjectCost'));
  additionalNotesInput = element(by.id('file_additionalNotes'));

  dealersSelect = element(by.id('field_dealers'));
  settlementCurrencySelect = element(by.id('field_settlementCurrency'));
  placeholderSelect = element(by.id('field_placeholder'));
  businessDocumentSelect = element(by.id('field_businessDocument'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setCatalogueNumberInput(catalogueNumber: string): Promise<void> {
    await this.catalogueNumberInput.sendKeys(catalogueNumber);
  }

  async getCatalogueNumberInput(): Promise<string> {
    return await this.catalogueNumberInput.getAttribute('value');
  }

  async setProjectTitleInput(projectTitle: string): Promise<void> {
    await this.projectTitleInput.sendKeys(projectTitle);
  }

  async getProjectTitleInput(): Promise<string> {
    return await this.projectTitleInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setDetailsInput(details: string): Promise<void> {
    await this.detailsInput.sendKeys(details);
  }

  async getDetailsInput(): Promise<string> {
    return await this.detailsInput.getAttribute('value');
  }

  async setTotalProjectCostInput(totalProjectCost: string): Promise<void> {
    await this.totalProjectCostInput.sendKeys(totalProjectCost);
  }

  async getTotalProjectCostInput(): Promise<string> {
    return await this.totalProjectCostInput.getAttribute('value');
  }

  async setAdditionalNotesInput(additionalNotes: string): Promise<void> {
    await this.additionalNotesInput.sendKeys(additionalNotes);
  }

  async getAdditionalNotesInput(): Promise<string> {
    return await this.additionalNotesInput.getAttribute('value');
  }

  async dealersSelectLastOption(): Promise<void> {
    await this.dealersSelect.all(by.tagName('option')).last().click();
  }

  async dealersSelectOption(option: string): Promise<void> {
    await this.dealersSelect.sendKeys(option);
  }

  getDealersSelect(): ElementFinder {
    return this.dealersSelect;
  }

  async getDealersSelectedOption(): Promise<string> {
    return await this.dealersSelect.element(by.css('option:checked')).getText();
  }

  async settlementCurrencySelectLastOption(): Promise<void> {
    await this.settlementCurrencySelect.all(by.tagName('option')).last().click();
  }

  async settlementCurrencySelectOption(option: string): Promise<void> {
    await this.settlementCurrencySelect.sendKeys(option);
  }

  getSettlementCurrencySelect(): ElementFinder {
    return this.settlementCurrencySelect;
  }

  async getSettlementCurrencySelectedOption(): Promise<string> {
    return await this.settlementCurrencySelect.element(by.css('option:checked')).getText();
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

  async businessDocumentSelectLastOption(): Promise<void> {
    await this.businessDocumentSelect.all(by.tagName('option')).last().click();
  }

  async businessDocumentSelectOption(option: string): Promise<void> {
    await this.businessDocumentSelect.sendKeys(option);
  }

  getBusinessDocumentSelect(): ElementFinder {
    return this.businessDocumentSelect;
  }

  async getBusinessDocumentSelectedOption(): Promise<string> {
    return await this.businessDocumentSelect.element(by.css('option:checked')).getText();
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

export class WorkProjectRegisterDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-workProjectRegister-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-workProjectRegister'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
