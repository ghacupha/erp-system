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
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  AmortizationPostingReportRequisitionComponentsPage,
  /* AmortizationPostingReportRequisitionDeleteDialog, */
  AmortizationPostingReportRequisitionUpdatePage,
} from './amortization-posting-report-requisition.page-object';

const expect = chai.expect;

describe('AmortizationPostingReportRequisition e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let amortizationPostingReportRequisitionComponentsPage: AmortizationPostingReportRequisitionComponentsPage;
  let amortizationPostingReportRequisitionUpdatePage: AmortizationPostingReportRequisitionUpdatePage;
  /* let amortizationPostingReportRequisitionDeleteDialog: AmortizationPostingReportRequisitionDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load AmortizationPostingReportRequisitions', async () => {
    await navBarPage.goToEntity('amortization-posting-report-requisition');
    amortizationPostingReportRequisitionComponentsPage = new AmortizationPostingReportRequisitionComponentsPage();
    await browser.wait(ec.visibilityOf(amortizationPostingReportRequisitionComponentsPage.title), 5000);
    expect(await amortizationPostingReportRequisitionComponentsPage.getTitle()).to.eq('Amortization Posting Report Requisitions');
    await browser.wait(
      ec.or(
        ec.visibilityOf(amortizationPostingReportRequisitionComponentsPage.entities),
        ec.visibilityOf(amortizationPostingReportRequisitionComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create AmortizationPostingReportRequisition page', async () => {
    await amortizationPostingReportRequisitionComponentsPage.clickOnCreateButton();
    amortizationPostingReportRequisitionUpdatePage = new AmortizationPostingReportRequisitionUpdatePage();
    expect(await amortizationPostingReportRequisitionUpdatePage.getPageTitle()).to.eq(
      'Create or edit a Amortization Posting Report Requisition'
    );
    await amortizationPostingReportRequisitionUpdatePage.cancel();
  });

  /* it('should create and save AmortizationPostingReportRequisitions', async () => {
        const nbButtonsBeforeCreate = await amortizationPostingReportRequisitionComponentsPage.countDeleteButtons();

        await amortizationPostingReportRequisitionComponentsPage.clickOnCreateButton();

        await promise.all([
            amortizationPostingReportRequisitionUpdatePage.setRequestIdInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            amortizationPostingReportRequisitionUpdatePage.setFilenameInput('filename'),
            amortizationPostingReportRequisitionUpdatePage.amortizationPeriodSelectLastOption(),
        ]);

        await amortizationPostingReportRequisitionUpdatePage.save();
        expect(await amortizationPostingReportRequisitionUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await amortizationPostingReportRequisitionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last AmortizationPostingReportRequisition', async () => {
        const nbButtonsBeforeDelete = await amortizationPostingReportRequisitionComponentsPage.countDeleteButtons();
        await amortizationPostingReportRequisitionComponentsPage.clickOnLastDeleteButton();

        amortizationPostingReportRequisitionDeleteDialog = new AmortizationPostingReportRequisitionDeleteDialog();
        expect(await amortizationPostingReportRequisitionDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Amortization Posting Report Requisition?');
        await amortizationPostingReportRequisitionDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(amortizationPostingReportRequisitionComponentsPage.title), 5000);

        expect(await amortizationPostingReportRequisitionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
