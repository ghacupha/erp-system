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
  LeaseLiabilityComponentsPage,
  /* LeaseLiabilityDeleteDialog, */
  LeaseLiabilityUpdatePage,
} from './lease-liability.page-object';

const expect = chai.expect;

describe('LeaseLiability e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let leaseLiabilityComponentsPage: LeaseLiabilityComponentsPage;
  let leaseLiabilityUpdatePage: LeaseLiabilityUpdatePage;
  /* let leaseLiabilityDeleteDialog: LeaseLiabilityDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load LeaseLiabilities', async () => {
    await navBarPage.goToEntity('lease-liability');
    leaseLiabilityComponentsPage = new LeaseLiabilityComponentsPage();
    await browser.wait(ec.visibilityOf(leaseLiabilityComponentsPage.title), 5000);
    expect(await leaseLiabilityComponentsPage.getTitle()).to.eq('Lease Liabilities');
    await browser.wait(
      ec.or(ec.visibilityOf(leaseLiabilityComponentsPage.entities), ec.visibilityOf(leaseLiabilityComponentsPage.noResult)),
      1000
    );
  });

  it('should load create LeaseLiability page', async () => {
    await leaseLiabilityComponentsPage.clickOnCreateButton();
    leaseLiabilityUpdatePage = new LeaseLiabilityUpdatePage();
    expect(await leaseLiabilityUpdatePage.getPageTitle()).to.eq('Create or edit a Lease Liability');
    await leaseLiabilityUpdatePage.cancel();
  });

  /* it('should create and save LeaseLiabilities', async () => {
        const nbButtonsBeforeCreate = await leaseLiabilityComponentsPage.countDeleteButtons();

        await leaseLiabilityComponentsPage.clickOnCreateButton();

        await promise.all([
            leaseLiabilityUpdatePage.setLeaseIdInput('leaseId'),
            leaseLiabilityUpdatePage.setLiabilityAmountInput('5'),
            leaseLiabilityUpdatePage.setStartDateInput('2000-12-31'),
            leaseLiabilityUpdatePage.setEndDateInput('2000-12-31'),
            leaseLiabilityUpdatePage.setInterestRateInput('5'),
            leaseLiabilityUpdatePage.leaseAmortizationCalculationSelectLastOption(),
            leaseLiabilityUpdatePage.leaseContractSelectLastOption(),
        ]);

        await leaseLiabilityUpdatePage.save();
        expect(await leaseLiabilityUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await leaseLiabilityComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last LeaseLiability', async () => {
        const nbButtonsBeforeDelete = await leaseLiabilityComponentsPage.countDeleteButtons();
        await leaseLiabilityComponentsPage.clickOnLastDeleteButton();

        leaseLiabilityDeleteDialog = new LeaseLiabilityDeleteDialog();
        expect(await leaseLiabilityDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Lease Liability?');
        await leaseLiabilityDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(leaseLiabilityComponentsPage.title), 5000);

        expect(await leaseLiabilityComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
