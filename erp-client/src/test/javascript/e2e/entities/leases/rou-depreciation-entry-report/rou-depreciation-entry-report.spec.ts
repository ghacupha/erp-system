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

import {
  RouDepreciationEntryReportComponentsPage,
  RouDepreciationEntryReportDeleteDialog,
  RouDepreciationEntryReportUpdatePage,
} from './rou-depreciation-entry-report.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('RouDepreciationEntryReport e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let rouDepreciationEntryReportComponentsPage: RouDepreciationEntryReportComponentsPage;
  let rouDepreciationEntryReportUpdatePage: RouDepreciationEntryReportUpdatePage;
  let rouDepreciationEntryReportDeleteDialog: RouDepreciationEntryReportDeleteDialog;
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

  it('should load RouDepreciationEntryReports', async () => {
    await navBarPage.goToEntity('rou-depreciation-entry-report');
    rouDepreciationEntryReportComponentsPage = new RouDepreciationEntryReportComponentsPage();
    await browser.wait(ec.visibilityOf(rouDepreciationEntryReportComponentsPage.title), 5000);
    expect(await rouDepreciationEntryReportComponentsPage.getTitle()).to.eq('Rou Depreciation Entry Reports');
    await browser.wait(
      ec.or(
        ec.visibilityOf(rouDepreciationEntryReportComponentsPage.entities),
        ec.visibilityOf(rouDepreciationEntryReportComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create RouDepreciationEntryReport page', async () => {
    await rouDepreciationEntryReportComponentsPage.clickOnCreateButton();
    rouDepreciationEntryReportUpdatePage = new RouDepreciationEntryReportUpdatePage();
    expect(await rouDepreciationEntryReportUpdatePage.getPageTitle()).to.eq('Create or edit a Rou Depreciation Entry Report');
    await rouDepreciationEntryReportUpdatePage.cancel();
  });

  it('should create and save RouDepreciationEntryReports', async () => {
    const nbButtonsBeforeCreate = await rouDepreciationEntryReportComponentsPage.countDeleteButtons();

    await rouDepreciationEntryReportComponentsPage.clickOnCreateButton();

    await promise.all([
      rouDepreciationEntryReportUpdatePage.setRequestIdInput('64c99148-3908-465d-8c4a-e510e3ade974'),
      rouDepreciationEntryReportUpdatePage.setTimeOfRequestInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      rouDepreciationEntryReportUpdatePage.getReportIsCompiledInput().click(),
      rouDepreciationEntryReportUpdatePage.setPeriodStartDateInput('2000-12-31'),
      rouDepreciationEntryReportUpdatePage.setPeriodEndDateInput('2000-12-31'),
      rouDepreciationEntryReportUpdatePage.setFileChecksumInput('fileChecksum'),
      rouDepreciationEntryReportUpdatePage.getTamperedInput().click(),
      rouDepreciationEntryReportUpdatePage.setFilenameInput('64c99148-3908-465d-8c4a-e510e3ade974'),
      rouDepreciationEntryReportUpdatePage.setReportParametersInput('reportParameters'),
      rouDepreciationEntryReportUpdatePage.setReportFileInput(absolutePath),
      rouDepreciationEntryReportUpdatePage.requestedBySelectLastOption(),
    ]);

    await rouDepreciationEntryReportUpdatePage.save();
    expect(await rouDepreciationEntryReportUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await rouDepreciationEntryReportComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last RouDepreciationEntryReport', async () => {
    const nbButtonsBeforeDelete = await rouDepreciationEntryReportComponentsPage.countDeleteButtons();
    await rouDepreciationEntryReportComponentsPage.clickOnLastDeleteButton();

    rouDepreciationEntryReportDeleteDialog = new RouDepreciationEntryReportDeleteDialog();
    expect(await rouDepreciationEntryReportDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Rou Depreciation Entry Report?'
    );
    await rouDepreciationEntryReportDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(rouDepreciationEntryReportComponentsPage.title), 5000);

    expect(await rouDepreciationEntryReportComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
