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

import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  WorkInProgressOutstandingReportRequisitionComponentsPage,
  WorkInProgressOutstandingReportRequisitionDeleteDialog,
  WorkInProgressOutstandingReportRequisitionUpdatePage,
} from './work-in-progress-outstanding-report-requisition.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('WorkInProgressOutstandingReportRequisition e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let workInProgressOutstandingReportRequisitionComponentsPage: WorkInProgressOutstandingReportRequisitionComponentsPage;
  let workInProgressOutstandingReportRequisitionUpdatePage: WorkInProgressOutstandingReportRequisitionUpdatePage;
  let workInProgressOutstandingReportRequisitionDeleteDialog: WorkInProgressOutstandingReportRequisitionDeleteDialog;
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

  it('should load WorkInProgressOutstandingReportRequisitions', async () => {
    await navBarPage.goToEntity('work-in-progress-outstanding-report-requisition');
    workInProgressOutstandingReportRequisitionComponentsPage = new WorkInProgressOutstandingReportRequisitionComponentsPage();
    await browser.wait(ec.visibilityOf(workInProgressOutstandingReportRequisitionComponentsPage.title), 5000);
    expect(await workInProgressOutstandingReportRequisitionComponentsPage.getTitle()).to.eq(
      'Work In Progress Outstanding Report Requisitions'
    );
    await browser.wait(
      ec.or(
        ec.visibilityOf(workInProgressOutstandingReportRequisitionComponentsPage.entities),
        ec.visibilityOf(workInProgressOutstandingReportRequisitionComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create WorkInProgressOutstandingReportRequisition page', async () => {
    await workInProgressOutstandingReportRequisitionComponentsPage.clickOnCreateButton();
    workInProgressOutstandingReportRequisitionUpdatePage = new WorkInProgressOutstandingReportRequisitionUpdatePage();
    expect(await workInProgressOutstandingReportRequisitionUpdatePage.getPageTitle()).to.eq(
      'Create or edit a Work In Progress Outstanding Report Requisition'
    );
    await workInProgressOutstandingReportRequisitionUpdatePage.cancel();
  });

  it('should create and save WorkInProgressOutstandingReportRequisitions', async () => {
    const nbButtonsBeforeCreate = await workInProgressOutstandingReportRequisitionComponentsPage.countDeleteButtons();

    await workInProgressOutstandingReportRequisitionComponentsPage.clickOnCreateButton();

    await promise.all([
      workInProgressOutstandingReportRequisitionUpdatePage.setRequestIdInput('64c99148-3908-465d-8c4a-e510e3ade974'),
      workInProgressOutstandingReportRequisitionUpdatePage.setReportDateInput('2000-12-31'),
      workInProgressOutstandingReportRequisitionUpdatePage.setTimeOfRequisitionInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      workInProgressOutstandingReportRequisitionUpdatePage.setFileChecksumInput('fileChecksum'),
      workInProgressOutstandingReportRequisitionUpdatePage.getTamperedInput().click(),
      workInProgressOutstandingReportRequisitionUpdatePage.setFilenameInput('64c99148-3908-465d-8c4a-e510e3ade974'),
      workInProgressOutstandingReportRequisitionUpdatePage.setReportParametersInput('reportParameters'),
      workInProgressOutstandingReportRequisitionUpdatePage.setReportFileInput(absolutePath),
      workInProgressOutstandingReportRequisitionUpdatePage.requestedBySelectLastOption(),
      workInProgressOutstandingReportRequisitionUpdatePage.lastAccessedBySelectLastOption(),
    ]);

    await workInProgressOutstandingReportRequisitionUpdatePage.save();
    expect(await workInProgressOutstandingReportRequisitionUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be
      .false;

    expect(await workInProgressOutstandingReportRequisitionComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last WorkInProgressOutstandingReportRequisition', async () => {
    const nbButtonsBeforeDelete = await workInProgressOutstandingReportRequisitionComponentsPage.countDeleteButtons();
    await workInProgressOutstandingReportRequisitionComponentsPage.clickOnLastDeleteButton();

    workInProgressOutstandingReportRequisitionDeleteDialog = new WorkInProgressOutstandingReportRequisitionDeleteDialog();
    expect(await workInProgressOutstandingReportRequisitionDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Work In Progress Outstanding Report Requisition?'
    );
    await workInProgressOutstandingReportRequisitionDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(workInProgressOutstandingReportRequisitionComponentsPage.title), 5000);

    expect(await workInProgressOutstandingReportRequisitionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
