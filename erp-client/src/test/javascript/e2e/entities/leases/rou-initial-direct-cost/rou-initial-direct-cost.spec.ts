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
  RouInitialDirectCostComponentsPage,
  /* RouInitialDirectCostDeleteDialog, */
  RouInitialDirectCostUpdatePage,
} from './rou-initial-direct-cost.page-object';

const expect = chai.expect;

describe('RouInitialDirectCost e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let rouInitialDirectCostComponentsPage: RouInitialDirectCostComponentsPage;
  let rouInitialDirectCostUpdatePage: RouInitialDirectCostUpdatePage;
  /* let rouInitialDirectCostDeleteDialog: RouInitialDirectCostDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load RouInitialDirectCosts', async () => {
    await navBarPage.goToEntity('rou-initial-direct-cost');
    rouInitialDirectCostComponentsPage = new RouInitialDirectCostComponentsPage();
    await browser.wait(ec.visibilityOf(rouInitialDirectCostComponentsPage.title), 5000);
    expect(await rouInitialDirectCostComponentsPage.getTitle()).to.eq('Rou Initial Direct Costs');
    await browser.wait(
      ec.or(ec.visibilityOf(rouInitialDirectCostComponentsPage.entities), ec.visibilityOf(rouInitialDirectCostComponentsPage.noResult)),
      1000
    );
  });

  it('should load create RouInitialDirectCost page', async () => {
    await rouInitialDirectCostComponentsPage.clickOnCreateButton();
    rouInitialDirectCostUpdatePage = new RouInitialDirectCostUpdatePage();
    expect(await rouInitialDirectCostUpdatePage.getPageTitle()).to.eq('Create or edit a Rou Initial Direct Cost');
    await rouInitialDirectCostUpdatePage.cancel();
  });

  /* it('should create and save RouInitialDirectCosts', async () => {
        const nbButtonsBeforeCreate = await rouInitialDirectCostComponentsPage.countDeleteButtons();

        await rouInitialDirectCostComponentsPage.clickOnCreateButton();

        await promise.all([
            rouInitialDirectCostUpdatePage.setTransactionDateInput('2000-12-31'),
            rouInitialDirectCostUpdatePage.setDescriptionInput('description'),
            rouInitialDirectCostUpdatePage.setCostInput('5'),
            rouInitialDirectCostUpdatePage.setReferenceNumberInput('5'),
            rouInitialDirectCostUpdatePage.leaseContractSelectLastOption(),
            rouInitialDirectCostUpdatePage.settlementDetailsSelectLastOption(),
            rouInitialDirectCostUpdatePage.targetROUAccountSelectLastOption(),
            rouInitialDirectCostUpdatePage.transferAccountSelectLastOption(),
            // rouInitialDirectCostUpdatePage.placeholderSelectLastOption(),
        ]);

        await rouInitialDirectCostUpdatePage.save();
        expect(await rouInitialDirectCostUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await rouInitialDirectCostComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last RouInitialDirectCost', async () => {
        const nbButtonsBeforeDelete = await rouInitialDirectCostComponentsPage.countDeleteButtons();
        await rouInitialDirectCostComponentsPage.clickOnLastDeleteButton();

        rouInitialDirectCostDeleteDialog = new RouInitialDirectCostDeleteDialog();
        expect(await rouInitialDirectCostDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Rou Initial Direct Cost?');
        await rouInitialDirectCostDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(rouInitialDirectCostComponentsPage.title), 5000);

        expect(await rouInitialDirectCostComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
