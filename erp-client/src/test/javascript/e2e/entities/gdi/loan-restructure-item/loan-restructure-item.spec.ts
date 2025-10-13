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
  LoanRestructureItemComponentsPage,
  LoanRestructureItemDeleteDialog,
  LoanRestructureItemUpdatePage,
} from './loan-restructure-item.page-object';

const expect = chai.expect;

describe('LoanRestructureItem e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let loanRestructureItemComponentsPage: LoanRestructureItemComponentsPage;
  let loanRestructureItemUpdatePage: LoanRestructureItemUpdatePage;
  let loanRestructureItemDeleteDialog: LoanRestructureItemDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load LoanRestructureItems', async () => {
    await navBarPage.goToEntity('loan-restructure-item');
    loanRestructureItemComponentsPage = new LoanRestructureItemComponentsPage();
    await browser.wait(ec.visibilityOf(loanRestructureItemComponentsPage.title), 5000);
    expect(await loanRestructureItemComponentsPage.getTitle()).to.eq('Loan Restructure Items');
    await browser.wait(
      ec.or(ec.visibilityOf(loanRestructureItemComponentsPage.entities), ec.visibilityOf(loanRestructureItemComponentsPage.noResult)),
      1000
    );
  });

  it('should load create LoanRestructureItem page', async () => {
    await loanRestructureItemComponentsPage.clickOnCreateButton();
    loanRestructureItemUpdatePage = new LoanRestructureItemUpdatePage();
    expect(await loanRestructureItemUpdatePage.getPageTitle()).to.eq('Create or edit a Loan Restructure Item');
    await loanRestructureItemUpdatePage.cancel();
  });

  it('should create and save LoanRestructureItems', async () => {
    const nbButtonsBeforeCreate = await loanRestructureItemComponentsPage.countDeleteButtons();

    await loanRestructureItemComponentsPage.clickOnCreateButton();

    await promise.all([
      loanRestructureItemUpdatePage.setLoanRestructureItemCodeInput('loanRestructureItemCode'),
      loanRestructureItemUpdatePage.setLoanRestructureItemTypeInput('loanRestructureItemType'),
      loanRestructureItemUpdatePage.setLoanRestructureItemDetailsInput('loanRestructureItemDetails'),
    ]);

    await loanRestructureItemUpdatePage.save();
    expect(await loanRestructureItemUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await loanRestructureItemComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last LoanRestructureItem', async () => {
    const nbButtonsBeforeDelete = await loanRestructureItemComponentsPage.countDeleteButtons();
    await loanRestructureItemComponentsPage.clickOnLastDeleteButton();

    loanRestructureItemDeleteDialog = new LoanRestructureItemDeleteDialog();
    expect(await loanRestructureItemDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Loan Restructure Item?');
    await loanRestructureItemDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(loanRestructureItemComponentsPage.title), 5000);

    expect(await loanRestructureItemComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
