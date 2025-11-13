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
  PrepaymentAmortizationComponentsPage,
  /* PrepaymentAmortizationDeleteDialog, */
  PrepaymentAmortizationUpdatePage,
} from './prepayment-amortization.page-object';

const expect = chai.expect;

describe('PrepaymentAmortization e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let prepaymentAmortizationComponentsPage: PrepaymentAmortizationComponentsPage;
  let prepaymentAmortizationUpdatePage: PrepaymentAmortizationUpdatePage;
  /* let prepaymentAmortizationDeleteDialog: PrepaymentAmortizationDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load PrepaymentAmortizations', async () => {
    await navBarPage.goToEntity('prepayment-amortization');
    prepaymentAmortizationComponentsPage = new PrepaymentAmortizationComponentsPage();
    await browser.wait(ec.visibilityOf(prepaymentAmortizationComponentsPage.title), 5000);
    expect(await prepaymentAmortizationComponentsPage.getTitle()).to.eq('Prepayment Amortizations');
    await browser.wait(
      ec.or(ec.visibilityOf(prepaymentAmortizationComponentsPage.entities), ec.visibilityOf(prepaymentAmortizationComponentsPage.noResult)),
      1000
    );
  });

  it('should load create PrepaymentAmortization page', async () => {
    await prepaymentAmortizationComponentsPage.clickOnCreateButton();
    prepaymentAmortizationUpdatePage = new PrepaymentAmortizationUpdatePage();
    expect(await prepaymentAmortizationUpdatePage.getPageTitle()).to.eq('Create or edit a Prepayment Amortization');
    await prepaymentAmortizationUpdatePage.cancel();
  });

  /* it('should create and save PrepaymentAmortizations', async () => {
        const nbButtonsBeforeCreate = await prepaymentAmortizationComponentsPage.countDeleteButtons();

        await prepaymentAmortizationComponentsPage.clickOnCreateButton();

        await promise.all([
            prepaymentAmortizationUpdatePage.setDescriptionInput('description'),
            prepaymentAmortizationUpdatePage.setPrepaymentPeriodInput('2000-12-31'),
            prepaymentAmortizationUpdatePage.setPrepaymentAmountInput('5'),
            prepaymentAmortizationUpdatePage.getInactiveInput().click(),
            prepaymentAmortizationUpdatePage.prepaymentAccountSelectLastOption(),
            prepaymentAmortizationUpdatePage.settlementCurrencySelectLastOption(),
            prepaymentAmortizationUpdatePage.debitAccountSelectLastOption(),
            prepaymentAmortizationUpdatePage.creditAccountSelectLastOption(),
            // prepaymentAmortizationUpdatePage.placeholderSelectLastOption(),
            prepaymentAmortizationUpdatePage.fiscalMonthSelectLastOption(),
            prepaymentAmortizationUpdatePage.prepaymentCompilationRequestSelectLastOption(),
            prepaymentAmortizationUpdatePage.amortizationPeriodSelectLastOption(),
        ]);

        await prepaymentAmortizationUpdatePage.save();
        expect(await prepaymentAmortizationUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await prepaymentAmortizationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last PrepaymentAmortization', async () => {
        const nbButtonsBeforeDelete = await prepaymentAmortizationComponentsPage.countDeleteButtons();
        await prepaymentAmortizationComponentsPage.clickOnLastDeleteButton();

        prepaymentAmortizationDeleteDialog = new PrepaymentAmortizationDeleteDialog();
        expect(await prepaymentAmortizationDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Prepayment Amortization?');
        await prepaymentAmortizationDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(prepaymentAmortizationComponentsPage.title), 5000);

        expect(await prepaymentAmortizationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
