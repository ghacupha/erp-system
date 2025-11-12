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

import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  AccountAttributeComponentsPage,
  /* AccountAttributeDeleteDialog, */
  AccountAttributeUpdatePage,
} from './account-attribute.page-object';

const expect = chai.expect;

describe('AccountAttribute e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let accountAttributeComponentsPage: AccountAttributeComponentsPage;
  let accountAttributeUpdatePage: AccountAttributeUpdatePage;
  /* let accountAttributeDeleteDialog: AccountAttributeDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load AccountAttributes', async () => {
    await navBarPage.goToEntity('account-attribute');
    accountAttributeComponentsPage = new AccountAttributeComponentsPage();
    await browser.wait(ec.visibilityOf(accountAttributeComponentsPage.title), 5000);
    expect(await accountAttributeComponentsPage.getTitle()).to.eq('Account Attributes');
    await browser.wait(
      ec.or(ec.visibilityOf(accountAttributeComponentsPage.entities), ec.visibilityOf(accountAttributeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create AccountAttribute page', async () => {
    await accountAttributeComponentsPage.clickOnCreateButton();
    accountAttributeUpdatePage = new AccountAttributeUpdatePage();
    expect(await accountAttributeUpdatePage.getPageTitle()).to.eq('Create or edit a Account Attribute');
    await accountAttributeUpdatePage.cancel();
  });

  /* it('should create and save AccountAttributes', async () => {
        const nbButtonsBeforeCreate = await accountAttributeComponentsPage.countDeleteButtons();

        await accountAttributeComponentsPage.clickOnCreateButton();

        await promise.all([
            accountAttributeUpdatePage.setReportingDateInput('2000-12-31'),
            accountAttributeUpdatePage.setCustomerNumberInput('customerNumber'),
            accountAttributeUpdatePage.setAccountContractNumberInput('accountContractNumber'),
            accountAttributeUpdatePage.setAccountNameInput('accountName'),
            accountAttributeUpdatePage.setAccountOpeningDateInput('2000-12-31'),
            accountAttributeUpdatePage.setAccountClosingDateInput('2000-12-31'),
            accountAttributeUpdatePage.setDebitInterestRateInput('5'),
            accountAttributeUpdatePage.setCreditInterestRateInput('5'),
            accountAttributeUpdatePage.setSanctionedAccountLimitFcyInput('5'),
            accountAttributeUpdatePage.setSanctionedAccountLimitLcyInput('5'),
            accountAttributeUpdatePage.setAccountStatusChangeDateInput('2000-12-31'),
            accountAttributeUpdatePage.setExpiryDateInput('2000-12-31'),
            accountAttributeUpdatePage.bankCodeSelectLastOption(),
            accountAttributeUpdatePage.branchCodeSelectLastOption(),
            accountAttributeUpdatePage.accountOwnershipTypeSelectLastOption(),
        ]);

        await accountAttributeUpdatePage.save();
        expect(await accountAttributeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await accountAttributeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last AccountAttribute', async () => {
        const nbButtonsBeforeDelete = await accountAttributeComponentsPage.countDeleteButtons();
        await accountAttributeComponentsPage.clickOnLastDeleteButton();

        accountAttributeDeleteDialog = new AccountAttributeDeleteDialog();
        expect(await accountAttributeDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Account Attribute?');
        await accountAttributeDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(accountAttributeComponentsPage.title), 5000);

        expect(await accountAttributeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
