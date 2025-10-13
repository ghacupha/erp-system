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
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  LeasePeriodComponentsPage,
  /* LeasePeriodDeleteDialog, */
  LeasePeriodUpdatePage,
} from './lease-period.page-object';

const expect = chai.expect;

describe('LeasePeriod e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let leasePeriodComponentsPage: LeasePeriodComponentsPage;
  let leasePeriodUpdatePage: LeasePeriodUpdatePage;
  /* let leasePeriodDeleteDialog: LeasePeriodDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load LeasePeriods', async () => {
    await navBarPage.goToEntity('lease-period');
    leasePeriodComponentsPage = new LeasePeriodComponentsPage();
    await browser.wait(ec.visibilityOf(leasePeriodComponentsPage.title), 5000);
    expect(await leasePeriodComponentsPage.getTitle()).to.eq('Lease Periods');
    await browser.wait(
      ec.or(ec.visibilityOf(leasePeriodComponentsPage.entities), ec.visibilityOf(leasePeriodComponentsPage.noResult)),
      1000
    );
  });

  it('should load create LeasePeriod page', async () => {
    await leasePeriodComponentsPage.clickOnCreateButton();
    leasePeriodUpdatePage = new LeasePeriodUpdatePage();
    expect(await leasePeriodUpdatePage.getPageTitle()).to.eq('Create or edit a Lease Period');
    await leasePeriodUpdatePage.cancel();
  });

  /* it('should create and save LeasePeriods', async () => {
        const nbButtonsBeforeCreate = await leasePeriodComponentsPage.countDeleteButtons();

        await leasePeriodComponentsPage.clickOnCreateButton();

        await promise.all([
            leasePeriodUpdatePage.setSequenceNumberInput('5'),
            leasePeriodUpdatePage.setStartDateInput('2000-12-31'),
            leasePeriodUpdatePage.setEndDateInput('2000-12-31'),
            leasePeriodUpdatePage.setPeriodCodeInput('periodCode'),
            leasePeriodUpdatePage.fiscalMonthSelectLastOption(),
        ]);

        await leasePeriodUpdatePage.save();
        expect(await leasePeriodUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await leasePeriodComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last LeasePeriod', async () => {
        const nbButtonsBeforeDelete = await leasePeriodComponentsPage.countDeleteButtons();
        await leasePeriodComponentsPage.clickOnLastDeleteButton();

        leasePeriodDeleteDialog = new LeasePeriodDeleteDialog();
        expect(await leasePeriodDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Lease Period?');
        await leasePeriodDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(leasePeriodComponentsPage.title), 5000);

        expect(await leasePeriodComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
