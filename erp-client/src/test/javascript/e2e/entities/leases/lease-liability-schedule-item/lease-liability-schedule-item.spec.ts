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
  LeaseLiabilityScheduleItemComponentsPage,
  /* LeaseLiabilityScheduleItemDeleteDialog, */
  LeaseLiabilityScheduleItemUpdatePage,
} from './lease-liability-schedule-item.page-object';

const expect = chai.expect;

describe('LeaseLiabilityScheduleItem e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let leaseLiabilityScheduleItemComponentsPage: LeaseLiabilityScheduleItemComponentsPage;
  let leaseLiabilityScheduleItemUpdatePage: LeaseLiabilityScheduleItemUpdatePage;
  /* let leaseLiabilityScheduleItemDeleteDialog: LeaseLiabilityScheduleItemDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load LeaseLiabilityScheduleItems', async () => {
    await navBarPage.goToEntity('lease-liability-schedule-item');
    leaseLiabilityScheduleItemComponentsPage = new LeaseLiabilityScheduleItemComponentsPage();
    await browser.wait(ec.visibilityOf(leaseLiabilityScheduleItemComponentsPage.title), 5000);
    expect(await leaseLiabilityScheduleItemComponentsPage.getTitle()).to.eq('Lease Liability Schedule Items');
    await browser.wait(
      ec.or(
        ec.visibilityOf(leaseLiabilityScheduleItemComponentsPage.entities),
        ec.visibilityOf(leaseLiabilityScheduleItemComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create LeaseLiabilityScheduleItem page', async () => {
    await leaseLiabilityScheduleItemComponentsPage.clickOnCreateButton();
    leaseLiabilityScheduleItemUpdatePage = new LeaseLiabilityScheduleItemUpdatePage();
    expect(await leaseLiabilityScheduleItemUpdatePage.getPageTitle()).to.eq('Create or edit a Lease Liability Schedule Item');
    await leaseLiabilityScheduleItemUpdatePage.cancel();
  });

  /* it('should create and save LeaseLiabilityScheduleItems', async () => {
        const nbButtonsBeforeCreate = await leaseLiabilityScheduleItemComponentsPage.countDeleteButtons();

        await leaseLiabilityScheduleItemComponentsPage.clickOnCreateButton();

        await promise.all([
            leaseLiabilityScheduleItemUpdatePage.setSequenceNumberInput('5'),
            leaseLiabilityScheduleItemUpdatePage.setOpeningBalanceInput('5'),
            leaseLiabilityScheduleItemUpdatePage.setCashPaymentInput('5'),
            leaseLiabilityScheduleItemUpdatePage.setPrincipalPaymentInput('5'),
            leaseLiabilityScheduleItemUpdatePage.setInterestPaymentInput('5'),
            leaseLiabilityScheduleItemUpdatePage.setOutstandingBalanceInput('5'),
            leaseLiabilityScheduleItemUpdatePage.setInterestPayableOpeningInput('5'),
            leaseLiabilityScheduleItemUpdatePage.setInterestAccruedInput('5'),
            leaseLiabilityScheduleItemUpdatePage.setInterestPayableClosingInput('5'),
            // leaseLiabilityScheduleItemUpdatePage.placeholderSelectLastOption(),
            // leaseLiabilityScheduleItemUpdatePage.universallyUniqueMappingSelectLastOption(),
            leaseLiabilityScheduleItemUpdatePage.leaseAmortizationScheduleSelectLastOption(),
            leaseLiabilityScheduleItemUpdatePage.leaseContractSelectLastOption(),
            leaseLiabilityScheduleItemUpdatePage.leaseLiabilitySelectLastOption(),
            leaseLiabilityScheduleItemUpdatePage.leasePeriodSelectLastOption(),
        ]);

        await leaseLiabilityScheduleItemUpdatePage.save();
        expect(await leaseLiabilityScheduleItemUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await leaseLiabilityScheduleItemComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last LeaseLiabilityScheduleItem', async () => {
        const nbButtonsBeforeDelete = await leaseLiabilityScheduleItemComponentsPage.countDeleteButtons();
        await leaseLiabilityScheduleItemComponentsPage.clickOnLastDeleteButton();

        leaseLiabilityScheduleItemDeleteDialog = new LeaseLiabilityScheduleItemDeleteDialog();
        expect(await leaseLiabilityScheduleItemDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Lease Liability Schedule Item?');
        await leaseLiabilityScheduleItemDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(leaseLiabilityScheduleItemComponentsPage.title), 5000);

        expect(await leaseLiabilityScheduleItemComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
