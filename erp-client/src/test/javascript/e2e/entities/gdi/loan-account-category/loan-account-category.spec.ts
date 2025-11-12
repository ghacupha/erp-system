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
  LoanAccountCategoryComponentsPage,
  LoanAccountCategoryDeleteDialog,
  LoanAccountCategoryUpdatePage,
} from './loan-account-category.page-object';

const expect = chai.expect;

describe('LoanAccountCategory e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let loanAccountCategoryComponentsPage: LoanAccountCategoryComponentsPage;
  let loanAccountCategoryUpdatePage: LoanAccountCategoryUpdatePage;
  let loanAccountCategoryDeleteDialog: LoanAccountCategoryDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load LoanAccountCategories', async () => {
    await navBarPage.goToEntity('loan-account-category');
    loanAccountCategoryComponentsPage = new LoanAccountCategoryComponentsPage();
    await browser.wait(ec.visibilityOf(loanAccountCategoryComponentsPage.title), 5000);
    expect(await loanAccountCategoryComponentsPage.getTitle()).to.eq('Loan Account Categories');
    await browser.wait(
      ec.or(ec.visibilityOf(loanAccountCategoryComponentsPage.entities), ec.visibilityOf(loanAccountCategoryComponentsPage.noResult)),
      1000
    );
  });

  it('should load create LoanAccountCategory page', async () => {
    await loanAccountCategoryComponentsPage.clickOnCreateButton();
    loanAccountCategoryUpdatePage = new LoanAccountCategoryUpdatePage();
    expect(await loanAccountCategoryUpdatePage.getPageTitle()).to.eq('Create or edit a Loan Account Category');
    await loanAccountCategoryUpdatePage.cancel();
  });

  it('should create and save LoanAccountCategories', async () => {
    const nbButtonsBeforeCreate = await loanAccountCategoryComponentsPage.countDeleteButtons();

    await loanAccountCategoryComponentsPage.clickOnCreateButton();

    await promise.all([
      loanAccountCategoryUpdatePage.setLoanAccountMutationCodeInput('loanAccountMutationCode'),
      loanAccountCategoryUpdatePage.loanAccountMutationTypeSelectLastOption(),
      loanAccountCategoryUpdatePage.setLoanAccountMutationDetailsInput('loanAccountMutationDetails'),
      loanAccountCategoryUpdatePage.setLoanAccountMutationDescriptionInput('loanAccountMutationDescription'),
    ]);

    await loanAccountCategoryUpdatePage.save();
    expect(await loanAccountCategoryUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await loanAccountCategoryComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last LoanAccountCategory', async () => {
    const nbButtonsBeforeDelete = await loanAccountCategoryComponentsPage.countDeleteButtons();
    await loanAccountCategoryComponentsPage.clickOnLastDeleteButton();

    loanAccountCategoryDeleteDialog = new LoanAccountCategoryDeleteDialog();
    expect(await loanAccountCategoryDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Loan Account Category?');
    await loanAccountCategoryDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(loanAccountCategoryComponentsPage.title), 5000);

    expect(await loanAccountCategoryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
