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

import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  LoanApplicationStatusComponentsPage,
  LoanApplicationStatusDeleteDialog,
  LoanApplicationStatusUpdatePage,
} from './loan-application-status.page-object';

const expect = chai.expect;

describe('LoanApplicationStatus e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let loanApplicationStatusComponentsPage: LoanApplicationStatusComponentsPage;
  let loanApplicationStatusUpdatePage: LoanApplicationStatusUpdatePage;
  let loanApplicationStatusDeleteDialog: LoanApplicationStatusDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load LoanApplicationStatuses', async () => {
    await navBarPage.goToEntity('loan-application-status');
    loanApplicationStatusComponentsPage = new LoanApplicationStatusComponentsPage();
    await browser.wait(ec.visibilityOf(loanApplicationStatusComponentsPage.title), 5000);
    expect(await loanApplicationStatusComponentsPage.getTitle()).to.eq('Loan Application Statuses');
    await browser.wait(
      ec.or(ec.visibilityOf(loanApplicationStatusComponentsPage.entities), ec.visibilityOf(loanApplicationStatusComponentsPage.noResult)),
      1000
    );
  });

  it('should load create LoanApplicationStatus page', async () => {
    await loanApplicationStatusComponentsPage.clickOnCreateButton();
    loanApplicationStatusUpdatePage = new LoanApplicationStatusUpdatePage();
    expect(await loanApplicationStatusUpdatePage.getPageTitle()).to.eq('Create or edit a Loan Application Status');
    await loanApplicationStatusUpdatePage.cancel();
  });

  it('should create and save LoanApplicationStatuses', async () => {
    const nbButtonsBeforeCreate = await loanApplicationStatusComponentsPage.countDeleteButtons();

    await loanApplicationStatusComponentsPage.clickOnCreateButton();

    await promise.all([
      loanApplicationStatusUpdatePage.setLoanApplicationStatusTypeCodeInput('loanApplicationStatusTypeCode'),
      loanApplicationStatusUpdatePage.setLoanApplicationStatusTypeInput('loanApplicationStatusType'),
      loanApplicationStatusUpdatePage.setLoanApplicationStatusDetailsInput('loanApplicationStatusDetails'),
    ]);

    await loanApplicationStatusUpdatePage.save();
    expect(await loanApplicationStatusUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await loanApplicationStatusComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last LoanApplicationStatus', async () => {
    const nbButtonsBeforeDelete = await loanApplicationStatusComponentsPage.countDeleteButtons();
    await loanApplicationStatusComponentsPage.clickOnLastDeleteButton();

    loanApplicationStatusDeleteDialog = new LoanApplicationStatusDeleteDialog();
    expect(await loanApplicationStatusDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Loan Application Status?');
    await loanApplicationStatusDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(loanApplicationStatusComponentsPage.title), 5000);

    expect(await loanApplicationStatusComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
