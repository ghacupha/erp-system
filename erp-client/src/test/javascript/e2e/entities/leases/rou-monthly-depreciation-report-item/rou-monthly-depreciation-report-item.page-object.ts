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

export class RouMonthlyDepreciationReportItemComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-rou-monthly-depreciation-report-item div table .btn-danger'));
  title = element.all(by.css('jhi-rou-monthly-depreciation-report-item div h2#page-heading span')).first();
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

export class RouMonthlyDepreciationReportItemUpdatePage {
  pageTitle = element(by.id('jhi-rou-monthly-depreciation-report-item-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  fiscalMonthStartDateInput = element(by.id('field_fiscalMonthStartDate'));
  fiscalMonthEndDateInput = element(by.id('field_fiscalMonthEndDate'));
  totalDepreciationAmountInput = element(by.id('field_totalDepreciationAmount'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setFiscalMonthStartDateInput(fiscalMonthStartDate: string): Promise<void> {
    await this.fiscalMonthStartDateInput.sendKeys(fiscalMonthStartDate);
  }

  async getFiscalMonthStartDateInput(): Promise<string> {
    return await this.fiscalMonthStartDateInput.getAttribute('value');
  }

  async setFiscalMonthEndDateInput(fiscalMonthEndDate: string): Promise<void> {
    await this.fiscalMonthEndDateInput.sendKeys(fiscalMonthEndDate);
  }

  async getFiscalMonthEndDateInput(): Promise<string> {
    return await this.fiscalMonthEndDateInput.getAttribute('value');
  }

  async setTotalDepreciationAmountInput(totalDepreciationAmount: string): Promise<void> {
    await this.totalDepreciationAmountInput.sendKeys(totalDepreciationAmount);
  }

  async getTotalDepreciationAmountInput(): Promise<string> {
    return await this.totalDepreciationAmountInput.getAttribute('value');
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

export class RouMonthlyDepreciationReportItemDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-rouMonthlyDepreciationReportItem-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-rouMonthlyDepreciationReportItem'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
