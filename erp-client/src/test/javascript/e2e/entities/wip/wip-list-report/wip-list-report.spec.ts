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

import { WIPListReportComponentsPage, WIPListReportDeleteDialog, WIPListReportUpdatePage } from './wip-list-report.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('WIPListReport e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let wIPListReportComponentsPage: WIPListReportComponentsPage;
  let wIPListReportUpdatePage: WIPListReportUpdatePage;
  let wIPListReportDeleteDialog: WIPListReportDeleteDialog;
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

  it('should load WIPListReports', async () => {
    await navBarPage.goToEntity('wip-list-report');
    wIPListReportComponentsPage = new WIPListReportComponentsPage();
    await browser.wait(ec.visibilityOf(wIPListReportComponentsPage.title), 5000);
    expect(await wIPListReportComponentsPage.getTitle()).to.eq('WIP List Reports');
    await browser.wait(
      ec.or(ec.visibilityOf(wIPListReportComponentsPage.entities), ec.visibilityOf(wIPListReportComponentsPage.noResult)),
      1000
    );
  });

  it('should load create WIPListReport page', async () => {
    await wIPListReportComponentsPage.clickOnCreateButton();
    wIPListReportUpdatePage = new WIPListReportUpdatePage();
    expect(await wIPListReportUpdatePage.getPageTitle()).to.eq('Create or edit a WIP List Report');
    await wIPListReportUpdatePage.cancel();
  });

  it('should create and save WIPListReports', async () => {
    const nbButtonsBeforeCreate = await wIPListReportComponentsPage.countDeleteButtons();

    await wIPListReportComponentsPage.clickOnCreateButton();

    await promise.all([
      wIPListReportUpdatePage.setTimeOfRequestInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      wIPListReportUpdatePage.setRequestIdInput('64c99148-3908-465d-8c4a-e510e3ade974'),
      wIPListReportUpdatePage.setFileChecksumInput('fileChecksum'),
      wIPListReportUpdatePage.getTamperedInput().click(),
      wIPListReportUpdatePage.setFilenameInput('64c99148-3908-465d-8c4a-e510e3ade974'),
      wIPListReportUpdatePage.setReportParametersInput('reportParameters'),
      wIPListReportUpdatePage.setReportFileInput(absolutePath),
      wIPListReportUpdatePage.requestedBySelectLastOption(),
    ]);

    await wIPListReportUpdatePage.save();
    expect(await wIPListReportUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await wIPListReportComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last WIPListReport', async () => {
    const nbButtonsBeforeDelete = await wIPListReportComponentsPage.countDeleteButtons();
    await wIPListReportComponentsPage.clickOnLastDeleteButton();

    wIPListReportDeleteDialog = new WIPListReportDeleteDialog();
    expect(await wIPListReportDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this WIP List Report?');
    await wIPListReportDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(wIPListReportComponentsPage.title), 5000);

    expect(await wIPListReportComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
