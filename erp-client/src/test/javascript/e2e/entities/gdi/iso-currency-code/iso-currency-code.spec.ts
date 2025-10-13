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

import { IsoCurrencyCodeComponentsPage, IsoCurrencyCodeDeleteDialog, IsoCurrencyCodeUpdatePage } from './iso-currency-code.page-object';

const expect = chai.expect;

describe('IsoCurrencyCode e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let isoCurrencyCodeComponentsPage: IsoCurrencyCodeComponentsPage;
  let isoCurrencyCodeUpdatePage: IsoCurrencyCodeUpdatePage;
  let isoCurrencyCodeDeleteDialog: IsoCurrencyCodeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load IsoCurrencyCodes', async () => {
    await navBarPage.goToEntity('iso-currency-code');
    isoCurrencyCodeComponentsPage = new IsoCurrencyCodeComponentsPage();
    await browser.wait(ec.visibilityOf(isoCurrencyCodeComponentsPage.title), 5000);
    expect(await isoCurrencyCodeComponentsPage.getTitle()).to.eq('Iso Currency Codes');
    await browser.wait(
      ec.or(ec.visibilityOf(isoCurrencyCodeComponentsPage.entities), ec.visibilityOf(isoCurrencyCodeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create IsoCurrencyCode page', async () => {
    await isoCurrencyCodeComponentsPage.clickOnCreateButton();
    isoCurrencyCodeUpdatePage = new IsoCurrencyCodeUpdatePage();
    expect(await isoCurrencyCodeUpdatePage.getPageTitle()).to.eq('Create or edit a Iso Currency Code');
    await isoCurrencyCodeUpdatePage.cancel();
  });

  it('should create and save IsoCurrencyCodes', async () => {
    const nbButtonsBeforeCreate = await isoCurrencyCodeComponentsPage.countDeleteButtons();

    await isoCurrencyCodeComponentsPage.clickOnCreateButton();

    await promise.all([
      isoCurrencyCodeUpdatePage.setAlphabeticCodeInput('alphabeticCode'),
      isoCurrencyCodeUpdatePage.setNumericCodeInput('numericCode'),
      isoCurrencyCodeUpdatePage.setMinorUnitInput('minorUnit'),
      isoCurrencyCodeUpdatePage.setCurrencyInput('currency'),
      isoCurrencyCodeUpdatePage.setCountryInput('country'),
    ]);

    await isoCurrencyCodeUpdatePage.save();
    expect(await isoCurrencyCodeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await isoCurrencyCodeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last IsoCurrencyCode', async () => {
    const nbButtonsBeforeDelete = await isoCurrencyCodeComponentsPage.countDeleteButtons();
    await isoCurrencyCodeComponentsPage.clickOnLastDeleteButton();

    isoCurrencyCodeDeleteDialog = new IsoCurrencyCodeDeleteDialog();
    expect(await isoCurrencyCodeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Iso Currency Code?');
    await isoCurrencyCodeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(isoCurrencyCodeComponentsPage.title), 5000);

    expect(await isoCurrencyCodeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
