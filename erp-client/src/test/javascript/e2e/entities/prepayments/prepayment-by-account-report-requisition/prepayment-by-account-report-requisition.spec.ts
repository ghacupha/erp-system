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
  PrepaymentByAccountReportRequisitionComponentsPage,
  PrepaymentByAccountReportRequisitionDeleteDialog,
  PrepaymentByAccountReportRequisitionUpdatePage,
} from './prepayment-by-account-report-requisition.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('PrepaymentByAccountReportRequisition e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let prepaymentByAccountReportRequisitionComponentsPage: PrepaymentByAccountReportRequisitionComponentsPage;
  let prepaymentByAccountReportRequisitionUpdatePage: PrepaymentByAccountReportRequisitionUpdatePage;
  let prepaymentByAccountReportRequisitionDeleteDialog: PrepaymentByAccountReportRequisitionDeleteDialog;
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

  it('should load PrepaymentByAccountReportRequisitions', async () => {
    await navBarPage.goToEntity('prepayment-by-account-report-requisition');
    prepaymentByAccountReportRequisitionComponentsPage = new PrepaymentByAccountReportRequisitionComponentsPage();
    await browser.wait(ec.visibilityOf(prepaymentByAccountReportRequisitionComponentsPage.title), 5000);
    expect(await prepaymentByAccountReportRequisitionComponentsPage.getTitle()).to.eq('Prepayment By Account Report Requisitions');
    await browser.wait(
      ec.or(
        ec.visibilityOf(prepaymentByAccountReportRequisitionComponentsPage.entities),
        ec.visibilityOf(prepaymentByAccountReportRequisitionComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create PrepaymentByAccountReportRequisition page', async () => {
    await prepaymentByAccountReportRequisitionComponentsPage.clickOnCreateButton();
    prepaymentByAccountReportRequisitionUpdatePage = new PrepaymentByAccountReportRequisitionUpdatePage();
    expect(await prepaymentByAccountReportRequisitionUpdatePage.getPageTitle()).to.eq(
      'Create or edit a Prepayment By Account Report Requisition'
    );
    await prepaymentByAccountReportRequisitionUpdatePage.cancel();
  });

  it('should create and save PrepaymentByAccountReportRequisitions', async () => {
    const nbButtonsBeforeCreate = await prepaymentByAccountReportRequisitionComponentsPage.countDeleteButtons();

    await prepaymentByAccountReportRequisitionComponentsPage.clickOnCreateButton();

    await promise.all([
      prepaymentByAccountReportRequisitionUpdatePage.setRequestIdInput('64c99148-3908-465d-8c4a-e510e3ade974'),
      prepaymentByAccountReportRequisitionUpdatePage.setTimeOfRequisitionInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      prepaymentByAccountReportRequisitionUpdatePage.setFileChecksumInput('fileChecksum'),
      prepaymentByAccountReportRequisitionUpdatePage.setFilenameInput('64c99148-3908-465d-8c4a-e510e3ade974'),
      prepaymentByAccountReportRequisitionUpdatePage.setReportParametersInput('reportParameters'),
      prepaymentByAccountReportRequisitionUpdatePage.setReportFileInput(absolutePath),
      prepaymentByAccountReportRequisitionUpdatePage.setReportDateInput('2000-12-31'),
      prepaymentByAccountReportRequisitionUpdatePage.getTamperedInput().click(),
      prepaymentByAccountReportRequisitionUpdatePage.requestedBySelectLastOption(),
      prepaymentByAccountReportRequisitionUpdatePage.lastAccessedBySelectLastOption(),
    ]);

    await prepaymentByAccountReportRequisitionUpdatePage.save();
    expect(await prepaymentByAccountReportRequisitionUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await prepaymentByAccountReportRequisitionComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last PrepaymentByAccountReportRequisition', async () => {
    const nbButtonsBeforeDelete = await prepaymentByAccountReportRequisitionComponentsPage.countDeleteButtons();
    await prepaymentByAccountReportRequisitionComponentsPage.clickOnLastDeleteButton();

    prepaymentByAccountReportRequisitionDeleteDialog = new PrepaymentByAccountReportRequisitionDeleteDialog();
    expect(await prepaymentByAccountReportRequisitionDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Prepayment By Account Report Requisition?'
    );
    await prepaymentByAccountReportRequisitionDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(prepaymentByAccountReportRequisitionComponentsPage.title), 5000);

    expect(await prepaymentByAccountReportRequisitionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
