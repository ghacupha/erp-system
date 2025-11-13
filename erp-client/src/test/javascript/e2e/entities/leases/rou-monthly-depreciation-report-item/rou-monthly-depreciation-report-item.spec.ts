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

import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  RouMonthlyDepreciationReportItemComponentsPage,
  RouMonthlyDepreciationReportItemDeleteDialog,
  RouMonthlyDepreciationReportItemUpdatePage,
} from './rou-monthly-depreciation-report-item.page-object';

const expect = chai.expect;

describe('RouMonthlyDepreciationReportItem e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let rouMonthlyDepreciationReportItemComponentsPage: RouMonthlyDepreciationReportItemComponentsPage;
  let rouMonthlyDepreciationReportItemUpdatePage: RouMonthlyDepreciationReportItemUpdatePage;
  let rouMonthlyDepreciationReportItemDeleteDialog: RouMonthlyDepreciationReportItemDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load RouMonthlyDepreciationReportItems', async () => {
    await navBarPage.goToEntity('rou-monthly-depreciation-report-item');
    rouMonthlyDepreciationReportItemComponentsPage = new RouMonthlyDepreciationReportItemComponentsPage();
    await browser.wait(ec.visibilityOf(rouMonthlyDepreciationReportItemComponentsPage.title), 5000);
    expect(await rouMonthlyDepreciationReportItemComponentsPage.getTitle()).to.eq('Rou Monthly Depreciation Report Items');
    await browser.wait(
      ec.or(
        ec.visibilityOf(rouMonthlyDepreciationReportItemComponentsPage.entities),
        ec.visibilityOf(rouMonthlyDepreciationReportItemComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create RouMonthlyDepreciationReportItem page', async () => {
    await rouMonthlyDepreciationReportItemComponentsPage.clickOnCreateButton();
    rouMonthlyDepreciationReportItemUpdatePage = new RouMonthlyDepreciationReportItemUpdatePage();
    expect(await rouMonthlyDepreciationReportItemUpdatePage.getPageTitle()).to.eq('Create or edit a Rou Monthly Depreciation Report Item');
    await rouMonthlyDepreciationReportItemUpdatePage.cancel();
  });

  it('should create and save RouMonthlyDepreciationReportItems', async () => {
    const nbButtonsBeforeCreate = await rouMonthlyDepreciationReportItemComponentsPage.countDeleteButtons();

    await rouMonthlyDepreciationReportItemComponentsPage.clickOnCreateButton();

    await promise.all([
      rouMonthlyDepreciationReportItemUpdatePage.setFiscalMonthStartDateInput('2000-12-31'),
      rouMonthlyDepreciationReportItemUpdatePage.setFiscalMonthEndDateInput('2000-12-31'),
      rouMonthlyDepreciationReportItemUpdatePage.setTotalDepreciationAmountInput('5'),
    ]);

    await rouMonthlyDepreciationReportItemUpdatePage.save();
    expect(await rouMonthlyDepreciationReportItemUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await rouMonthlyDepreciationReportItemComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last RouMonthlyDepreciationReportItem', async () => {
    const nbButtonsBeforeDelete = await rouMonthlyDepreciationReportItemComponentsPage.countDeleteButtons();
    await rouMonthlyDepreciationReportItemComponentsPage.clickOnLastDeleteButton();

    rouMonthlyDepreciationReportItemDeleteDialog = new RouMonthlyDepreciationReportItemDeleteDialog();
    expect(await rouMonthlyDepreciationReportItemDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Rou Monthly Depreciation Report Item?'
    );
    await rouMonthlyDepreciationReportItemDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(rouMonthlyDepreciationReportItemComponentsPage.title), 5000);

    expect(await rouMonthlyDepreciationReportItemComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
