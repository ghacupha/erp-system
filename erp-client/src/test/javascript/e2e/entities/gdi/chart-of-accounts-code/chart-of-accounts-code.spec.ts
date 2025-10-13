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

import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  ChartOfAccountsCodeComponentsPage,
  ChartOfAccountsCodeDeleteDialog,
  ChartOfAccountsCodeUpdatePage,
} from './chart-of-accounts-code.page-object';

const expect = chai.expect;

describe('ChartOfAccountsCode e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let chartOfAccountsCodeComponentsPage: ChartOfAccountsCodeComponentsPage;
  let chartOfAccountsCodeUpdatePage: ChartOfAccountsCodeUpdatePage;
  let chartOfAccountsCodeDeleteDialog: ChartOfAccountsCodeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ChartOfAccountsCodes', async () => {
    await navBarPage.goToEntity('chart-of-accounts-code');
    chartOfAccountsCodeComponentsPage = new ChartOfAccountsCodeComponentsPage();
    await browser.wait(ec.visibilityOf(chartOfAccountsCodeComponentsPage.title), 5000);
    expect(await chartOfAccountsCodeComponentsPage.getTitle()).to.eq('Chart Of Accounts Codes');
    await browser.wait(
      ec.or(ec.visibilityOf(chartOfAccountsCodeComponentsPage.entities), ec.visibilityOf(chartOfAccountsCodeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create ChartOfAccountsCode page', async () => {
    await chartOfAccountsCodeComponentsPage.clickOnCreateButton();
    chartOfAccountsCodeUpdatePage = new ChartOfAccountsCodeUpdatePage();
    expect(await chartOfAccountsCodeUpdatePage.getPageTitle()).to.eq('Create or edit a Chart Of Accounts Code');
    await chartOfAccountsCodeUpdatePage.cancel();
  });

  it('should create and save ChartOfAccountsCodes', async () => {
    const nbButtonsBeforeCreate = await chartOfAccountsCodeComponentsPage.countDeleteButtons();

    await chartOfAccountsCodeComponentsPage.clickOnCreateButton();

    await promise.all([
      chartOfAccountsCodeUpdatePage.setChartOfAccountsCodeInput('chartOfAccountsCode'),
      chartOfAccountsCodeUpdatePage.setChartOfAccountsClassInput('chartOfAccountsClass'),
      chartOfAccountsCodeUpdatePage.setDescriptionInput('description'),
    ]);

    await chartOfAccountsCodeUpdatePage.save();
    expect(await chartOfAccountsCodeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await chartOfAccountsCodeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last ChartOfAccountsCode', async () => {
    const nbButtonsBeforeDelete = await chartOfAccountsCodeComponentsPage.countDeleteButtons();
    await chartOfAccountsCodeComponentsPage.clickOnLastDeleteButton();

    chartOfAccountsCodeDeleteDialog = new ChartOfAccountsCodeDeleteDialog();
    expect(await chartOfAccountsCodeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Chart Of Accounts Code?');
    await chartOfAccountsCodeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(chartOfAccountsCodeComponentsPage.title), 5000);

    expect(await chartOfAccountsCodeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
