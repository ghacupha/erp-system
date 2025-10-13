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

import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { DepreciationJobComponentsPage, DepreciationJobDeleteDialog, DepreciationJobUpdatePage } from './depreciation-job.page-object';

const expect = chai.expect;

describe('DepreciationJob e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let depreciationJobComponentsPage: DepreciationJobComponentsPage;
  let depreciationJobUpdatePage: DepreciationJobUpdatePage;
  let depreciationJobDeleteDialog: DepreciationJobDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load DepreciationJobs', async () => {
    await navBarPage.goToEntity('depreciation-job');
    depreciationJobComponentsPage = new DepreciationJobComponentsPage();
    await browser.wait(ec.visibilityOf(depreciationJobComponentsPage.title), 5000);
    expect(await depreciationJobComponentsPage.getTitle()).to.eq('Depreciation Jobs');
    await browser.wait(
      ec.or(ec.visibilityOf(depreciationJobComponentsPage.entities), ec.visibilityOf(depreciationJobComponentsPage.noResult)),
      1000
    );
  });

  it('should load create DepreciationJob page', async () => {
    await depreciationJobComponentsPage.clickOnCreateButton();
    depreciationJobUpdatePage = new DepreciationJobUpdatePage();
    expect(await depreciationJobUpdatePage.getPageTitle()).to.eq('Create or edit a Depreciation Job');
    await depreciationJobUpdatePage.cancel();
  });

  it('should create and save DepreciationJobs', async () => {
    const nbButtonsBeforeCreate = await depreciationJobComponentsPage.countDeleteButtons();

    await depreciationJobComponentsPage.clickOnCreateButton();

    await promise.all([
      depreciationJobUpdatePage.setTimeOfCommencementInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      depreciationJobUpdatePage.depreciationJobStatusSelectLastOption(),
      depreciationJobUpdatePage.setDescriptionInput('description'),
      depreciationJobUpdatePage.setNumberOfBatchesInput('5'),
      depreciationJobUpdatePage.setProcessedBatchesInput('5'),
      depreciationJobUpdatePage.setLastBatchSizeInput('5'),
      depreciationJobUpdatePage.setProcessedItemsInput('5'),
      depreciationJobUpdatePage.setProcessingTimeInput('PT12S'),
      depreciationJobUpdatePage.setTotalItemsInput('5'),
      depreciationJobUpdatePage.createdBySelectLastOption(),
      depreciationJobUpdatePage.depreciationPeriodSelectLastOption(),
    ]);

    await depreciationJobUpdatePage.save();
    expect(await depreciationJobUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await depreciationJobComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last DepreciationJob', async () => {
    const nbButtonsBeforeDelete = await depreciationJobComponentsPage.countDeleteButtons();
    await depreciationJobComponentsPage.clickOnLastDeleteButton();

    depreciationJobDeleteDialog = new DepreciationJobDeleteDialog();
    expect(await depreciationJobDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Depreciation Job?');
    await depreciationJobDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(depreciationJobComponentsPage.title), 5000);

    expect(await depreciationJobComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
