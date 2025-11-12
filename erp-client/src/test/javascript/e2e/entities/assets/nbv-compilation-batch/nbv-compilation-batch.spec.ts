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

import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  NbvCompilationBatchComponentsPage,
  NbvCompilationBatchDeleteDialog,
  NbvCompilationBatchUpdatePage,
} from './nbv-compilation-batch.page-object';

const expect = chai.expect;

describe('NbvCompilationBatch e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let nbvCompilationBatchComponentsPage: NbvCompilationBatchComponentsPage;
  let nbvCompilationBatchUpdatePage: NbvCompilationBatchUpdatePage;
  let nbvCompilationBatchDeleteDialog: NbvCompilationBatchDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load NbvCompilationBatches', async () => {
    await navBarPage.goToEntity('nbv-compilation-batch');
    nbvCompilationBatchComponentsPage = new NbvCompilationBatchComponentsPage();
    await browser.wait(ec.visibilityOf(nbvCompilationBatchComponentsPage.title), 5000);
    expect(await nbvCompilationBatchComponentsPage.getTitle()).to.eq('Nbv Compilation Batches');
    await browser.wait(
      ec.or(ec.visibilityOf(nbvCompilationBatchComponentsPage.entities), ec.visibilityOf(nbvCompilationBatchComponentsPage.noResult)),
      1000
    );
  });

  it('should load create NbvCompilationBatch page', async () => {
    await nbvCompilationBatchComponentsPage.clickOnCreateButton();
    nbvCompilationBatchUpdatePage = new NbvCompilationBatchUpdatePage();
    expect(await nbvCompilationBatchUpdatePage.getPageTitle()).to.eq('Create or edit a Nbv Compilation Batch');
    await nbvCompilationBatchUpdatePage.cancel();
  });

  it('should create and save NbvCompilationBatches', async () => {
    const nbButtonsBeforeCreate = await nbvCompilationBatchComponentsPage.countDeleteButtons();

    await nbvCompilationBatchComponentsPage.clickOnCreateButton();

    await promise.all([
      nbvCompilationBatchUpdatePage.setStartIndexInput('5'),
      nbvCompilationBatchUpdatePage.setEndIndexInput('5'),
      nbvCompilationBatchUpdatePage.compilationBatchStatusSelectLastOption(),
      nbvCompilationBatchUpdatePage.setCompilationBatchIdentifierInput('64c99148-3908-465d-8c4a-e510e3ade974'),
      nbvCompilationBatchUpdatePage.setCompilationJobidentifierInput('64c99148-3908-465d-8c4a-e510e3ade974'),
      nbvCompilationBatchUpdatePage.setDepreciationPeriodIdentifierInput('64c99148-3908-465d-8c4a-e510e3ade974'),
      nbvCompilationBatchUpdatePage.setFiscalMonthIdentifierInput('64c99148-3908-465d-8c4a-e510e3ade974'),
      nbvCompilationBatchUpdatePage.setBatchSizeInput('5'),
      nbvCompilationBatchUpdatePage.setProcessedItemsInput('5'),
      nbvCompilationBatchUpdatePage.setSequenceNumberInput('5'),
      nbvCompilationBatchUpdatePage.getIsLastBatchInput().click(),
      nbvCompilationBatchUpdatePage.setProcessingTimeInput('PT12S'),
      nbvCompilationBatchUpdatePage.setTotalItemsInput('5'),
      nbvCompilationBatchUpdatePage.nbvCompilationJobSelectLastOption(),
    ]);

    await nbvCompilationBatchUpdatePage.save();
    expect(await nbvCompilationBatchUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await nbvCompilationBatchComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last NbvCompilationBatch', async () => {
    const nbButtonsBeforeDelete = await nbvCompilationBatchComponentsPage.countDeleteButtons();
    await nbvCompilationBatchComponentsPage.clickOnLastDeleteButton();

    nbvCompilationBatchDeleteDialog = new NbvCompilationBatchDeleteDialog();
    expect(await nbvCompilationBatchDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Nbv Compilation Batch?');
    await nbvCompilationBatchDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(nbvCompilationBatchComponentsPage.title), 5000);

    expect(await nbvCompilationBatchComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
