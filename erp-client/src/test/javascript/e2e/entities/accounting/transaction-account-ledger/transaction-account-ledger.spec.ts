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
  TransactionAccountLedgerComponentsPage,
  TransactionAccountLedgerDeleteDialog,
  TransactionAccountLedgerUpdatePage,
} from './transaction-account-ledger.page-object';

const expect = chai.expect;

describe('TransactionAccountLedger e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let transactionAccountLedgerComponentsPage: TransactionAccountLedgerComponentsPage;
  let transactionAccountLedgerUpdatePage: TransactionAccountLedgerUpdatePage;
  let transactionAccountLedgerDeleteDialog: TransactionAccountLedgerDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load TransactionAccountLedgers', async () => {
    await navBarPage.goToEntity('transaction-account-ledger');
    transactionAccountLedgerComponentsPage = new TransactionAccountLedgerComponentsPage();
    await browser.wait(ec.visibilityOf(transactionAccountLedgerComponentsPage.title), 5000);
    expect(await transactionAccountLedgerComponentsPage.getTitle()).to.eq('Transaction Account Ledgers');
    await browser.wait(
      ec.or(
        ec.visibilityOf(transactionAccountLedgerComponentsPage.entities),
        ec.visibilityOf(transactionAccountLedgerComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create TransactionAccountLedger page', async () => {
    await transactionAccountLedgerComponentsPage.clickOnCreateButton();
    transactionAccountLedgerUpdatePage = new TransactionAccountLedgerUpdatePage();
    expect(await transactionAccountLedgerUpdatePage.getPageTitle()).to.eq('Create or edit a Transaction Account Ledger');
    await transactionAccountLedgerUpdatePage.cancel();
  });

  it('should create and save TransactionAccountLedgers', async () => {
    const nbButtonsBeforeCreate = await transactionAccountLedgerComponentsPage.countDeleteButtons();

    await transactionAccountLedgerComponentsPage.clickOnCreateButton();

    await promise.all([
      transactionAccountLedgerUpdatePage.setLedgerCodeInput('ledgerCode'),
      transactionAccountLedgerUpdatePage.setLedgerNameInput('ledgerName'),
      // transactionAccountLedgerUpdatePage.placeholderSelectLastOption(),
    ]);

    await transactionAccountLedgerUpdatePage.save();
    expect(await transactionAccountLedgerUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await transactionAccountLedgerComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last TransactionAccountLedger', async () => {
    const nbButtonsBeforeDelete = await transactionAccountLedgerComponentsPage.countDeleteButtons();
    await transactionAccountLedgerComponentsPage.clickOnLastDeleteButton();

    transactionAccountLedgerDeleteDialog = new TransactionAccountLedgerDeleteDialog();
    expect(await transactionAccountLedgerDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Transaction Account Ledger?'
    );
    await transactionAccountLedgerDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(transactionAccountLedgerComponentsPage.title), 5000);

    expect(await transactionAccountLedgerComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
