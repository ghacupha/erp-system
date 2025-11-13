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

import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { FiscalYearComponentsPage, FiscalYearDeleteDialog, FiscalYearUpdatePage } from './fiscal-year.page-object';

const expect = chai.expect;

describe('FiscalYear e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let fiscalYearComponentsPage: FiscalYearComponentsPage;
  let fiscalYearUpdatePage: FiscalYearUpdatePage;
  let fiscalYearDeleteDialog: FiscalYearDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load FiscalYears', async () => {
    await navBarPage.goToEntity('fiscal-year');
    fiscalYearComponentsPage = new FiscalYearComponentsPage();
    await browser.wait(ec.visibilityOf(fiscalYearComponentsPage.title), 5000);
    expect(await fiscalYearComponentsPage.getTitle()).to.eq('Fiscal Years');
    await browser.wait(ec.or(ec.visibilityOf(fiscalYearComponentsPage.entities), ec.visibilityOf(fiscalYearComponentsPage.noResult)), 1000);
  });

  it('should load create FiscalYear page', async () => {
    await fiscalYearComponentsPage.clickOnCreateButton();
    fiscalYearUpdatePage = new FiscalYearUpdatePage();
    expect(await fiscalYearUpdatePage.getPageTitle()).to.eq('Create or edit a Fiscal Year');
    await fiscalYearUpdatePage.cancel();
  });

  it('should create and save FiscalYears', async () => {
    const nbButtonsBeforeCreate = await fiscalYearComponentsPage.countDeleteButtons();

    await fiscalYearComponentsPage.clickOnCreateButton();

    await promise.all([
      fiscalYearUpdatePage.setFiscalYearCodeInput('fiscalYearCode'),
      fiscalYearUpdatePage.setStartDateInput('2000-12-31'),
      fiscalYearUpdatePage.setEndDateInput('2000-12-31'),
      fiscalYearUpdatePage.fiscalYearStatusSelectLastOption(),
      // fiscalYearUpdatePage.placeholderSelectLastOption(),
      // fiscalYearUpdatePage.universallyUniqueMappingSelectLastOption(),
      fiscalYearUpdatePage.createdBySelectLastOption(),
      fiscalYearUpdatePage.lastUpdatedBySelectLastOption(),
    ]);

    await fiscalYearUpdatePage.save();
    expect(await fiscalYearUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await fiscalYearComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last FiscalYear', async () => {
    const nbButtonsBeforeDelete = await fiscalYearComponentsPage.countDeleteButtons();
    await fiscalYearComponentsPage.clickOnLastDeleteButton();

    fiscalYearDeleteDialog = new FiscalYearDeleteDialog();
    expect(await fiscalYearDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Fiscal Year?');
    await fiscalYearDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(fiscalYearComponentsPage.title), 5000);

    expect(await fiscalYearComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
