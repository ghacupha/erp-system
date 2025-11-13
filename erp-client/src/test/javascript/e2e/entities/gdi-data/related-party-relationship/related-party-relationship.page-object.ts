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

export class RelatedPartyRelationshipComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-related-party-relationship div table .btn-danger'));
  title = element.all(by.css('jhi-related-party-relationship div h2#page-heading span')).first();
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

export class RelatedPartyRelationshipUpdatePage {
  pageTitle = element(by.id('jhi-related-party-relationship-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  reportingDateInput = element(by.id('field_reportingDate'));
  customerIdInput = element(by.id('field_customerId'));
  relatedPartyIdInput = element(by.id('field_relatedPartyId'));

  bankCodeSelect = element(by.id('field_bankCode'));
  branchIdSelect = element(by.id('field_branchId'));
  relationshipTypeSelect = element(by.id('field_relationshipType'));

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

  async setCustomerIdInput(customerId: string): Promise<void> {
    await this.customerIdInput.sendKeys(customerId);
  }

  async getCustomerIdInput(): Promise<string> {
    return await this.customerIdInput.getAttribute('value');
  }

  async setRelatedPartyIdInput(relatedPartyId: string): Promise<void> {
    await this.relatedPartyIdInput.sendKeys(relatedPartyId);
  }

  async getRelatedPartyIdInput(): Promise<string> {
    return await this.relatedPartyIdInput.getAttribute('value');
  }

  async bankCodeSelectLastOption(): Promise<void> {
    await this.bankCodeSelect.all(by.tagName('option')).last().click();
  }

  async bankCodeSelectOption(option: string): Promise<void> {
    await this.bankCodeSelect.sendKeys(option);
  }

  getBankCodeSelect(): ElementFinder {
    return this.bankCodeSelect;
  }

  async getBankCodeSelectedOption(): Promise<string> {
    return await this.bankCodeSelect.element(by.css('option:checked')).getText();
  }

  async branchIdSelectLastOption(): Promise<void> {
    await this.branchIdSelect.all(by.tagName('option')).last().click();
  }

  async branchIdSelectOption(option: string): Promise<void> {
    await this.branchIdSelect.sendKeys(option);
  }

  getBranchIdSelect(): ElementFinder {
    return this.branchIdSelect;
  }

  async getBranchIdSelectedOption(): Promise<string> {
    return await this.branchIdSelect.element(by.css('option:checked')).getText();
  }

  async relationshipTypeSelectLastOption(): Promise<void> {
    await this.relationshipTypeSelect.all(by.tagName('option')).last().click();
  }

  async relationshipTypeSelectOption(option: string): Promise<void> {
    await this.relationshipTypeSelect.sendKeys(option);
  }

  getRelationshipTypeSelect(): ElementFinder {
    return this.relationshipTypeSelect;
  }

  async getRelationshipTypeSelectedOption(): Promise<string> {
    return await this.relationshipTypeSelect.element(by.css('option:checked')).getText();
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

export class RelatedPartyRelationshipDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-relatedPartyRelationship-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-relatedPartyRelationship'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
