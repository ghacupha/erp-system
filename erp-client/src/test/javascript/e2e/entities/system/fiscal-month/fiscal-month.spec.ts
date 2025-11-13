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
  FiscalMonthComponentsPage,
  /* FiscalMonthDeleteDialog, */
  FiscalMonthUpdatePage,
} from './fiscal-month.page-object';

const expect = chai.expect;

describe('FiscalMonth e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let fiscalMonthComponentsPage: FiscalMonthComponentsPage;
  let fiscalMonthUpdatePage: FiscalMonthUpdatePage;
  /* let fiscalMonthDeleteDialog: FiscalMonthDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load FiscalMonths', async () => {
    await navBarPage.goToEntity('fiscal-month');
    fiscalMonthComponentsPage = new FiscalMonthComponentsPage();
    await browser.wait(ec.visibilityOf(fiscalMonthComponentsPage.title), 5000);
    expect(await fiscalMonthComponentsPage.getTitle()).to.eq('Fiscal Months');
    await browser.wait(
      ec.or(ec.visibilityOf(fiscalMonthComponentsPage.entities), ec.visibilityOf(fiscalMonthComponentsPage.noResult)),
      1000
    );
  });

  it('should load create FiscalMonth page', async () => {
    await fiscalMonthComponentsPage.clickOnCreateButton();
    fiscalMonthUpdatePage = new FiscalMonthUpdatePage();
    expect(await fiscalMonthUpdatePage.getPageTitle()).to.eq('Create or edit a Fiscal Month');
    await fiscalMonthUpdatePage.cancel();
  });

  /* it('should create and save FiscalMonths', async () => {
        const nbButtonsBeforeCreate = await fiscalMonthComponentsPage.countDeleteButtons();

        await fiscalMonthComponentsPage.clickOnCreateButton();

        await promise.all([
            fiscalMonthUpdatePage.setMonthNumberInput('5'),
            fiscalMonthUpdatePage.setStartDateInput('2000-12-31'),
            fiscalMonthUpdatePage.setEndDateInput('2000-12-31'),
            fiscalMonthUpdatePage.setFiscalMonthCodeInput('fiscalMonthCode'),
            fiscalMonthUpdatePage.fiscalYearSelectLastOption(),
            // fiscalMonthUpdatePage.placeholderSelectLastOption(),
            // fiscalMonthUpdatePage.universallyUniqueMappingSelectLastOption(),
            fiscalMonthUpdatePage.fiscalQuarterSelectLastOption(),
        ]);

        await fiscalMonthUpdatePage.save();
        expect(await fiscalMonthUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await fiscalMonthComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last FiscalMonth', async () => {
        const nbButtonsBeforeDelete = await fiscalMonthComponentsPage.countDeleteButtons();
        await fiscalMonthComponentsPage.clickOnLastDeleteButton();

        fiscalMonthDeleteDialog = new FiscalMonthDeleteDialog();
        expect(await fiscalMonthDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Fiscal Month?');
        await fiscalMonthDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(fiscalMonthComponentsPage.title), 5000);

        expect(await fiscalMonthComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
