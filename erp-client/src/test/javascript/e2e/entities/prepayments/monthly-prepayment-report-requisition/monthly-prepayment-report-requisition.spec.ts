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
  MonthlyPrepaymentReportRequisitionComponentsPage,
  /* MonthlyPrepaymentReportRequisitionDeleteDialog, */
  MonthlyPrepaymentReportRequisitionUpdatePage,
} from './monthly-prepayment-report-requisition.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('MonthlyPrepaymentReportRequisition e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let monthlyPrepaymentReportRequisitionComponentsPage: MonthlyPrepaymentReportRequisitionComponentsPage;
  let monthlyPrepaymentReportRequisitionUpdatePage: MonthlyPrepaymentReportRequisitionUpdatePage;
  /* let monthlyPrepaymentReportRequisitionDeleteDialog: MonthlyPrepaymentReportRequisitionDeleteDialog; */
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

  it('should load MonthlyPrepaymentReportRequisitions', async () => {
    await navBarPage.goToEntity('monthly-prepayment-report-requisition');
    monthlyPrepaymentReportRequisitionComponentsPage = new MonthlyPrepaymentReportRequisitionComponentsPage();
    await browser.wait(ec.visibilityOf(monthlyPrepaymentReportRequisitionComponentsPage.title), 5000);
    expect(await monthlyPrepaymentReportRequisitionComponentsPage.getTitle()).to.eq('Monthly Prepayment Report Requisitions');
    await browser.wait(
      ec.or(
        ec.visibilityOf(monthlyPrepaymentReportRequisitionComponentsPage.entities),
        ec.visibilityOf(monthlyPrepaymentReportRequisitionComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create MonthlyPrepaymentReportRequisition page', async () => {
    await monthlyPrepaymentReportRequisitionComponentsPage.clickOnCreateButton();
    monthlyPrepaymentReportRequisitionUpdatePage = new MonthlyPrepaymentReportRequisitionUpdatePage();
    expect(await monthlyPrepaymentReportRequisitionUpdatePage.getPageTitle()).to.eq(
      'Create or edit a Monthly Prepayment Report Requisition'
    );
    await monthlyPrepaymentReportRequisitionUpdatePage.cancel();
  });

  /* it('should create and save MonthlyPrepaymentReportRequisitions', async () => {
        const nbButtonsBeforeCreate = await monthlyPrepaymentReportRequisitionComponentsPage.countDeleteButtons();

        await monthlyPrepaymentReportRequisitionComponentsPage.clickOnCreateButton();

        await promise.all([
            monthlyPrepaymentReportRequisitionUpdatePage.setRequestIdInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            monthlyPrepaymentReportRequisitionUpdatePage.setTimeOfRequisitionInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            monthlyPrepaymentReportRequisitionUpdatePage.setFileChecksumInput('fileChecksum'),
            monthlyPrepaymentReportRequisitionUpdatePage.setFilenameInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            monthlyPrepaymentReportRequisitionUpdatePage.setReportParametersInput('reportParameters'),
            monthlyPrepaymentReportRequisitionUpdatePage.setReportFileInput(absolutePath),
            monthlyPrepaymentReportRequisitionUpdatePage.getTamperedInput().click(),
            monthlyPrepaymentReportRequisitionUpdatePage.fiscalYearSelectLastOption(),
        ]);

        await monthlyPrepaymentReportRequisitionUpdatePage.save();
        expect(await monthlyPrepaymentReportRequisitionUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await monthlyPrepaymentReportRequisitionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last MonthlyPrepaymentReportRequisition', async () => {
        const nbButtonsBeforeDelete = await monthlyPrepaymentReportRequisitionComponentsPage.countDeleteButtons();
        await monthlyPrepaymentReportRequisitionComponentsPage.clickOnLastDeleteButton();

        monthlyPrepaymentReportRequisitionDeleteDialog = new MonthlyPrepaymentReportRequisitionDeleteDialog();
        expect(await monthlyPrepaymentReportRequisitionDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Monthly Prepayment Report Requisition?');
        await monthlyPrepaymentReportRequisitionDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(monthlyPrepaymentReportRequisitionComponentsPage.title), 5000);

        expect(await monthlyPrepaymentReportRequisitionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
