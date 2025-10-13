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
  LeaseRepaymentPeriodComponentsPage,
  /* LeaseRepaymentPeriodDeleteDialog, */
  LeaseRepaymentPeriodUpdatePage,
} from './lease-repayment-period.page-object';

const expect = chai.expect;

describe('LeaseRepaymentPeriod e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let leaseRepaymentPeriodComponentsPage: LeaseRepaymentPeriodComponentsPage;
  let leaseRepaymentPeriodUpdatePage: LeaseRepaymentPeriodUpdatePage;
  /* let leaseRepaymentPeriodDeleteDialog: LeaseRepaymentPeriodDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load LeaseRepaymentPeriods', async () => {
    await navBarPage.goToEntity('lease-repayment-period');
    leaseRepaymentPeriodComponentsPage = new LeaseRepaymentPeriodComponentsPage();
    await browser.wait(ec.visibilityOf(leaseRepaymentPeriodComponentsPage.title), 5000);
    expect(await leaseRepaymentPeriodComponentsPage.getTitle()).to.eq('Lease Repayment Periods');
    await browser.wait(
      ec.or(ec.visibilityOf(leaseRepaymentPeriodComponentsPage.entities), ec.visibilityOf(leaseRepaymentPeriodComponentsPage.noResult)),
      1000
    );
  });

  it('should load create LeaseRepaymentPeriod page', async () => {
    await leaseRepaymentPeriodComponentsPage.clickOnCreateButton();
    leaseRepaymentPeriodUpdatePage = new LeaseRepaymentPeriodUpdatePage();
    expect(await leaseRepaymentPeriodUpdatePage.getPageTitle()).to.eq('Create or edit a Lease Repayment Period');
    await leaseRepaymentPeriodUpdatePage.cancel();
  });

  /* it('should create and save LeaseRepaymentPeriods', async () => {
        const nbButtonsBeforeCreate = await leaseRepaymentPeriodComponentsPage.countDeleteButtons();

        await leaseRepaymentPeriodComponentsPage.clickOnCreateButton();

        await promise.all([
            leaseRepaymentPeriodUpdatePage.setSequenceNumberInput('5'),
            leaseRepaymentPeriodUpdatePage.setStartDateInput('2000-12-31'),
            leaseRepaymentPeriodUpdatePage.setEndDateInput('2000-12-31'),
            leaseRepaymentPeriodUpdatePage.setPeriodCodeInput('periodCode'),
            leaseRepaymentPeriodUpdatePage.fiscalMonthSelectLastOption(),
        ]);

        await leaseRepaymentPeriodUpdatePage.save();
        expect(await leaseRepaymentPeriodUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await leaseRepaymentPeriodComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last LeaseRepaymentPeriod', async () => {
        const nbButtonsBeforeDelete = await leaseRepaymentPeriodComponentsPage.countDeleteButtons();
        await leaseRepaymentPeriodComponentsPage.clickOnLastDeleteButton();

        leaseRepaymentPeriodDeleteDialog = new LeaseRepaymentPeriodDeleteDialog();
        expect(await leaseRepaymentPeriodDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Lease Repayment Period?');
        await leaseRepaymentPeriodDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(leaseRepaymentPeriodComponentsPage.title), 5000);

        expect(await leaseRepaymentPeriodComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
