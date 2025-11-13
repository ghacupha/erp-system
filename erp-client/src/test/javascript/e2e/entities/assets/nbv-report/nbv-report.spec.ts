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
  NbvReportComponentsPage,
  /* NbvReportDeleteDialog, */
  NbvReportUpdatePage,
} from './nbv-report.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('NbvReport e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let nbvReportComponentsPage: NbvReportComponentsPage;
  let nbvReportUpdatePage: NbvReportUpdatePage;
  /* let nbvReportDeleteDialog: NbvReportDeleteDialog; */
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

  it('should load NbvReports', async () => {
    await navBarPage.goToEntity('nbv-report');
    nbvReportComponentsPage = new NbvReportComponentsPage();
    await browser.wait(ec.visibilityOf(nbvReportComponentsPage.title), 5000);
    expect(await nbvReportComponentsPage.getTitle()).to.eq('Nbv Reports');
    await browser.wait(ec.or(ec.visibilityOf(nbvReportComponentsPage.entities), ec.visibilityOf(nbvReportComponentsPage.noResult)), 1000);
  });

  it('should load create NbvReport page', async () => {
    await nbvReportComponentsPage.clickOnCreateButton();
    nbvReportUpdatePage = new NbvReportUpdatePage();
    expect(await nbvReportUpdatePage.getPageTitle()).to.eq('Create or edit a Nbv Report');
    await nbvReportUpdatePage.cancel();
  });

  /* it('should create and save NbvReports', async () => {
        const nbButtonsBeforeCreate = await nbvReportComponentsPage.countDeleteButtons();

        await nbvReportComponentsPage.clickOnCreateButton();

        await promise.all([
            nbvReportUpdatePage.setReportNameInput('reportName'),
            nbvReportUpdatePage.setTimeOfReportRequestInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            nbvReportUpdatePage.setFileChecksumInput('fileChecksum'),
            nbvReportUpdatePage.getTamperedInput().click(),
            nbvReportUpdatePage.setFilenameInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            nbvReportUpdatePage.setReportParametersInput('reportParameters'),
            nbvReportUpdatePage.setReportFileInput(absolutePath),
            nbvReportUpdatePage.requestedBySelectLastOption(),
            nbvReportUpdatePage.depreciationPeriodSelectLastOption(),
            nbvReportUpdatePage.serviceOutletSelectLastOption(),
            nbvReportUpdatePage.assetCategorySelectLastOption(),
        ]);

        await nbvReportUpdatePage.save();
        expect(await nbvReportUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await nbvReportComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last NbvReport', async () => {
        const nbButtonsBeforeDelete = await nbvReportComponentsPage.countDeleteButtons();
        await nbvReportComponentsPage.clickOnLastDeleteButton();

        nbvReportDeleteDialog = new NbvReportDeleteDialog();
        expect(await nbvReportDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Nbv Report?');
        await nbvReportDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(nbvReportComponentsPage.title), 5000);

        expect(await nbvReportComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
