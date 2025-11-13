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
  RouMonthlyDepreciationReportComponentsPage,
  /* RouMonthlyDepreciationReportDeleteDialog, */
  RouMonthlyDepreciationReportUpdatePage,
} from './rou-monthly-depreciation-report.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('RouMonthlyDepreciationReport e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let rouMonthlyDepreciationReportComponentsPage: RouMonthlyDepreciationReportComponentsPage;
  let rouMonthlyDepreciationReportUpdatePage: RouMonthlyDepreciationReportUpdatePage;
  /* let rouMonthlyDepreciationReportDeleteDialog: RouMonthlyDepreciationReportDeleteDialog; */
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

  it('should load RouMonthlyDepreciationReports', async () => {
    await navBarPage.goToEntity('rou-monthly-depreciation-report');
    rouMonthlyDepreciationReportComponentsPage = new RouMonthlyDepreciationReportComponentsPage();
    await browser.wait(ec.visibilityOf(rouMonthlyDepreciationReportComponentsPage.title), 5000);
    expect(await rouMonthlyDepreciationReportComponentsPage.getTitle()).to.eq('Rou Monthly Depreciation Reports');
    await browser.wait(
      ec.or(
        ec.visibilityOf(rouMonthlyDepreciationReportComponentsPage.entities),
        ec.visibilityOf(rouMonthlyDepreciationReportComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create RouMonthlyDepreciationReport page', async () => {
    await rouMonthlyDepreciationReportComponentsPage.clickOnCreateButton();
    rouMonthlyDepreciationReportUpdatePage = new RouMonthlyDepreciationReportUpdatePage();
    expect(await rouMonthlyDepreciationReportUpdatePage.getPageTitle()).to.eq('Create or edit a Rou Monthly Depreciation Report');
    await rouMonthlyDepreciationReportUpdatePage.cancel();
  });

  /* it('should create and save RouMonthlyDepreciationReports', async () => {
        const nbButtonsBeforeCreate = await rouMonthlyDepreciationReportComponentsPage.countDeleteButtons();

        await rouMonthlyDepreciationReportComponentsPage.clickOnCreateButton();

        await promise.all([
            rouMonthlyDepreciationReportUpdatePage.setRequestIdInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            rouMonthlyDepreciationReportUpdatePage.setTimeOfRequestInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            rouMonthlyDepreciationReportUpdatePage.getReportIsCompiledInput().click(),
            rouMonthlyDepreciationReportUpdatePage.setFileChecksumInput('fileChecksum'),
            rouMonthlyDepreciationReportUpdatePage.getTamperedInput().click(),
            rouMonthlyDepreciationReportUpdatePage.setFilenameInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            rouMonthlyDepreciationReportUpdatePage.setReportParametersInput('reportParameters'),
            rouMonthlyDepreciationReportUpdatePage.setReportFileInput(absolutePath),
            rouMonthlyDepreciationReportUpdatePage.requestedBySelectLastOption(),
            rouMonthlyDepreciationReportUpdatePage.reportingYearSelectLastOption(),
        ]);

        await rouMonthlyDepreciationReportUpdatePage.save();
        expect(await rouMonthlyDepreciationReportUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await rouMonthlyDepreciationReportComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last RouMonthlyDepreciationReport', async () => {
        const nbButtonsBeforeDelete = await rouMonthlyDepreciationReportComponentsPage.countDeleteButtons();
        await rouMonthlyDepreciationReportComponentsPage.clickOnLastDeleteButton();

        rouMonthlyDepreciationReportDeleteDialog = new RouMonthlyDepreciationReportDeleteDialog();
        expect(await rouMonthlyDepreciationReportDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Rou Monthly Depreciation Report?');
        await rouMonthlyDepreciationReportDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(rouMonthlyDepreciationReportComponentsPage.title), 5000);

        expect(await rouMonthlyDepreciationReportComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
