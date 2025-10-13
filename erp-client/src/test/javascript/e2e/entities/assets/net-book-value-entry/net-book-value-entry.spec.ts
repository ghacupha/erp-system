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

import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  NetBookValueEntryComponentsPage,
  NetBookValueEntryDeleteDialog,
  NetBookValueEntryUpdatePage,
} from './net-book-value-entry.page-object';

const expect = chai.expect;

describe('NetBookValueEntry e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let netBookValueEntryComponentsPage: NetBookValueEntryComponentsPage;
  let netBookValueEntryUpdatePage: NetBookValueEntryUpdatePage;
  let netBookValueEntryDeleteDialog: NetBookValueEntryDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load NetBookValueEntries', async () => {
    await navBarPage.goToEntity('net-book-value-entry');
    netBookValueEntryComponentsPage = new NetBookValueEntryComponentsPage();
    await browser.wait(ec.visibilityOf(netBookValueEntryComponentsPage.title), 5000);
    expect(await netBookValueEntryComponentsPage.getTitle()).to.eq('Net Book Value Entries');
    await browser.wait(
      ec.or(ec.visibilityOf(netBookValueEntryComponentsPage.entities), ec.visibilityOf(netBookValueEntryComponentsPage.noResult)),
      1000
    );
  });

  it('should load create NetBookValueEntry page', async () => {
    await netBookValueEntryComponentsPage.clickOnCreateButton();
    netBookValueEntryUpdatePage = new NetBookValueEntryUpdatePage();
    expect(await netBookValueEntryUpdatePage.getPageTitle()).to.eq('Create or edit a Net Book Value Entry');
    await netBookValueEntryUpdatePage.cancel();
  });

  it('should create and save NetBookValueEntries', async () => {
    const nbButtonsBeforeCreate = await netBookValueEntryComponentsPage.countDeleteButtons();

    await netBookValueEntryComponentsPage.clickOnCreateButton();

    await promise.all([
      netBookValueEntryUpdatePage.setAssetNumberInput('assetNumber'),
      netBookValueEntryUpdatePage.setAssetTagInput('assetTag'),
      netBookValueEntryUpdatePage.setAssetDescriptionInput('assetDescription'),
      netBookValueEntryUpdatePage.setNbvIdentifierInput('64c99148-3908-465d-8c4a-e510e3ade974'),
      netBookValueEntryUpdatePage.setCompilationJobIdentifierInput('64c99148-3908-465d-8c4a-e510e3ade974'),
      netBookValueEntryUpdatePage.setCompilationBatchIdentifierInput('64c99148-3908-465d-8c4a-e510e3ade974'),
      netBookValueEntryUpdatePage.setElapsedMonthsInput('5'),
      netBookValueEntryUpdatePage.setPriorMonthsInput('5'),
      netBookValueEntryUpdatePage.setUsefulLifeYearsInput('5'),
      netBookValueEntryUpdatePage.setNetBookValueAmountInput('5'),
      netBookValueEntryUpdatePage.setPreviousNetBookValueAmountInput('5'),
      netBookValueEntryUpdatePage.setHistoricalCostInput('5'),
      netBookValueEntryUpdatePage.serviceOutletSelectLastOption(),
      netBookValueEntryUpdatePage.depreciationPeriodSelectLastOption(),
      netBookValueEntryUpdatePage.fiscalMonthSelectLastOption(),
      netBookValueEntryUpdatePage.depreciationMethodSelectLastOption(),
      netBookValueEntryUpdatePage.assetRegistrationSelectLastOption(),
      netBookValueEntryUpdatePage.assetCategorySelectLastOption(),
      // netBookValueEntryUpdatePage.placeholderSelectLastOption(),
    ]);

    await netBookValueEntryUpdatePage.save();
    expect(await netBookValueEntryUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await netBookValueEntryComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last NetBookValueEntry', async () => {
    const nbButtonsBeforeDelete = await netBookValueEntryComponentsPage.countDeleteButtons();
    await netBookValueEntryComponentsPage.clickOnLastDeleteButton();

    netBookValueEntryDeleteDialog = new NetBookValueEntryDeleteDialog();
    expect(await netBookValueEntryDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Net Book Value Entry?');
    await netBookValueEntryDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(netBookValueEntryComponentsPage.title), 5000);

    expect(await netBookValueEntryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
