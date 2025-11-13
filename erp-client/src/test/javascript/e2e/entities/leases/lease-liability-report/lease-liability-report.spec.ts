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
  LeaseLiabilityReportComponentsPage,
  /* LeaseLiabilityReportDeleteDialog, */
  LeaseLiabilityReportUpdatePage,
} from './lease-liability-report.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('LeaseLiabilityReport e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let leaseLiabilityReportComponentsPage: LeaseLiabilityReportComponentsPage;
  let leaseLiabilityReportUpdatePage: LeaseLiabilityReportUpdatePage;
  /* let leaseLiabilityReportDeleteDialog: LeaseLiabilityReportDeleteDialog; */
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

  it('should load LeaseLiabilityReports', async () => {
    await navBarPage.goToEntity('lease-liability-report');
    leaseLiabilityReportComponentsPage = new LeaseLiabilityReportComponentsPage();
    await browser.wait(ec.visibilityOf(leaseLiabilityReportComponentsPage.title), 5000);
    expect(await leaseLiabilityReportComponentsPage.getTitle()).to.eq('Lease Liability Reports');
    await browser.wait(
      ec.or(ec.visibilityOf(leaseLiabilityReportComponentsPage.entities), ec.visibilityOf(leaseLiabilityReportComponentsPage.noResult)),
      1000
    );
  });

  it('should load create LeaseLiabilityReport page', async () => {
    await leaseLiabilityReportComponentsPage.clickOnCreateButton();
    leaseLiabilityReportUpdatePage = new LeaseLiabilityReportUpdatePage();
    expect(await leaseLiabilityReportUpdatePage.getPageTitle()).to.eq('Create or edit a Lease Liability Report');
    await leaseLiabilityReportUpdatePage.cancel();
  });

  /* it('should create and save LeaseLiabilityReports', async () => {
        const nbButtonsBeforeCreate = await leaseLiabilityReportComponentsPage.countDeleteButtons();

        await leaseLiabilityReportComponentsPage.clickOnCreateButton();

        await promise.all([
            leaseLiabilityReportUpdatePage.setRequestIdInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            leaseLiabilityReportUpdatePage.setTimeOfRequestInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            leaseLiabilityReportUpdatePage.setFileChecksumInput('fileChecksum'),
            leaseLiabilityReportUpdatePage.getTamperedInput().click(),
            leaseLiabilityReportUpdatePage.setFilenameInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            leaseLiabilityReportUpdatePage.setReportParametersInput('reportParameters'),
            leaseLiabilityReportUpdatePage.setReportFileInput(absolutePath),
            leaseLiabilityReportUpdatePage.requestedBySelectLastOption(),
            leaseLiabilityReportUpdatePage.leasePeriodSelectLastOption(),
        ]);

        await leaseLiabilityReportUpdatePage.save();
        expect(await leaseLiabilityReportUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await leaseLiabilityReportComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last LeaseLiabilityReport', async () => {
        const nbButtonsBeforeDelete = await leaseLiabilityReportComponentsPage.countDeleteButtons();
        await leaseLiabilityReportComponentsPage.clickOnLastDeleteButton();

        leaseLiabilityReportDeleteDialog = new LeaseLiabilityReportDeleteDialog();
        expect(await leaseLiabilityReportDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Lease Liability Report?');
        await leaseLiabilityReportDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(leaseLiabilityReportComponentsPage.title), 5000);

        expect(await leaseLiabilityReportComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
