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
  ExchangeRateComponentsPage,
  /* ExchangeRateDeleteDialog, */
  ExchangeRateUpdatePage,
} from './exchange-rate.page-object';

const expect = chai.expect;

describe('ExchangeRate e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let exchangeRateComponentsPage: ExchangeRateComponentsPage;
  let exchangeRateUpdatePage: ExchangeRateUpdatePage;
  /* let exchangeRateDeleteDialog: ExchangeRateDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ExchangeRates', async () => {
    await navBarPage.goToEntity('exchange-rate');
    exchangeRateComponentsPage = new ExchangeRateComponentsPage();
    await browser.wait(ec.visibilityOf(exchangeRateComponentsPage.title), 5000);
    expect(await exchangeRateComponentsPage.getTitle()).to.eq('Exchange Rates');
    await browser.wait(
      ec.or(ec.visibilityOf(exchangeRateComponentsPage.entities), ec.visibilityOf(exchangeRateComponentsPage.noResult)),
      1000
    );
  });

  it('should load create ExchangeRate page', async () => {
    await exchangeRateComponentsPage.clickOnCreateButton();
    exchangeRateUpdatePage = new ExchangeRateUpdatePage();
    expect(await exchangeRateUpdatePage.getPageTitle()).to.eq('Create or edit a Exchange Rate');
    await exchangeRateUpdatePage.cancel();
  });

  /* it('should create and save ExchangeRates', async () => {
        const nbButtonsBeforeCreate = await exchangeRateComponentsPage.countDeleteButtons();

        await exchangeRateComponentsPage.clickOnCreateButton();

        await promise.all([
            exchangeRateUpdatePage.setBusinessReportingDayInput('2000-12-31'),
            exchangeRateUpdatePage.setBuyingRateInput('5'),
            exchangeRateUpdatePage.setSellingRateInput('5'),
            exchangeRateUpdatePage.setMeanRateInput('5'),
            exchangeRateUpdatePage.setClosingBidRateInput('5'),
            exchangeRateUpdatePage.setClosingOfferRateInput('5'),
            exchangeRateUpdatePage.setUsdCrossRateInput('5'),
            exchangeRateUpdatePage.institutionCodeSelectLastOption(),
            exchangeRateUpdatePage.currencyCodeSelectLastOption(),
        ]);

        await exchangeRateUpdatePage.save();
        expect(await exchangeRateUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await exchangeRateComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last ExchangeRate', async () => {
        const nbButtonsBeforeDelete = await exchangeRateComponentsPage.countDeleteButtons();
        await exchangeRateComponentsPage.clickOnLastDeleteButton();

        exchangeRateDeleteDialog = new ExchangeRateDeleteDialog();
        expect(await exchangeRateDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Exchange Rate?');
        await exchangeRateDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(exchangeRateComponentsPage.title), 5000);

        expect(await exchangeRateComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
