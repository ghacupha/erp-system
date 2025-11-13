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
  WIPTransferListReportComponentsPage,
  WIPTransferListReportDeleteDialog,
  WIPTransferListReportUpdatePage,
} from './wip-transfer-list-report.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('WIPTransferListReport e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let wIPTransferListReportComponentsPage: WIPTransferListReportComponentsPage;
  let wIPTransferListReportUpdatePage: WIPTransferListReportUpdatePage;
  let wIPTransferListReportDeleteDialog: WIPTransferListReportDeleteDialog;
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

  it('should load WIPTransferListReports', async () => {
    await navBarPage.goToEntity('wip-transfer-list-report');
    wIPTransferListReportComponentsPage = new WIPTransferListReportComponentsPage();
    await browser.wait(ec.visibilityOf(wIPTransferListReportComponentsPage.title), 5000);
    expect(await wIPTransferListReportComponentsPage.getTitle()).to.eq('WIP Transfer List Reports');
    await browser.wait(
      ec.or(ec.visibilityOf(wIPTransferListReportComponentsPage.entities), ec.visibilityOf(wIPTransferListReportComponentsPage.noResult)),
      1000
    );
  });

  it('should load create WIPTransferListReport page', async () => {
    await wIPTransferListReportComponentsPage.clickOnCreateButton();
    wIPTransferListReportUpdatePage = new WIPTransferListReportUpdatePage();
    expect(await wIPTransferListReportUpdatePage.getPageTitle()).to.eq('Create or edit a WIP Transfer List Report');
    await wIPTransferListReportUpdatePage.cancel();
  });

  it('should create and save WIPTransferListReports', async () => {
    const nbButtonsBeforeCreate = await wIPTransferListReportComponentsPage.countDeleteButtons();

    await wIPTransferListReportComponentsPage.clickOnCreateButton();

    await promise.all([
      wIPTransferListReportUpdatePage.setTimeOfRequestInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      wIPTransferListReportUpdatePage.setRequestIdInput('64c99148-3908-465d-8c4a-e510e3ade974'),
      wIPTransferListReportUpdatePage.setFileChecksumInput('fileChecksum'),
      wIPTransferListReportUpdatePage.getTemperedInput().click(),
      wIPTransferListReportUpdatePage.setFilenameInput('64c99148-3908-465d-8c4a-e510e3ade974'),
      wIPTransferListReportUpdatePage.setReportParametersInput('reportParameters'),
      wIPTransferListReportUpdatePage.setReportFileInput(absolutePath),
      wIPTransferListReportUpdatePage.requestedBySelectLastOption(),
    ]);

    await wIPTransferListReportUpdatePage.save();
    expect(await wIPTransferListReportUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await wIPTransferListReportComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last WIPTransferListReport', async () => {
    const nbButtonsBeforeDelete = await wIPTransferListReportComponentsPage.countDeleteButtons();
    await wIPTransferListReportComponentsPage.clickOnLastDeleteButton();

    wIPTransferListReportDeleteDialog = new WIPTransferListReportDeleteDialog();
    expect(await wIPTransferListReportDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this WIP Transfer List Report?'
    );
    await wIPTransferListReportDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(wIPTransferListReportComponentsPage.title), 5000);

    expect(await wIPTransferListReportComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
