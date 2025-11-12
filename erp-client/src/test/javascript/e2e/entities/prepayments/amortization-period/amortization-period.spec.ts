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
  AmortizationPeriodComponentsPage,
  /* AmortizationPeriodDeleteDialog, */
  AmortizationPeriodUpdatePage,
} from './amortization-period.page-object';

const expect = chai.expect;

describe('AmortizationPeriod e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let amortizationPeriodComponentsPage: AmortizationPeriodComponentsPage;
  let amortizationPeriodUpdatePage: AmortizationPeriodUpdatePage;
  /* let amortizationPeriodDeleteDialog: AmortizationPeriodDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load AmortizationPeriods', async () => {
    await navBarPage.goToEntity('amortization-period');
    amortizationPeriodComponentsPage = new AmortizationPeriodComponentsPage();
    await browser.wait(ec.visibilityOf(amortizationPeriodComponentsPage.title), 5000);
    expect(await amortizationPeriodComponentsPage.getTitle()).to.eq('Amortization Periods');
    await browser.wait(
      ec.or(ec.visibilityOf(amortizationPeriodComponentsPage.entities), ec.visibilityOf(amortizationPeriodComponentsPage.noResult)),
      1000
    );
  });

  it('should load create AmortizationPeriod page', async () => {
    await amortizationPeriodComponentsPage.clickOnCreateButton();
    amortizationPeriodUpdatePage = new AmortizationPeriodUpdatePage();
    expect(await amortizationPeriodUpdatePage.getPageTitle()).to.eq('Create or edit a Amortization Period');
    await amortizationPeriodUpdatePage.cancel();
  });

  /* it('should create and save AmortizationPeriods', async () => {
        const nbButtonsBeforeCreate = await amortizationPeriodComponentsPage.countDeleteButtons();

        await amortizationPeriodComponentsPage.clickOnCreateButton();

        await promise.all([
            amortizationPeriodUpdatePage.setSequenceNumberInput('5'),
            amortizationPeriodUpdatePage.setStartDateInput('2000-12-31'),
            amortizationPeriodUpdatePage.setEndDateInput('2000-12-31'),
            amortizationPeriodUpdatePage.setPeriodCodeInput('periodCode'),
            amortizationPeriodUpdatePage.fiscalMonthSelectLastOption(),
            amortizationPeriodUpdatePage.amortizationPeriodSelectLastOption(),
        ]);

        await amortizationPeriodUpdatePage.save();
        expect(await amortizationPeriodUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await amortizationPeriodComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last AmortizationPeriod', async () => {
        const nbButtonsBeforeDelete = await amortizationPeriodComponentsPage.countDeleteButtons();
        await amortizationPeriodComponentsPage.clickOnLastDeleteButton();

        amortizationPeriodDeleteDialog = new AmortizationPeriodDeleteDialog();
        expect(await amortizationPeriodDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Amortization Period?');
        await amortizationPeriodDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(amortizationPeriodComponentsPage.title), 5000);

        expect(await amortizationPeriodComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
