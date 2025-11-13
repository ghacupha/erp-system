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
  LeaseAmortizationScheduleComponentsPage,
  /* LeaseAmortizationScheduleDeleteDialog, */
  LeaseAmortizationScheduleUpdatePage,
} from './lease-amortization-schedule.page-object';

const expect = chai.expect;

describe('LeaseAmortizationSchedule e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let leaseAmortizationScheduleComponentsPage: LeaseAmortizationScheduleComponentsPage;
  let leaseAmortizationScheduleUpdatePage: LeaseAmortizationScheduleUpdatePage;
  /* let leaseAmortizationScheduleDeleteDialog: LeaseAmortizationScheduleDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load LeaseAmortizationSchedules', async () => {
    await navBarPage.goToEntity('lease-amortization-schedule');
    leaseAmortizationScheduleComponentsPage = new LeaseAmortizationScheduleComponentsPage();
    await browser.wait(ec.visibilityOf(leaseAmortizationScheduleComponentsPage.title), 5000);
    expect(await leaseAmortizationScheduleComponentsPage.getTitle()).to.eq('Lease Amortization Schedules');
    await browser.wait(
      ec.or(
        ec.visibilityOf(leaseAmortizationScheduleComponentsPage.entities),
        ec.visibilityOf(leaseAmortizationScheduleComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create LeaseAmortizationSchedule page', async () => {
    await leaseAmortizationScheduleComponentsPage.clickOnCreateButton();
    leaseAmortizationScheduleUpdatePage = new LeaseAmortizationScheduleUpdatePage();
    expect(await leaseAmortizationScheduleUpdatePage.getPageTitle()).to.eq('Create or edit a Lease Amortization Schedule');
    await leaseAmortizationScheduleUpdatePage.cancel();
  });

  /* it('should create and save LeaseAmortizationSchedules', async () => {
        const nbButtonsBeforeCreate = await leaseAmortizationScheduleComponentsPage.countDeleteButtons();

        await leaseAmortizationScheduleComponentsPage.clickOnCreateButton();

        await promise.all([
            leaseAmortizationScheduleUpdatePage.setIdentifierInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            leaseAmortizationScheduleUpdatePage.leaseLiabilitySelectLastOption(),
            leaseAmortizationScheduleUpdatePage.leaseContractSelectLastOption(),
        ]);

        await leaseAmortizationScheduleUpdatePage.save();
        expect(await leaseAmortizationScheduleUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await leaseAmortizationScheduleComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last LeaseAmortizationSchedule', async () => {
        const nbButtonsBeforeDelete = await leaseAmortizationScheduleComponentsPage.countDeleteButtons();
        await leaseAmortizationScheduleComponentsPage.clickOnLastDeleteButton();

        leaseAmortizationScheduleDeleteDialog = new LeaseAmortizationScheduleDeleteDialog();
        expect(await leaseAmortizationScheduleDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Lease Amortization Schedule?');
        await leaseAmortizationScheduleDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(leaseAmortizationScheduleComponentsPage.title), 5000);

        expect(await leaseAmortizationScheduleComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
