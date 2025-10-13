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
  KenyanCurrencyDenominationComponentsPage,
  KenyanCurrencyDenominationDeleteDialog,
  KenyanCurrencyDenominationUpdatePage,
} from './kenyan-currency-denomination.page-object';

const expect = chai.expect;

describe('KenyanCurrencyDenomination e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let kenyanCurrencyDenominationComponentsPage: KenyanCurrencyDenominationComponentsPage;
  let kenyanCurrencyDenominationUpdatePage: KenyanCurrencyDenominationUpdatePage;
  let kenyanCurrencyDenominationDeleteDialog: KenyanCurrencyDenominationDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load KenyanCurrencyDenominations', async () => {
    await navBarPage.goToEntity('kenyan-currency-denomination');
    kenyanCurrencyDenominationComponentsPage = new KenyanCurrencyDenominationComponentsPage();
    await browser.wait(ec.visibilityOf(kenyanCurrencyDenominationComponentsPage.title), 5000);
    expect(await kenyanCurrencyDenominationComponentsPage.getTitle()).to.eq('Kenyan Currency Denominations');
    await browser.wait(
      ec.or(
        ec.visibilityOf(kenyanCurrencyDenominationComponentsPage.entities),
        ec.visibilityOf(kenyanCurrencyDenominationComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create KenyanCurrencyDenomination page', async () => {
    await kenyanCurrencyDenominationComponentsPage.clickOnCreateButton();
    kenyanCurrencyDenominationUpdatePage = new KenyanCurrencyDenominationUpdatePage();
    expect(await kenyanCurrencyDenominationUpdatePage.getPageTitle()).to.eq('Create or edit a Kenyan Currency Denomination');
    await kenyanCurrencyDenominationUpdatePage.cancel();
  });

  it('should create and save KenyanCurrencyDenominations', async () => {
    const nbButtonsBeforeCreate = await kenyanCurrencyDenominationComponentsPage.countDeleteButtons();

    await kenyanCurrencyDenominationComponentsPage.clickOnCreateButton();

    await promise.all([
      kenyanCurrencyDenominationUpdatePage.setCurrencyDenominationCodeInput('currencyDenominationCode'),
      kenyanCurrencyDenominationUpdatePage.setCurrencyDenominationTypeInput('currencyDenominationType'),
      kenyanCurrencyDenominationUpdatePage.setCurrencyDenominationTypeDetailsInput('currencyDenominationTypeDetails'),
    ]);

    await kenyanCurrencyDenominationUpdatePage.save();
    expect(await kenyanCurrencyDenominationUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await kenyanCurrencyDenominationComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last KenyanCurrencyDenomination', async () => {
    const nbButtonsBeforeDelete = await kenyanCurrencyDenominationComponentsPage.countDeleteButtons();
    await kenyanCurrencyDenominationComponentsPage.clickOnLastDeleteButton();

    kenyanCurrencyDenominationDeleteDialog = new KenyanCurrencyDenominationDeleteDialog();
    expect(await kenyanCurrencyDenominationDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Kenyan Currency Denomination?'
    );
    await kenyanCurrencyDenominationDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(kenyanCurrencyDenominationComponentsPage.title), 5000);

    expect(await kenyanCurrencyDenominationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
