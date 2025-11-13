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
  RouDepreciationEntryComponentsPage,
  /* RouDepreciationEntryDeleteDialog, */
  RouDepreciationEntryUpdatePage,
} from './rou-depreciation-entry.page-object';

const expect = chai.expect;

describe('RouDepreciationEntry e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let rouDepreciationEntryComponentsPage: RouDepreciationEntryComponentsPage;
  let rouDepreciationEntryUpdatePage: RouDepreciationEntryUpdatePage;
  /* let rouDepreciationEntryDeleteDialog: RouDepreciationEntryDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load RouDepreciationEntries', async () => {
    await navBarPage.goToEntity('rou-depreciation-entry');
    rouDepreciationEntryComponentsPage = new RouDepreciationEntryComponentsPage();
    await browser.wait(ec.visibilityOf(rouDepreciationEntryComponentsPage.title), 5000);
    expect(await rouDepreciationEntryComponentsPage.getTitle()).to.eq('Rou Depreciation Entries');
    await browser.wait(
      ec.or(ec.visibilityOf(rouDepreciationEntryComponentsPage.entities), ec.visibilityOf(rouDepreciationEntryComponentsPage.noResult)),
      1000
    );
  });

  it('should load create RouDepreciationEntry page', async () => {
    await rouDepreciationEntryComponentsPage.clickOnCreateButton();
    rouDepreciationEntryUpdatePage = new RouDepreciationEntryUpdatePage();
    expect(await rouDepreciationEntryUpdatePage.getPageTitle()).to.eq('Create or edit a Rou Depreciation Entry');
    await rouDepreciationEntryUpdatePage.cancel();
  });

  /* it('should create and save RouDepreciationEntries', async () => {
        const nbButtonsBeforeCreate = await rouDepreciationEntryComponentsPage.countDeleteButtons();

        await rouDepreciationEntryComponentsPage.clickOnCreateButton();

        await promise.all([
            rouDepreciationEntryUpdatePage.setDescriptionInput('description'),
            rouDepreciationEntryUpdatePage.setDepreciationAmountInput('5'),
            rouDepreciationEntryUpdatePage.setOutstandingAmountInput('5'),
            rouDepreciationEntryUpdatePage.setRouAssetIdentifierInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            rouDepreciationEntryUpdatePage.setRouDepreciationIdentifierInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            rouDepreciationEntryUpdatePage.setSequenceNumberInput('5'),
            rouDepreciationEntryUpdatePage.getInvalidatedInput().click(),
            rouDepreciationEntryUpdatePage.debitAccountSelectLastOption(),
            rouDepreciationEntryUpdatePage.creditAccountSelectLastOption(),
            rouDepreciationEntryUpdatePage.assetCategorySelectLastOption(),
            rouDepreciationEntryUpdatePage.leaseContractSelectLastOption(),
            rouDepreciationEntryUpdatePage.rouMetadataSelectLastOption(),
        ]);

        await rouDepreciationEntryUpdatePage.save();
        expect(await rouDepreciationEntryUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await rouDepreciationEntryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last RouDepreciationEntry', async () => {
        const nbButtonsBeforeDelete = await rouDepreciationEntryComponentsPage.countDeleteButtons();
        await rouDepreciationEntryComponentsPage.clickOnLastDeleteButton();

        rouDepreciationEntryDeleteDialog = new RouDepreciationEntryDeleteDialog();
        expect(await rouDepreciationEntryDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Rou Depreciation Entry?');
        await rouDepreciationEntryDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(rouDepreciationEntryComponentsPage.title), 5000);

        expect(await rouDepreciationEntryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
