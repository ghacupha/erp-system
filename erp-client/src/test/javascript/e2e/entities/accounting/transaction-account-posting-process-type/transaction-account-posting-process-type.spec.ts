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

import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  TransactionAccountPostingProcessTypeComponentsPage,
  /* TransactionAccountPostingProcessTypeDeleteDialog, */
  TransactionAccountPostingProcessTypeUpdatePage,
} from './transaction-account-posting-process-type.page-object';

const expect = chai.expect;

describe('TransactionAccountPostingProcessType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let transactionAccountPostingProcessTypeComponentsPage: TransactionAccountPostingProcessTypeComponentsPage;
  let transactionAccountPostingProcessTypeUpdatePage: TransactionAccountPostingProcessTypeUpdatePage;
  /* let transactionAccountPostingProcessTypeDeleteDialog: TransactionAccountPostingProcessTypeDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load TransactionAccountPostingProcessTypes', async () => {
    await navBarPage.goToEntity('transaction-account-posting-process-type');
    transactionAccountPostingProcessTypeComponentsPage = new TransactionAccountPostingProcessTypeComponentsPage();
    await browser.wait(ec.visibilityOf(transactionAccountPostingProcessTypeComponentsPage.title), 5000);
    expect(await transactionAccountPostingProcessTypeComponentsPage.getTitle()).to.eq('Transaction Account Posting Process Types');
    await browser.wait(
      ec.or(
        ec.visibilityOf(transactionAccountPostingProcessTypeComponentsPage.entities),
        ec.visibilityOf(transactionAccountPostingProcessTypeComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create TransactionAccountPostingProcessType page', async () => {
    await transactionAccountPostingProcessTypeComponentsPage.clickOnCreateButton();
    transactionAccountPostingProcessTypeUpdatePage = new TransactionAccountPostingProcessTypeUpdatePage();
    expect(await transactionAccountPostingProcessTypeUpdatePage.getPageTitle()).to.eq(
      'Create or edit a Transaction Account Posting Process Type'
    );
    await transactionAccountPostingProcessTypeUpdatePage.cancel();
  });

  /* it('should create and save TransactionAccountPostingProcessTypes', async () => {
        const nbButtonsBeforeCreate = await transactionAccountPostingProcessTypeComponentsPage.countDeleteButtons();

        await transactionAccountPostingProcessTypeComponentsPage.clickOnCreateButton();

        await promise.all([
            transactionAccountPostingProcessTypeUpdatePage.setNameInput('name'),
            transactionAccountPostingProcessTypeUpdatePage.debitAccountTypeSelectLastOption(),
            transactionAccountPostingProcessTypeUpdatePage.creditAccountTypeSelectLastOption(),
            // transactionAccountPostingProcessTypeUpdatePage.placeholderSelectLastOption(),
        ]);

        await transactionAccountPostingProcessTypeUpdatePage.save();
        expect(await transactionAccountPostingProcessTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await transactionAccountPostingProcessTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last TransactionAccountPostingProcessType', async () => {
        const nbButtonsBeforeDelete = await transactionAccountPostingProcessTypeComponentsPage.countDeleteButtons();
        await transactionAccountPostingProcessTypeComponentsPage.clickOnLastDeleteButton();

        transactionAccountPostingProcessTypeDeleteDialog = new TransactionAccountPostingProcessTypeDeleteDialog();
        expect(await transactionAccountPostingProcessTypeDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Transaction Account Posting Process Type?');
        await transactionAccountPostingProcessTypeDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(transactionAccountPostingProcessTypeComponentsPage.title), 5000);

        expect(await transactionAccountPostingProcessTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
