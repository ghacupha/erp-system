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

import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  LeaseAmortizationCalculationComponentsPage,
  /* LeaseAmortizationCalculationDeleteDialog, */
  LeaseAmortizationCalculationUpdatePage,
} from './lease-amortization-calculation.page-object';

const expect = chai.expect;

describe('LeaseAmortizationCalculation e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let leaseAmortizationCalculationComponentsPage: LeaseAmortizationCalculationComponentsPage;
  let leaseAmortizationCalculationUpdatePage: LeaseAmortizationCalculationUpdatePage;
  /* let leaseAmortizationCalculationDeleteDialog: LeaseAmortizationCalculationDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load LeaseAmortizationCalculations', async () => {
    await navBarPage.goToEntity('lease-amortization-calculation');
    leaseAmortizationCalculationComponentsPage = new LeaseAmortizationCalculationComponentsPage();
    await browser.wait(ec.visibilityOf(leaseAmortizationCalculationComponentsPage.title), 5000);
    expect(await leaseAmortizationCalculationComponentsPage.getTitle()).to.eq('Lease Amortization Calculations');
    await browser.wait(
      ec.or(
        ec.visibilityOf(leaseAmortizationCalculationComponentsPage.entities),
        ec.visibilityOf(leaseAmortizationCalculationComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create LeaseAmortizationCalculation page', async () => {
    await leaseAmortizationCalculationComponentsPage.clickOnCreateButton();
    leaseAmortizationCalculationUpdatePage = new LeaseAmortizationCalculationUpdatePage();
    expect(await leaseAmortizationCalculationUpdatePage.getPageTitle()).to.eq('Create or edit a Lease Amortization Calculation');
    await leaseAmortizationCalculationUpdatePage.cancel();
  });

  /* it('should create and save LeaseAmortizationCalculations', async () => {
        const nbButtonsBeforeCreate = await leaseAmortizationCalculationComponentsPage.countDeleteButtons();

        await leaseAmortizationCalculationComponentsPage.clickOnCreateButton();

        await promise.all([
            leaseAmortizationCalculationUpdatePage.setInterestRateInput('5'),
            leaseAmortizationCalculationUpdatePage.setPeriodicityInput('periodicity'),
            leaseAmortizationCalculationUpdatePage.setLeaseAmountInput('5'),
            leaseAmortizationCalculationUpdatePage.setNumberOfPeriodsInput('5'),
            leaseAmortizationCalculationUpdatePage.leaseContractSelectLastOption(),
        ]);

        await leaseAmortizationCalculationUpdatePage.save();
        expect(await leaseAmortizationCalculationUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await leaseAmortizationCalculationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last LeaseAmortizationCalculation', async () => {
        const nbButtonsBeforeDelete = await leaseAmortizationCalculationComponentsPage.countDeleteButtons();
        await leaseAmortizationCalculationComponentsPage.clickOnLastDeleteButton();

        leaseAmortizationCalculationDeleteDialog = new LeaseAmortizationCalculationDeleteDialog();
        expect(await leaseAmortizationCalculationDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Lease Amortization Calculation?');
        await leaseAmortizationCalculationDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(leaseAmortizationCalculationComponentsPage.title), 5000);

        expect(await leaseAmortizationCalculationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
