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
  AccountBalanceComponentsPage,
  /* AccountBalanceDeleteDialog, */
  AccountBalanceUpdatePage,
} from './account-balance.page-object';

const expect = chai.expect;

describe('AccountBalance e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let accountBalanceComponentsPage: AccountBalanceComponentsPage;
  let accountBalanceUpdatePage: AccountBalanceUpdatePage;
  /* let accountBalanceDeleteDialog: AccountBalanceDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load AccountBalances', async () => {
    await navBarPage.goToEntity('account-balance');
    accountBalanceComponentsPage = new AccountBalanceComponentsPage();
    await browser.wait(ec.visibilityOf(accountBalanceComponentsPage.title), 5000);
    expect(await accountBalanceComponentsPage.getTitle()).to.eq('Account Balances');
    await browser.wait(
      ec.or(ec.visibilityOf(accountBalanceComponentsPage.entities), ec.visibilityOf(accountBalanceComponentsPage.noResult)),
      1000
    );
  });

  it('should load create AccountBalance page', async () => {
    await accountBalanceComponentsPage.clickOnCreateButton();
    accountBalanceUpdatePage = new AccountBalanceUpdatePage();
    expect(await accountBalanceUpdatePage.getPageTitle()).to.eq('Create or edit a Account Balance');
    await accountBalanceUpdatePage.cancel();
  });

  /* it('should create and save AccountBalances', async () => {
        const nbButtonsBeforeCreate = await accountBalanceComponentsPage.countDeleteButtons();

        await accountBalanceComponentsPage.clickOnCreateButton();

        await promise.all([
            accountBalanceUpdatePage.setReportingDateInput('2000-12-31'),
            accountBalanceUpdatePage.setCustomerIdInput('customerId'),
            accountBalanceUpdatePage.setAccountContractNumberInput('729527096613173'),
            accountBalanceUpdatePage.setAccruedInterestBalanceFCYInput('5'),
            accountBalanceUpdatePage.setAccruedInterestBalanceLCYInput('5'),
            accountBalanceUpdatePage.setAccountBalanceFCYInput('5'),
            accountBalanceUpdatePage.setAccountBalanceLCYInput('5'),
            accountBalanceUpdatePage.bankCodeSelectLastOption(),
            accountBalanceUpdatePage.branchIdSelectLastOption(),
            accountBalanceUpdatePage.currencyCodeSelectLastOption(),
        ]);

        await accountBalanceUpdatePage.save();
        expect(await accountBalanceUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await accountBalanceComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last AccountBalance', async () => {
        const nbButtonsBeforeDelete = await accountBalanceComponentsPage.countDeleteButtons();
        await accountBalanceComponentsPage.clickOnLastDeleteButton();

        accountBalanceDeleteDialog = new AccountBalanceDeleteDialog();
        expect(await accountBalanceDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Account Balance?');
        await accountBalanceDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(accountBalanceComponentsPage.title), 5000);

        expect(await accountBalanceComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
