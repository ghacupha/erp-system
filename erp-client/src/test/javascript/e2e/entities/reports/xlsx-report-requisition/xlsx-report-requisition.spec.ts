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

import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  XlsxReportRequisitionComponentsPage,
  /* XlsxReportRequisitionDeleteDialog, */
  XlsxReportRequisitionUpdatePage,
} from './xlsx-report-requisition.page-object';

const expect = chai.expect;

describe('XlsxReportRequisition e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let xlsxReportRequisitionComponentsPage: XlsxReportRequisitionComponentsPage;
  let xlsxReportRequisitionUpdatePage: XlsxReportRequisitionUpdatePage;
  /* let xlsxReportRequisitionDeleteDialog: XlsxReportRequisitionDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load XlsxReportRequisitions', async () => {
    await navBarPage.goToEntity('xlsx-report-requisition');
    xlsxReportRequisitionComponentsPage = new XlsxReportRequisitionComponentsPage();
    await browser.wait(ec.visibilityOf(xlsxReportRequisitionComponentsPage.title), 5000);
    expect(await xlsxReportRequisitionComponentsPage.getTitle()).to.eq('Xlsx Report Requisitions');
    await browser.wait(
      ec.or(ec.visibilityOf(xlsxReportRequisitionComponentsPage.entities), ec.visibilityOf(xlsxReportRequisitionComponentsPage.noResult)),
      1000
    );
  });

  it('should load create XlsxReportRequisition page', async () => {
    await xlsxReportRequisitionComponentsPage.clickOnCreateButton();
    xlsxReportRequisitionUpdatePage = new XlsxReportRequisitionUpdatePage();
    expect(await xlsxReportRequisitionUpdatePage.getPageTitle()).to.eq('Create or edit a Xlsx Report Requisition');
    await xlsxReportRequisitionUpdatePage.cancel();
  });

  /* it('should create and save XlsxReportRequisitions', async () => {
        const nbButtonsBeforeCreate = await xlsxReportRequisitionComponentsPage.countDeleteButtons();

        await xlsxReportRequisitionComponentsPage.clickOnCreateButton();

        await promise.all([
            xlsxReportRequisitionUpdatePage.setReportNameInput('reportName'),
            xlsxReportRequisitionUpdatePage.setReportDateInput('2000-12-31'),
            xlsxReportRequisitionUpdatePage.setUserPasswordInput('userPassword'),
            xlsxReportRequisitionUpdatePage.setReportFileChecksumInput('reportFileChecksum'),
            xlsxReportRequisitionUpdatePage.reportStatusSelectLastOption(),
            xlsxReportRequisitionUpdatePage.setReportIdInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            xlsxReportRequisitionUpdatePage.reportTemplateSelectLastOption(),
            // xlsxReportRequisitionUpdatePage.placeholderSelectLastOption(),
            // xlsxReportRequisitionUpdatePage.parametersSelectLastOption(),
        ]);

        await xlsxReportRequisitionUpdatePage.save();
        expect(await xlsxReportRequisitionUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await xlsxReportRequisitionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last XlsxReportRequisition', async () => {
        const nbButtonsBeforeDelete = await xlsxReportRequisitionComponentsPage.countDeleteButtons();
        await xlsxReportRequisitionComponentsPage.clickOnLastDeleteButton();

        xlsxReportRequisitionDeleteDialog = new XlsxReportRequisitionDeleteDialog();
        expect(await xlsxReportRequisitionDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Xlsx Report Requisition?');
        await xlsxReportRequisitionDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(xlsxReportRequisitionComponentsPage.title), 5000);

        expect(await xlsxReportRequisitionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
