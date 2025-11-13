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
  LoanRestructureFlagComponentsPage,
  LoanRestructureFlagDeleteDialog,
  LoanRestructureFlagUpdatePage,
} from './loan-restructure-flag.page-object';

const expect = chai.expect;

describe('LoanRestructureFlag e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let loanRestructureFlagComponentsPage: LoanRestructureFlagComponentsPage;
  let loanRestructureFlagUpdatePage: LoanRestructureFlagUpdatePage;
  let loanRestructureFlagDeleteDialog: LoanRestructureFlagDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load LoanRestructureFlags', async () => {
    await navBarPage.goToEntity('loan-restructure-flag');
    loanRestructureFlagComponentsPage = new LoanRestructureFlagComponentsPage();
    await browser.wait(ec.visibilityOf(loanRestructureFlagComponentsPage.title), 5000);
    expect(await loanRestructureFlagComponentsPage.getTitle()).to.eq('Loan Restructure Flags');
    await browser.wait(
      ec.or(ec.visibilityOf(loanRestructureFlagComponentsPage.entities), ec.visibilityOf(loanRestructureFlagComponentsPage.noResult)),
      1000
    );
  });

  it('should load create LoanRestructureFlag page', async () => {
    await loanRestructureFlagComponentsPage.clickOnCreateButton();
    loanRestructureFlagUpdatePage = new LoanRestructureFlagUpdatePage();
    expect(await loanRestructureFlagUpdatePage.getPageTitle()).to.eq('Create or edit a Loan Restructure Flag');
    await loanRestructureFlagUpdatePage.cancel();
  });

  it('should create and save LoanRestructureFlags', async () => {
    const nbButtonsBeforeCreate = await loanRestructureFlagComponentsPage.countDeleteButtons();

    await loanRestructureFlagComponentsPage.clickOnCreateButton();

    await promise.all([
      loanRestructureFlagUpdatePage.loanRestructureFlagCodeSelectLastOption(),
      loanRestructureFlagUpdatePage.setLoanRestructureFlagTypeInput('loanRestructureFlagType'),
      loanRestructureFlagUpdatePage.setLoanRestructureFlagDetailsInput('loanRestructureFlagDetails'),
    ]);

    await loanRestructureFlagUpdatePage.save();
    expect(await loanRestructureFlagUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await loanRestructureFlagComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last LoanRestructureFlag', async () => {
    const nbButtonsBeforeDelete = await loanRestructureFlagComponentsPage.countDeleteButtons();
    await loanRestructureFlagComponentsPage.clickOnLastDeleteButton();

    loanRestructureFlagDeleteDialog = new LoanRestructureFlagDeleteDialog();
    expect(await loanRestructureFlagDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Loan Restructure Flag?');
    await loanRestructureFlagDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(loanRestructureFlagComponentsPage.title), 5000);

    expect(await loanRestructureFlagComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
