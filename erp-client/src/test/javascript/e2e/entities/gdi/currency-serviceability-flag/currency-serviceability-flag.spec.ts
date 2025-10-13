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
  CurrencyServiceabilityFlagComponentsPage,
  CurrencyServiceabilityFlagDeleteDialog,
  CurrencyServiceabilityFlagUpdatePage,
} from './currency-serviceability-flag.page-object';

const expect = chai.expect;

describe('CurrencyServiceabilityFlag e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let currencyServiceabilityFlagComponentsPage: CurrencyServiceabilityFlagComponentsPage;
  let currencyServiceabilityFlagUpdatePage: CurrencyServiceabilityFlagUpdatePage;
  let currencyServiceabilityFlagDeleteDialog: CurrencyServiceabilityFlagDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CurrencyServiceabilityFlags', async () => {
    await navBarPage.goToEntity('currency-serviceability-flag');
    currencyServiceabilityFlagComponentsPage = new CurrencyServiceabilityFlagComponentsPage();
    await browser.wait(ec.visibilityOf(currencyServiceabilityFlagComponentsPage.title), 5000);
    expect(await currencyServiceabilityFlagComponentsPage.getTitle()).to.eq('Currency Serviceability Flags');
    await browser.wait(
      ec.or(
        ec.visibilityOf(currencyServiceabilityFlagComponentsPage.entities),
        ec.visibilityOf(currencyServiceabilityFlagComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create CurrencyServiceabilityFlag page', async () => {
    await currencyServiceabilityFlagComponentsPage.clickOnCreateButton();
    currencyServiceabilityFlagUpdatePage = new CurrencyServiceabilityFlagUpdatePage();
    expect(await currencyServiceabilityFlagUpdatePage.getPageTitle()).to.eq('Create or edit a Currency Serviceability Flag');
    await currencyServiceabilityFlagUpdatePage.cancel();
  });

  it('should create and save CurrencyServiceabilityFlags', async () => {
    const nbButtonsBeforeCreate = await currencyServiceabilityFlagComponentsPage.countDeleteButtons();

    await currencyServiceabilityFlagComponentsPage.clickOnCreateButton();

    await promise.all([
      currencyServiceabilityFlagUpdatePage.currencyServiceabilityFlagSelectLastOption(),
      currencyServiceabilityFlagUpdatePage.currencyServiceabilitySelectLastOption(),
      currencyServiceabilityFlagUpdatePage.setCurrencyServiceabilityFlagDetailsInput('currencyServiceabilityFlagDetails'),
    ]);

    await currencyServiceabilityFlagUpdatePage.save();
    expect(await currencyServiceabilityFlagUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await currencyServiceabilityFlagComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last CurrencyServiceabilityFlag', async () => {
    const nbButtonsBeforeDelete = await currencyServiceabilityFlagComponentsPage.countDeleteButtons();
    await currencyServiceabilityFlagComponentsPage.clickOnLastDeleteButton();

    currencyServiceabilityFlagDeleteDialog = new CurrencyServiceabilityFlagDeleteDialog();
    expect(await currencyServiceabilityFlagDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Currency Serviceability Flag?'
    );
    await currencyServiceabilityFlagDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(currencyServiceabilityFlagComponentsPage.title), 5000);

    expect(await currencyServiceabilityFlagComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
