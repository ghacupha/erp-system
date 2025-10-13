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
  LoanDeclineReasonComponentsPage,
  LoanDeclineReasonDeleteDialog,
  LoanDeclineReasonUpdatePage,
} from './loan-decline-reason.page-object';

const expect = chai.expect;

describe('LoanDeclineReason e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let loanDeclineReasonComponentsPage: LoanDeclineReasonComponentsPage;
  let loanDeclineReasonUpdatePage: LoanDeclineReasonUpdatePage;
  let loanDeclineReasonDeleteDialog: LoanDeclineReasonDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load LoanDeclineReasons', async () => {
    await navBarPage.goToEntity('loan-decline-reason');
    loanDeclineReasonComponentsPage = new LoanDeclineReasonComponentsPage();
    await browser.wait(ec.visibilityOf(loanDeclineReasonComponentsPage.title), 5000);
    expect(await loanDeclineReasonComponentsPage.getTitle()).to.eq('Loan Decline Reasons');
    await browser.wait(
      ec.or(ec.visibilityOf(loanDeclineReasonComponentsPage.entities), ec.visibilityOf(loanDeclineReasonComponentsPage.noResult)),
      1000
    );
  });

  it('should load create LoanDeclineReason page', async () => {
    await loanDeclineReasonComponentsPage.clickOnCreateButton();
    loanDeclineReasonUpdatePage = new LoanDeclineReasonUpdatePage();
    expect(await loanDeclineReasonUpdatePage.getPageTitle()).to.eq('Create or edit a Loan Decline Reason');
    await loanDeclineReasonUpdatePage.cancel();
  });

  it('should create and save LoanDeclineReasons', async () => {
    const nbButtonsBeforeCreate = await loanDeclineReasonComponentsPage.countDeleteButtons();

    await loanDeclineReasonComponentsPage.clickOnCreateButton();

    await promise.all([
      loanDeclineReasonUpdatePage.setLoanDeclineReasonTypeCodeInput('loanDeclineReasonTypeCode'),
      loanDeclineReasonUpdatePage.setLoanDeclineReasonTypeInput('loanDeclineReasonType'),
      loanDeclineReasonUpdatePage.setLoanDeclineReasonDetailsInput('loanDeclineReasonDetails'),
    ]);

    await loanDeclineReasonUpdatePage.save();
    expect(await loanDeclineReasonUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await loanDeclineReasonComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last LoanDeclineReason', async () => {
    const nbButtonsBeforeDelete = await loanDeclineReasonComponentsPage.countDeleteButtons();
    await loanDeclineReasonComponentsPage.clickOnLastDeleteButton();

    loanDeclineReasonDeleteDialog = new LoanDeclineReasonDeleteDialog();
    expect(await loanDeclineReasonDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Loan Decline Reason?');
    await loanDeclineReasonDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(loanDeclineReasonComponentsPage.title), 5000);

    expect(await loanDeclineReasonComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
