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

import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  GdiTransactionDataIndexComponentsPage,
  GdiTransactionDataIndexDeleteDialog,
  GdiTransactionDataIndexUpdatePage,
} from './gdi-transaction-data-index.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('GdiTransactionDataIndex e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let gdiTransactionDataIndexComponentsPage: GdiTransactionDataIndexComponentsPage;
  let gdiTransactionDataIndexUpdatePage: GdiTransactionDataIndexUpdatePage;
  let gdiTransactionDataIndexDeleteDialog: GdiTransactionDataIndexDeleteDialog;
  const fileNameToUpload = 'logo-jhipster.png';
  const fileToUpload = '../../../../../../../src/main/webapp/content/images/' + fileNameToUpload;
  const absolutePath = path.resolve(__dirname, fileToUpload);
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load GdiTransactionDataIndices', async () => {
    await navBarPage.goToEntity('gdi-transaction-data-index');
    gdiTransactionDataIndexComponentsPage = new GdiTransactionDataIndexComponentsPage();
    await browser.wait(ec.visibilityOf(gdiTransactionDataIndexComponentsPage.title), 5000);
    expect(await gdiTransactionDataIndexComponentsPage.getTitle()).to.eq('Gdi Transaction Data Indices');
    await browser.wait(
      ec.or(
        ec.visibilityOf(gdiTransactionDataIndexComponentsPage.entities),
        ec.visibilityOf(gdiTransactionDataIndexComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create GdiTransactionDataIndex page', async () => {
    await gdiTransactionDataIndexComponentsPage.clickOnCreateButton();
    gdiTransactionDataIndexUpdatePage = new GdiTransactionDataIndexUpdatePage();
    expect(await gdiTransactionDataIndexUpdatePage.getPageTitle()).to.eq('Create or edit a Gdi Transaction Data Index');
    await gdiTransactionDataIndexUpdatePage.cancel();
  });

  it('should create and save GdiTransactionDataIndices', async () => {
    const nbButtonsBeforeCreate = await gdiTransactionDataIndexComponentsPage.countDeleteButtons();

    await gdiTransactionDataIndexComponentsPage.clickOnCreateButton();

    await promise.all([
      gdiTransactionDataIndexUpdatePage.setDatasetNameInput('datasetName'),
      gdiTransactionDataIndexUpdatePage.setDatabaseNameInput('databaseName'),
      gdiTransactionDataIndexUpdatePage.updateFrequencySelectLastOption(),
      gdiTransactionDataIndexUpdatePage.datasetBehaviorSelectLastOption(),
      gdiTransactionDataIndexUpdatePage.setMinimumDatarowsPerRequestInput('5'),
      gdiTransactionDataIndexUpdatePage.setMaximumDataRowsPerRequestInput('5'),
      gdiTransactionDataIndexUpdatePage.setDatasetDescriptionInput('datasetDescription'),
      gdiTransactionDataIndexUpdatePage.setDataTemplateInput(absolutePath),
      // gdiTransactionDataIndexUpdatePage.masterDataItemSelectLastOption(),
    ]);

    await gdiTransactionDataIndexUpdatePage.save();
    expect(await gdiTransactionDataIndexUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await gdiTransactionDataIndexComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last GdiTransactionDataIndex', async () => {
    const nbButtonsBeforeDelete = await gdiTransactionDataIndexComponentsPage.countDeleteButtons();
    await gdiTransactionDataIndexComponentsPage.clickOnLastDeleteButton();

    gdiTransactionDataIndexDeleteDialog = new GdiTransactionDataIndexDeleteDialog();
    expect(await gdiTransactionDataIndexDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Gdi Transaction Data Index?'
    );
    await gdiTransactionDataIndexDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(gdiTransactionDataIndexComponentsPage.title), 5000);

    expect(await gdiTransactionDataIndexComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
