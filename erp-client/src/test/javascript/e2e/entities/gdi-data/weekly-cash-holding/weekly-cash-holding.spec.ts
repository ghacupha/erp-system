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
  WeeklyCashHoldingComponentsPage,
  /* WeeklyCashHoldingDeleteDialog, */
  WeeklyCashHoldingUpdatePage,
} from './weekly-cash-holding.page-object';

const expect = chai.expect;

describe('WeeklyCashHolding e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let weeklyCashHoldingComponentsPage: WeeklyCashHoldingComponentsPage;
  let weeklyCashHoldingUpdatePage: WeeklyCashHoldingUpdatePage;
  /* let weeklyCashHoldingDeleteDialog: WeeklyCashHoldingDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load WeeklyCashHoldings', async () => {
    await navBarPage.goToEntity('weekly-cash-holding');
    weeklyCashHoldingComponentsPage = new WeeklyCashHoldingComponentsPage();
    await browser.wait(ec.visibilityOf(weeklyCashHoldingComponentsPage.title), 5000);
    expect(await weeklyCashHoldingComponentsPage.getTitle()).to.eq('Weekly Cash Holdings');
    await browser.wait(
      ec.or(ec.visibilityOf(weeklyCashHoldingComponentsPage.entities), ec.visibilityOf(weeklyCashHoldingComponentsPage.noResult)),
      1000
    );
  });

  it('should load create WeeklyCashHolding page', async () => {
    await weeklyCashHoldingComponentsPage.clickOnCreateButton();
    weeklyCashHoldingUpdatePage = new WeeklyCashHoldingUpdatePage();
    expect(await weeklyCashHoldingUpdatePage.getPageTitle()).to.eq('Create or edit a Weekly Cash Holding');
    await weeklyCashHoldingUpdatePage.cancel();
  });

  /* it('should create and save WeeklyCashHoldings', async () => {
        const nbButtonsBeforeCreate = await weeklyCashHoldingComponentsPage.countDeleteButtons();

        await weeklyCashHoldingComponentsPage.clickOnCreateButton();

        await promise.all([
            weeklyCashHoldingUpdatePage.setReportingDateInput('2000-12-31'),
            weeklyCashHoldingUpdatePage.setFitUnitsInput('5'),
            weeklyCashHoldingUpdatePage.setUnfitUnitsInput('5'),
            weeklyCashHoldingUpdatePage.bankCodeSelectLastOption(),
            weeklyCashHoldingUpdatePage.branchIdSelectLastOption(),
            weeklyCashHoldingUpdatePage.subCountyCodeSelectLastOption(),
            weeklyCashHoldingUpdatePage.denominationSelectLastOption(),
        ]);

        await weeklyCashHoldingUpdatePage.save();
        expect(await weeklyCashHoldingUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await weeklyCashHoldingComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last WeeklyCashHolding', async () => {
        const nbButtonsBeforeDelete = await weeklyCashHoldingComponentsPage.countDeleteButtons();
        await weeklyCashHoldingComponentsPage.clickOnLastDeleteButton();

        weeklyCashHoldingDeleteDialog = new WeeklyCashHoldingDeleteDialog();
        expect(await weeklyCashHoldingDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Weekly Cash Holding?');
        await weeklyCashHoldingDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(weeklyCashHoldingComponentsPage.title), 5000);

        expect(await weeklyCashHoldingComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
