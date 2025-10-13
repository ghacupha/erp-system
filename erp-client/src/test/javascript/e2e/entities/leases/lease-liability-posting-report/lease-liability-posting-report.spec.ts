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
  LeaseLiabilityPostingReportComponentsPage,
  /* LeaseLiabilityPostingReportDeleteDialog, */
  LeaseLiabilityPostingReportUpdatePage,
} from './lease-liability-posting-report.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('LeaseLiabilityPostingReport e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let leaseLiabilityPostingReportComponentsPage: LeaseLiabilityPostingReportComponentsPage;
  let leaseLiabilityPostingReportUpdatePage: LeaseLiabilityPostingReportUpdatePage;
  /* let leaseLiabilityPostingReportDeleteDialog: LeaseLiabilityPostingReportDeleteDialog; */
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

  it('should load LeaseLiabilityPostingReports', async () => {
    await navBarPage.goToEntity('lease-liability-posting-report');
    leaseLiabilityPostingReportComponentsPage = new LeaseLiabilityPostingReportComponentsPage();
    await browser.wait(ec.visibilityOf(leaseLiabilityPostingReportComponentsPage.title), 5000);
    expect(await leaseLiabilityPostingReportComponentsPage.getTitle()).to.eq('Lease Liability Posting Reports');
    await browser.wait(
      ec.or(
        ec.visibilityOf(leaseLiabilityPostingReportComponentsPage.entities),
        ec.visibilityOf(leaseLiabilityPostingReportComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create LeaseLiabilityPostingReport page', async () => {
    await leaseLiabilityPostingReportComponentsPage.clickOnCreateButton();
    leaseLiabilityPostingReportUpdatePage = new LeaseLiabilityPostingReportUpdatePage();
    expect(await leaseLiabilityPostingReportUpdatePage.getPageTitle()).to.eq('Create or edit a Lease Liability Posting Report');
    await leaseLiabilityPostingReportUpdatePage.cancel();
  });

  /* it('should create and save LeaseLiabilityPostingReports', async () => {
        const nbButtonsBeforeCreate = await leaseLiabilityPostingReportComponentsPage.countDeleteButtons();

        await leaseLiabilityPostingReportComponentsPage.clickOnCreateButton();

        await promise.all([
            leaseLiabilityPostingReportUpdatePage.setRequestIdInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            leaseLiabilityPostingReportUpdatePage.setTimeOfRequestInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            leaseLiabilityPostingReportUpdatePage.setFileChecksumInput('fileChecksum'),
            leaseLiabilityPostingReportUpdatePage.getTamperedInput().click(),
            leaseLiabilityPostingReportUpdatePage.setFilenameInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            leaseLiabilityPostingReportUpdatePage.setReportParametersInput('reportParameters'),
            leaseLiabilityPostingReportUpdatePage.setReportFileInput(absolutePath),
            leaseLiabilityPostingReportUpdatePage.requestedBySelectLastOption(),
            leaseLiabilityPostingReportUpdatePage.leasePeriodSelectLastOption(),
        ]);

        await leaseLiabilityPostingReportUpdatePage.save();
        expect(await leaseLiabilityPostingReportUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await leaseLiabilityPostingReportComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last LeaseLiabilityPostingReport', async () => {
        const nbButtonsBeforeDelete = await leaseLiabilityPostingReportComponentsPage.countDeleteButtons();
        await leaseLiabilityPostingReportComponentsPage.clickOnLastDeleteButton();

        leaseLiabilityPostingReportDeleteDialog = new LeaseLiabilityPostingReportDeleteDialog();
        expect(await leaseLiabilityPostingReportDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Lease Liability Posting Report?');
        await leaseLiabilityPostingReportDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(leaseLiabilityPostingReportComponentsPage.title), 5000);

        expect(await leaseLiabilityPostingReportComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
