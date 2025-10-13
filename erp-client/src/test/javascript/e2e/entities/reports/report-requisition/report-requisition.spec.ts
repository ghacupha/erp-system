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
  ReportRequisitionComponentsPage,
  /* ReportRequisitionDeleteDialog, */
  ReportRequisitionUpdatePage,
} from './report-requisition.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('ReportRequisition e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let reportRequisitionComponentsPage: ReportRequisitionComponentsPage;
  let reportRequisitionUpdatePage: ReportRequisitionUpdatePage;
  /* let reportRequisitionDeleteDialog: ReportRequisitionDeleteDialog; */
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

  it('should load ReportRequisitions', async () => {
    await navBarPage.goToEntity('report-requisition');
    reportRequisitionComponentsPage = new ReportRequisitionComponentsPage();
    await browser.wait(ec.visibilityOf(reportRequisitionComponentsPage.title), 5000);
    expect(await reportRequisitionComponentsPage.getTitle()).to.eq('Report Requisitions');
    await browser.wait(
      ec.or(ec.visibilityOf(reportRequisitionComponentsPage.entities), ec.visibilityOf(reportRequisitionComponentsPage.noResult)),
      1000
    );
  });

  it('should load create ReportRequisition page', async () => {
    await reportRequisitionComponentsPage.clickOnCreateButton();
    reportRequisitionUpdatePage = new ReportRequisitionUpdatePage();
    expect(await reportRequisitionUpdatePage.getPageTitle()).to.eq('Create or edit a Report Requisition');
    await reportRequisitionUpdatePage.cancel();
  });

  /* it('should create and save ReportRequisitions', async () => {
        const nbButtonsBeforeCreate = await reportRequisitionComponentsPage.countDeleteButtons();

        await reportRequisitionComponentsPage.clickOnCreateButton();

        await promise.all([
            reportRequisitionUpdatePage.setReportNameInput('reportName'),
            reportRequisitionUpdatePage.setReportRequestTimeInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            reportRequisitionUpdatePage.setReportPasswordInput('reportPassword'),
            reportRequisitionUpdatePage.reportStatusSelectLastOption(),
            reportRequisitionUpdatePage.setReportIdInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            reportRequisitionUpdatePage.setReportFileAttachmentInput(absolutePath),
            reportRequisitionUpdatePage.setReportFileCheckSumInput('reportFileCheckSum'),
            reportRequisitionUpdatePage.setReportNotesInput(absolutePath),
            // reportRequisitionUpdatePage.placeholdersSelectLastOption(),
            // reportRequisitionUpdatePage.parametersSelectLastOption(),
            reportRequisitionUpdatePage.reportTemplateSelectLastOption(),
            reportRequisitionUpdatePage.reportContentTypeSelectLastOption(),
        ]);

        await reportRequisitionUpdatePage.save();
        expect(await reportRequisitionUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await reportRequisitionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last ReportRequisition', async () => {
        const nbButtonsBeforeDelete = await reportRequisitionComponentsPage.countDeleteButtons();
        await reportRequisitionComponentsPage.clickOnLastDeleteButton();

        reportRequisitionDeleteDialog = new ReportRequisitionDeleteDialog();
        expect(await reportRequisitionDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Report Requisition?');
        await reportRequisitionDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(reportRequisitionComponentsPage.title), 5000);

        expect(await reportRequisitionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
