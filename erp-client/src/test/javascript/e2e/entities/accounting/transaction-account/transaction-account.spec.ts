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

import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  TransactionAccountComponentsPage,
  /* TransactionAccountDeleteDialog, */
  TransactionAccountUpdatePage,
} from './transaction-account.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('TransactionAccount e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let transactionAccountComponentsPage: TransactionAccountComponentsPage;
  let transactionAccountUpdatePage: TransactionAccountUpdatePage;
  /* let transactionAccountDeleteDialog: TransactionAccountDeleteDialog; */
  const fileNameToUpload = 'logo-jhipster.png';
  const fileToUpload = '../../../../../../../src/main/webapp/content/images/' + fileNameToUpload;
  const absolutePath = path.resolve(__dirname, fileToUpload);
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load TransactionAccounts', async () => {
    await navBarPage.goToEntity('transaction-account');
    transactionAccountComponentsPage = new TransactionAccountComponentsPage();
    await browser.wait(ec.visibilityOf(transactionAccountComponentsPage.title), 5000);
    expect(await transactionAccountComponentsPage.getTitle()).to.eq('Transaction Accounts');
    await browser.wait(
      ec.or(ec.visibilityOf(transactionAccountComponentsPage.entities), ec.visibilityOf(transactionAccountComponentsPage.noResult)),
      1000
    );
  });

  it('should load create TransactionAccount page', async () => {
    await transactionAccountComponentsPage.clickOnCreateButton();
    transactionAccountUpdatePage = new TransactionAccountUpdatePage();
    expect(await transactionAccountUpdatePage.getPageTitle()).to.eq('Create or edit a Transaction Account');
    await transactionAccountUpdatePage.cancel();
  });

  /* it('should create and save TransactionAccounts', async () => {
        const nbButtonsBeforeCreate = await transactionAccountComponentsPage.countDeleteButtons();

        await transactionAccountComponentsPage.clickOnCreateButton();

        await promise.all([
            transactionAccountUpdatePage.setAccountNumberInput('accountNumber'),
            transactionAccountUpdatePage.setAccountNameInput('accountName'),
            transactionAccountUpdatePage.setNotesInput(absolutePath),
            transactionAccountUpdatePage.accountTypeSelectLastOption(),
            transactionAccountUpdatePage.accountSubTypeSelectLastOption(),
            transactionAccountUpdatePage.getDummyAccountInput().click(),
            transactionAccountUpdatePage.accountLedgerSelectLastOption(),
            transactionAccountUpdatePage.accountCategorySelectLastOption(),
            // transactionAccountUpdatePage.placeholderSelectLastOption(),
            transactionAccountUpdatePage.serviceOutletSelectLastOption(),
            transactionAccountUpdatePage.settlementCurrencySelectLastOption(),
            transactionAccountUpdatePage.institutionSelectLastOption(),
        ]);

        await transactionAccountUpdatePage.save();
        expect(await transactionAccountUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await transactionAccountComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last TransactionAccount', async () => {
        const nbButtonsBeforeDelete = await transactionAccountComponentsPage.countDeleteButtons();
        await transactionAccountComponentsPage.clickOnLastDeleteButton();

        transactionAccountDeleteDialog = new TransactionAccountDeleteDialog();
        expect(await transactionAccountDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Transaction Account?');
        await transactionAccountDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(transactionAccountComponentsPage.title), 5000);

        expect(await transactionAccountComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
