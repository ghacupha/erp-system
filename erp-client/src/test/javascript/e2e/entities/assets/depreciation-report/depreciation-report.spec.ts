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

import { browser, ExpectedConditions as ec /* , protractor, promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  DepreciationReportComponentsPage,
  /* DepreciationReportDeleteDialog, */
  DepreciationReportUpdatePage,
} from './depreciation-report.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('DepreciationReport e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let depreciationReportComponentsPage: DepreciationReportComponentsPage;
  let depreciationReportUpdatePage: DepreciationReportUpdatePage;
  /* let depreciationReportDeleteDialog: DepreciationReportDeleteDialog; */
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

  it('should load DepreciationReports', async () => {
    await navBarPage.goToEntity('depreciation-report');
    depreciationReportComponentsPage = new DepreciationReportComponentsPage();
    await browser.wait(ec.visibilityOf(depreciationReportComponentsPage.title), 5000);
    expect(await depreciationReportComponentsPage.getTitle()).to.eq('Depreciation Reports');
    await browser.wait(
      ec.or(ec.visibilityOf(depreciationReportComponentsPage.entities), ec.visibilityOf(depreciationReportComponentsPage.noResult)),
      1000
    );
  });

  it('should load create DepreciationReport page', async () => {
    await depreciationReportComponentsPage.clickOnCreateButton();
    depreciationReportUpdatePage = new DepreciationReportUpdatePage();
    expect(await depreciationReportUpdatePage.getPageTitle()).to.eq('Create or edit a Depreciation Report');
    await depreciationReportUpdatePage.cancel();
  });

  /* it('should create and save DepreciationReports', async () => {
        const nbButtonsBeforeCreate = await depreciationReportComponentsPage.countDeleteButtons();

        await depreciationReportComponentsPage.clickOnCreateButton();

        await promise.all([
            depreciationReportUpdatePage.setReportNameInput('reportName'),
            depreciationReportUpdatePage.setTimeOfReportRequestInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            depreciationReportUpdatePage.setFileChecksumInput('fileChecksum'),
            depreciationReportUpdatePage.getTamperedInput().click(),
            depreciationReportUpdatePage.setFilenameInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            depreciationReportUpdatePage.setReportParametersInput('reportParameters'),
            depreciationReportUpdatePage.setReportFileInput(absolutePath),
            depreciationReportUpdatePage.requestedBySelectLastOption(),
            depreciationReportUpdatePage.depreciationPeriodSelectLastOption(),
            depreciationReportUpdatePage.serviceOutletSelectLastOption(),
            depreciationReportUpdatePage.assetCategorySelectLastOption(),
        ]);

        await depreciationReportUpdatePage.save();
        expect(await depreciationReportUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await depreciationReportComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last DepreciationReport', async () => {
        const nbButtonsBeforeDelete = await depreciationReportComponentsPage.countDeleteButtons();
        await depreciationReportComponentsPage.clickOnLastDeleteButton();

        depreciationReportDeleteDialog = new DepreciationReportDeleteDialog();
        expect(await depreciationReportDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Depreciation Report?');
        await depreciationReportDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(depreciationReportComponentsPage.title), 5000);

        expect(await depreciationReportComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
