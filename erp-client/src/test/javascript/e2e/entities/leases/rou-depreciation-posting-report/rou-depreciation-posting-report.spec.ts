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

import { browser, ExpectedConditions as ec /* , protractor, promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  RouDepreciationPostingReportComponentsPage,
  /* RouDepreciationPostingReportDeleteDialog, */
  RouDepreciationPostingReportUpdatePage,
} from './rou-depreciation-posting-report.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('RouDepreciationPostingReport e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let rouDepreciationPostingReportComponentsPage: RouDepreciationPostingReportComponentsPage;
  let rouDepreciationPostingReportUpdatePage: RouDepreciationPostingReportUpdatePage;
  /* let rouDepreciationPostingReportDeleteDialog: RouDepreciationPostingReportDeleteDialog; */
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

  it('should load RouDepreciationPostingReports', async () => {
    await navBarPage.goToEntity('rou-depreciation-posting-report');
    rouDepreciationPostingReportComponentsPage = new RouDepreciationPostingReportComponentsPage();
    await browser.wait(ec.visibilityOf(rouDepreciationPostingReportComponentsPage.title), 5000);
    expect(await rouDepreciationPostingReportComponentsPage.getTitle()).to.eq('Rou Depreciation Posting Reports');
    await browser.wait(
      ec.or(
        ec.visibilityOf(rouDepreciationPostingReportComponentsPage.entities),
        ec.visibilityOf(rouDepreciationPostingReportComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create RouDepreciationPostingReport page', async () => {
    await rouDepreciationPostingReportComponentsPage.clickOnCreateButton();
    rouDepreciationPostingReportUpdatePage = new RouDepreciationPostingReportUpdatePage();
    expect(await rouDepreciationPostingReportUpdatePage.getPageTitle()).to.eq('Create or edit a Rou Depreciation Posting Report');
    await rouDepreciationPostingReportUpdatePage.cancel();
  });

  /* it('should create and save RouDepreciationPostingReports', async () => {
        const nbButtonsBeforeCreate = await rouDepreciationPostingReportComponentsPage.countDeleteButtons();

        await rouDepreciationPostingReportComponentsPage.clickOnCreateButton();

        await promise.all([
            rouDepreciationPostingReportUpdatePage.setRequestIdInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            rouDepreciationPostingReportUpdatePage.setTimeOfRequestInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            rouDepreciationPostingReportUpdatePage.getReportIsCompiledInput().click(),
            rouDepreciationPostingReportUpdatePage.setFileChecksumInput('fileChecksum'),
            rouDepreciationPostingReportUpdatePage.getTamperedInput().click(),
            rouDepreciationPostingReportUpdatePage.setFilenameInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            rouDepreciationPostingReportUpdatePage.setReportParametersInput('reportParameters'),
            rouDepreciationPostingReportUpdatePage.setReportFileInput(absolutePath),
            rouDepreciationPostingReportUpdatePage.leasePeriodSelectLastOption(),
            rouDepreciationPostingReportUpdatePage.requestedBySelectLastOption(),
        ]);

        await rouDepreciationPostingReportUpdatePage.save();
        expect(await rouDepreciationPostingReportUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await rouDepreciationPostingReportComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last RouDepreciationPostingReport', async () => {
        const nbButtonsBeforeDelete = await rouDepreciationPostingReportComponentsPage.countDeleteButtons();
        await rouDepreciationPostingReportComponentsPage.clickOnLastDeleteButton();

        rouDepreciationPostingReportDeleteDialog = new RouDepreciationPostingReportDeleteDialog();
        expect(await rouDepreciationPostingReportDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Rou Depreciation Posting Report?');
        await rouDepreciationPostingReportDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(rouDepreciationPostingReportComponentsPage.title), 5000);

        expect(await rouDepreciationPostingReportComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
