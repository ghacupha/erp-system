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

import { browser, ExpectedConditions as ec /* , protractor, promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  RouAssetNBVReportComponentsPage,
  /* RouAssetNBVReportDeleteDialog, */
  RouAssetNBVReportUpdatePage,
} from './rou-asset-nbv-report.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('RouAssetNBVReport e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let rouAssetNBVReportComponentsPage: RouAssetNBVReportComponentsPage;
  let rouAssetNBVReportUpdatePage: RouAssetNBVReportUpdatePage;
  /* let rouAssetNBVReportDeleteDialog: RouAssetNBVReportDeleteDialog; */
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

  it('should load RouAssetNBVReports', async () => {
    await navBarPage.goToEntity('rou-asset-nbv-report');
    rouAssetNBVReportComponentsPage = new RouAssetNBVReportComponentsPage();
    await browser.wait(ec.visibilityOf(rouAssetNBVReportComponentsPage.title), 5000);
    expect(await rouAssetNBVReportComponentsPage.getTitle()).to.eq('Rou Asset NBV Reports');
    await browser.wait(
      ec.or(ec.visibilityOf(rouAssetNBVReportComponentsPage.entities), ec.visibilityOf(rouAssetNBVReportComponentsPage.noResult)),
      1000
    );
  });

  it('should load create RouAssetNBVReport page', async () => {
    await rouAssetNBVReportComponentsPage.clickOnCreateButton();
    rouAssetNBVReportUpdatePage = new RouAssetNBVReportUpdatePage();
    expect(await rouAssetNBVReportUpdatePage.getPageTitle()).to.eq('Create or edit a Rou Asset NBV Report');
    await rouAssetNBVReportUpdatePage.cancel();
  });

  /* it('should create and save RouAssetNBVReports', async () => {
        const nbButtonsBeforeCreate = await rouAssetNBVReportComponentsPage.countDeleteButtons();

        await rouAssetNBVReportComponentsPage.clickOnCreateButton();

        await promise.all([
            rouAssetNBVReportUpdatePage.setRequestIdInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            rouAssetNBVReportUpdatePage.setTimeOfRequestInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            rouAssetNBVReportUpdatePage.getReportIsCompiledInput().click(),
            rouAssetNBVReportUpdatePage.setFileChecksumInput('fileChecksum'),
            rouAssetNBVReportUpdatePage.getTamperedInput().click(),
            rouAssetNBVReportUpdatePage.setFilenameInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            rouAssetNBVReportUpdatePage.setReportParametersInput('reportParameters'),
            rouAssetNBVReportUpdatePage.setReportFileInput(absolutePath),
            rouAssetNBVReportUpdatePage.leasePeriodSelectLastOption(),
            rouAssetNBVReportUpdatePage.requestedBySelectLastOption(),
        ]);

        await rouAssetNBVReportUpdatePage.save();
        expect(await rouAssetNBVReportUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await rouAssetNBVReportComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last RouAssetNBVReport', async () => {
        const nbButtonsBeforeDelete = await rouAssetNBVReportComponentsPage.countDeleteButtons();
        await rouAssetNBVReportComponentsPage.clickOnLastDeleteButton();

        rouAssetNBVReportDeleteDialog = new RouAssetNBVReportDeleteDialog();
        expect(await rouAssetNBVReportDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Rou Asset NBV Report?');
        await rouAssetNBVReportDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(rouAssetNBVReportComponentsPage.title), 5000);

        expect(await rouAssetNBVReportComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
