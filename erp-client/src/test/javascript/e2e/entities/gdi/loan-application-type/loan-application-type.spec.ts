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
  LoanApplicationTypeComponentsPage,
  LoanApplicationTypeDeleteDialog,
  LoanApplicationTypeUpdatePage,
} from './loan-application-type.page-object';

const expect = chai.expect;

describe('LoanApplicationType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let loanApplicationTypeComponentsPage: LoanApplicationTypeComponentsPage;
  let loanApplicationTypeUpdatePage: LoanApplicationTypeUpdatePage;
  let loanApplicationTypeDeleteDialog: LoanApplicationTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load LoanApplicationTypes', async () => {
    await navBarPage.goToEntity('loan-application-type');
    loanApplicationTypeComponentsPage = new LoanApplicationTypeComponentsPage();
    await browser.wait(ec.visibilityOf(loanApplicationTypeComponentsPage.title), 5000);
    expect(await loanApplicationTypeComponentsPage.getTitle()).to.eq('Loan Application Types');
    await browser.wait(
      ec.or(ec.visibilityOf(loanApplicationTypeComponentsPage.entities), ec.visibilityOf(loanApplicationTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create LoanApplicationType page', async () => {
    await loanApplicationTypeComponentsPage.clickOnCreateButton();
    loanApplicationTypeUpdatePage = new LoanApplicationTypeUpdatePage();
    expect(await loanApplicationTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Loan Application Type');
    await loanApplicationTypeUpdatePage.cancel();
  });

  it('should create and save LoanApplicationTypes', async () => {
    const nbButtonsBeforeCreate = await loanApplicationTypeComponentsPage.countDeleteButtons();

    await loanApplicationTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      loanApplicationTypeUpdatePage.setLoanApplicationTypeCodeInput('loanApplicationTypeCode'),
      loanApplicationTypeUpdatePage.setLoanApplicationTypeInput('loanApplicationType'),
      loanApplicationTypeUpdatePage.setLoanApplicationDetailsInput('loanApplicationDetails'),
    ]);

    await loanApplicationTypeUpdatePage.save();
    expect(await loanApplicationTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await loanApplicationTypeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last LoanApplicationType', async () => {
    const nbButtonsBeforeDelete = await loanApplicationTypeComponentsPage.countDeleteButtons();
    await loanApplicationTypeComponentsPage.clickOnLastDeleteButton();

    loanApplicationTypeDeleteDialog = new LoanApplicationTypeDeleteDialog();
    expect(await loanApplicationTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Loan Application Type?');
    await loanApplicationTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(loanApplicationTypeComponentsPage.title), 5000);

    expect(await loanApplicationTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
