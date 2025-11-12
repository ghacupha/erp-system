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
  LeaseLiabilityScheduleReportComponentsPage,
  /* LeaseLiabilityScheduleReportDeleteDialog, */
  LeaseLiabilityScheduleReportUpdatePage,
} from './lease-liability-schedule-report.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('LeaseLiabilityScheduleReport e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let leaseLiabilityScheduleReportComponentsPage: LeaseLiabilityScheduleReportComponentsPage;
  let leaseLiabilityScheduleReportUpdatePage: LeaseLiabilityScheduleReportUpdatePage;
  /* let leaseLiabilityScheduleReportDeleteDialog: LeaseLiabilityScheduleReportDeleteDialog; */
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

  it('should load LeaseLiabilityScheduleReports', async () => {
    await navBarPage.goToEntity('lease-liability-schedule-report');
    leaseLiabilityScheduleReportComponentsPage = new LeaseLiabilityScheduleReportComponentsPage();
    await browser.wait(ec.visibilityOf(leaseLiabilityScheduleReportComponentsPage.title), 5000);
    expect(await leaseLiabilityScheduleReportComponentsPage.getTitle()).to.eq('Lease Liability Schedule Reports');
    await browser.wait(
      ec.or(
        ec.visibilityOf(leaseLiabilityScheduleReportComponentsPage.entities),
        ec.visibilityOf(leaseLiabilityScheduleReportComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create LeaseLiabilityScheduleReport page', async () => {
    await leaseLiabilityScheduleReportComponentsPage.clickOnCreateButton();
    leaseLiabilityScheduleReportUpdatePage = new LeaseLiabilityScheduleReportUpdatePage();
    expect(await leaseLiabilityScheduleReportUpdatePage.getPageTitle()).to.eq('Create or edit a Lease Liability Schedule Report');
    await leaseLiabilityScheduleReportUpdatePage.cancel();
  });

  /* it('should create and save LeaseLiabilityScheduleReports', async () => {
        const nbButtonsBeforeCreate = await leaseLiabilityScheduleReportComponentsPage.countDeleteButtons();

        await leaseLiabilityScheduleReportComponentsPage.clickOnCreateButton();

        await promise.all([
            leaseLiabilityScheduleReportUpdatePage.setRequestIdInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            leaseLiabilityScheduleReportUpdatePage.setTimeOfRequestInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            leaseLiabilityScheduleReportUpdatePage.setFileChecksumInput('fileChecksum'),
            leaseLiabilityScheduleReportUpdatePage.getTamperedInput().click(),
            leaseLiabilityScheduleReportUpdatePage.setFilenameInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            leaseLiabilityScheduleReportUpdatePage.setReportParametersInput('reportParameters'),
            leaseLiabilityScheduleReportUpdatePage.setReportFileInput(absolutePath),
            leaseLiabilityScheduleReportUpdatePage.requestedBySelectLastOption(),
            leaseLiabilityScheduleReportUpdatePage.leaseAmortizationScheduleSelectLastOption(),
        ]);

        await leaseLiabilityScheduleReportUpdatePage.save();
        expect(await leaseLiabilityScheduleReportUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await leaseLiabilityScheduleReportComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last LeaseLiabilityScheduleReport', async () => {
        const nbButtonsBeforeDelete = await leaseLiabilityScheduleReportComponentsPage.countDeleteButtons();
        await leaseLiabilityScheduleReportComponentsPage.clickOnLastDeleteButton();

        leaseLiabilityScheduleReportDeleteDialog = new LeaseLiabilityScheduleReportDeleteDialog();
        expect(await leaseLiabilityScheduleReportDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Lease Liability Schedule Report?');
        await leaseLiabilityScheduleReportDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(leaseLiabilityScheduleReportComponentsPage.title), 5000);

        expect(await leaseLiabilityScheduleReportComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
