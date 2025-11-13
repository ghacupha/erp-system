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

export class IFRS16LeaseContractComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-ifrs-16-lease-contract div table .btn-danger'));
  title = element.all(by.css('jhi-ifrs-16-lease-contract div h2#page-heading span')).first();
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

export class IFRS16LeaseContractUpdatePage {
  pageTitle = element(by.id('jhi-ifrs-16-lease-contract-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  bookingIdInput = element(by.id('field_bookingId'));
  leaseTitleInput = element(by.id('field_leaseTitle'));
  shortTitleInput = element(by.id('field_shortTitle'));
  descriptionInput = element(by.id('field_description'));
  inceptionDateInput = element(by.id('field_inceptionDate'));
  commencementDateInput = element(by.id('field_commencementDate'));
  serialNumberInput = element(by.id('field_serialNumber'));

  superintendentServiceOutletSelect = element(by.id('field_superintendentServiceOutlet'));
  mainDealerSelect = element(by.id('field_mainDealer'));
  firstReportingPeriodSelect = element(by.id('field_firstReportingPeriod'));
  lastReportingPeriodSelect = element(by.id('field_lastReportingPeriod'));
  leaseContractDocumentSelect = element(by.id('field_leaseContractDocument'));
  leaseContractCalculationsSelect = element(by.id('field_leaseContractCalculations'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setBookingIdInput(bookingId: string): Promise<void> {
    await this.bookingIdInput.sendKeys(bookingId);
  }

  async getBookingIdInput(): Promise<string> {
    return await this.bookingIdInput.getAttribute('value');
  }

  async setLeaseTitleInput(leaseTitle: string): Promise<void> {
    await this.leaseTitleInput.sendKeys(leaseTitle);
  }

  async getLeaseTitleInput(): Promise<string> {
    return await this.leaseTitleInput.getAttribute('value');
  }

  async setShortTitleInput(shortTitle: string): Promise<void> {
    await this.shortTitleInput.sendKeys(shortTitle);
  }

  async getShortTitleInput(): Promise<string> {
    return await this.shortTitleInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setInceptionDateInput(inceptionDate: string): Promise<void> {
    await this.inceptionDateInput.sendKeys(inceptionDate);
  }

  async getInceptionDateInput(): Promise<string> {
    return await this.inceptionDateInput.getAttribute('value');
  }

  async setCommencementDateInput(commencementDate: string): Promise<void> {
    await this.commencementDateInput.sendKeys(commencementDate);
  }

  async getCommencementDateInput(): Promise<string> {
    return await this.commencementDateInput.getAttribute('value');
  }

  async setSerialNumberInput(serialNumber: string): Promise<void> {
    await this.serialNumberInput.sendKeys(serialNumber);
  }

  async getSerialNumberInput(): Promise<string> {
    return await this.serialNumberInput.getAttribute('value');
  }

  async superintendentServiceOutletSelectLastOption(): Promise<void> {
    await this.superintendentServiceOutletSelect.all(by.tagName('option')).last().click();
  }

  async superintendentServiceOutletSelectOption(option: string): Promise<void> {
    await this.superintendentServiceOutletSelect.sendKeys(option);
  }

  getSuperintendentServiceOutletSelect(): ElementFinder {
    return this.superintendentServiceOutletSelect;
  }

  async getSuperintendentServiceOutletSelectedOption(): Promise<string> {
    return await this.superintendentServiceOutletSelect.element(by.css('option:checked')).getText();
  }

  async mainDealerSelectLastOption(): Promise<void> {
    await this.mainDealerSelect.all(by.tagName('option')).last().click();
  }

  async mainDealerSelectOption(option: string): Promise<void> {
    await this.mainDealerSelect.sendKeys(option);
  }

  getMainDealerSelect(): ElementFinder {
    return this.mainDealerSelect;
  }

  async getMainDealerSelectedOption(): Promise<string> {
    return await this.mainDealerSelect.element(by.css('option:checked')).getText();
  }

  async firstReportingPeriodSelectLastOption(): Promise<void> {
    await this.firstReportingPeriodSelect.all(by.tagName('option')).last().click();
  }

  async firstReportingPeriodSelectOption(option: string): Promise<void> {
    await this.firstReportingPeriodSelect.sendKeys(option);
  }

  getFirstReportingPeriodSelect(): ElementFinder {
    return this.firstReportingPeriodSelect;
  }

  async getFirstReportingPeriodSelectedOption(): Promise<string> {
    return await this.firstReportingPeriodSelect.element(by.css('option:checked')).getText();
  }

  async lastReportingPeriodSelectLastOption(): Promise<void> {
    await this.lastReportingPeriodSelect.all(by.tagName('option')).last().click();
  }

  async lastReportingPeriodSelectOption(option: string): Promise<void> {
    await this.lastReportingPeriodSelect.sendKeys(option);
  }

  getLastReportingPeriodSelect(): ElementFinder {
    return this.lastReportingPeriodSelect;
  }

  async getLastReportingPeriodSelectedOption(): Promise<string> {
    return await this.lastReportingPeriodSelect.element(by.css('option:checked')).getText();
  }

  async leaseContractDocumentSelectLastOption(): Promise<void> {
    await this.leaseContractDocumentSelect.all(by.tagName('option')).last().click();
  }

  async leaseContractDocumentSelectOption(option: string): Promise<void> {
    await this.leaseContractDocumentSelect.sendKeys(option);
  }

  getLeaseContractDocumentSelect(): ElementFinder {
    return this.leaseContractDocumentSelect;
  }

  async getLeaseContractDocumentSelectedOption(): Promise<string> {
    return await this.leaseContractDocumentSelect.element(by.css('option:checked')).getText();
  }

  async leaseContractCalculationsSelectLastOption(): Promise<void> {
    await this.leaseContractCalculationsSelect.all(by.tagName('option')).last().click();
  }

  async leaseContractCalculationsSelectOption(option: string): Promise<void> {
    await this.leaseContractCalculationsSelect.sendKeys(option);
  }

  getLeaseContractCalculationsSelect(): ElementFinder {
    return this.leaseContractCalculationsSelect;
  }

  async getLeaseContractCalculationsSelectedOption(): Promise<string> {
    return await this.leaseContractCalculationsSelect.element(by.css('option:checked')).getText();
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

export class IFRS16LeaseContractDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-iFRS16LeaseContract-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-iFRS16LeaseContract'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
