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
  BankTransactionTypeComponentsPage,
  BankTransactionTypeDeleteDialog,
  BankTransactionTypeUpdatePage,
} from './bank-transaction-type.page-object';

const expect = chai.expect;

describe('BankTransactionType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let bankTransactionTypeComponentsPage: BankTransactionTypeComponentsPage;
  let bankTransactionTypeUpdatePage: BankTransactionTypeUpdatePage;
  let bankTransactionTypeDeleteDialog: BankTransactionTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load BankTransactionTypes', async () => {
    await navBarPage.goToEntity('bank-transaction-type');
    bankTransactionTypeComponentsPage = new BankTransactionTypeComponentsPage();
    await browser.wait(ec.visibilityOf(bankTransactionTypeComponentsPage.title), 5000);
    expect(await bankTransactionTypeComponentsPage.getTitle()).to.eq('Bank Transaction Types');
    await browser.wait(
      ec.or(ec.visibilityOf(bankTransactionTypeComponentsPage.entities), ec.visibilityOf(bankTransactionTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create BankTransactionType page', async () => {
    await bankTransactionTypeComponentsPage.clickOnCreateButton();
    bankTransactionTypeUpdatePage = new BankTransactionTypeUpdatePage();
    expect(await bankTransactionTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Bank Transaction Type');
    await bankTransactionTypeUpdatePage.cancel();
  });

  it('should create and save BankTransactionTypes', async () => {
    const nbButtonsBeforeCreate = await bankTransactionTypeComponentsPage.countDeleteButtons();

    await bankTransactionTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      bankTransactionTypeUpdatePage.setTransactionTypeCodeInput('transactionTypeCode'),
      bankTransactionTypeUpdatePage.setTransactionTypeDetailsInput('transactionTypeDetails'),
    ]);

    await bankTransactionTypeUpdatePage.save();
    expect(await bankTransactionTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await bankTransactionTypeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last BankTransactionType', async () => {
    const nbButtonsBeforeDelete = await bankTransactionTypeComponentsPage.countDeleteButtons();
    await bankTransactionTypeComponentsPage.clickOnLastDeleteButton();

    bankTransactionTypeDeleteDialog = new BankTransactionTypeDeleteDialog();
    expect(await bankTransactionTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Bank Transaction Type?');
    await bankTransactionTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(bankTransactionTypeComponentsPage.title), 5000);

    expect(await bankTransactionTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
