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

import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  DepreciationEntryComponentsPage,
  DepreciationEntryDeleteDialog,
  DepreciationEntryUpdatePage,
} from './depreciation-entry.page-object';

const expect = chai.expect;

describe('DepreciationEntry e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let depreciationEntryComponentsPage: DepreciationEntryComponentsPage;
  let depreciationEntryUpdatePage: DepreciationEntryUpdatePage;
  let depreciationEntryDeleteDialog: DepreciationEntryDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load DepreciationEntries', async () => {
    await navBarPage.goToEntity('depreciation-entry');
    depreciationEntryComponentsPage = new DepreciationEntryComponentsPage();
    await browser.wait(ec.visibilityOf(depreciationEntryComponentsPage.title), 5000);
    expect(await depreciationEntryComponentsPage.getTitle()).to.eq('Depreciation Entries');
    await browser.wait(
      ec.or(ec.visibilityOf(depreciationEntryComponentsPage.entities), ec.visibilityOf(depreciationEntryComponentsPage.noResult)),
      1000
    );
  });

  it('should load create DepreciationEntry page', async () => {
    await depreciationEntryComponentsPage.clickOnCreateButton();
    depreciationEntryUpdatePage = new DepreciationEntryUpdatePage();
    expect(await depreciationEntryUpdatePage.getPageTitle()).to.eq('Create or edit a Depreciation Entry');
    await depreciationEntryUpdatePage.cancel();
  });

  it('should create and save DepreciationEntries', async () => {
    const nbButtonsBeforeCreate = await depreciationEntryComponentsPage.countDeleteButtons();

    await depreciationEntryComponentsPage.clickOnCreateButton();

    await promise.all([
      depreciationEntryUpdatePage.setPostedAtInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      depreciationEntryUpdatePage.setDepreciationAmountInput('5'),
      depreciationEntryUpdatePage.setAssetNumberInput('5'),
      depreciationEntryUpdatePage.setBatchSequenceNumberInput('5'),
      depreciationEntryUpdatePage.setProcessedItemsInput('processedItems'),
      depreciationEntryUpdatePage.setTotalItemsProcessedInput('5'),
      depreciationEntryUpdatePage.serviceOutletSelectLastOption(),
      depreciationEntryUpdatePage.assetCategorySelectLastOption(),
      depreciationEntryUpdatePage.depreciationMethodSelectLastOption(),
      depreciationEntryUpdatePage.assetRegistrationSelectLastOption(),
      depreciationEntryUpdatePage.depreciationPeriodSelectLastOption(),
      depreciationEntryUpdatePage.fiscalMonthSelectLastOption(),
      depreciationEntryUpdatePage.fiscalQuarterSelectLastOption(),
      depreciationEntryUpdatePage.fiscalYearSelectLastOption(),
      depreciationEntryUpdatePage.depreciationJobSelectLastOption(),
      depreciationEntryUpdatePage.depreciationBatchSequenceSelectLastOption(),
    ]);

    await depreciationEntryUpdatePage.save();
    expect(await depreciationEntryUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await depreciationEntryComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last DepreciationEntry', async () => {
    const nbButtonsBeforeDelete = await depreciationEntryComponentsPage.countDeleteButtons();
    await depreciationEntryComponentsPage.clickOnLastDeleteButton();

    depreciationEntryDeleteDialog = new DepreciationEntryDeleteDialog();
    expect(await depreciationEntryDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Depreciation Entry?');
    await depreciationEntryDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(depreciationEntryComponentsPage.title), 5000);

    expect(await depreciationEntryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
