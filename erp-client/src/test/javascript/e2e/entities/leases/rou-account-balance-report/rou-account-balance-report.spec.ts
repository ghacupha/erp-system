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
  RouAccountBalanceReportComponentsPage,
  /* RouAccountBalanceReportDeleteDialog, */
  RouAccountBalanceReportUpdatePage,
} from './rou-account-balance-report.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('RouAccountBalanceReport e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let rouAccountBalanceReportComponentsPage: RouAccountBalanceReportComponentsPage;
  let rouAccountBalanceReportUpdatePage: RouAccountBalanceReportUpdatePage;
  /* let rouAccountBalanceReportDeleteDialog: RouAccountBalanceReportDeleteDialog; */
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

  it('should load RouAccountBalanceReports', async () => {
    await navBarPage.goToEntity('rou-account-balance-report');
    rouAccountBalanceReportComponentsPage = new RouAccountBalanceReportComponentsPage();
    await browser.wait(ec.visibilityOf(rouAccountBalanceReportComponentsPage.title), 5000);
    expect(await rouAccountBalanceReportComponentsPage.getTitle()).to.eq('Rou Account Balance Reports');
    await browser.wait(
      ec.or(
        ec.visibilityOf(rouAccountBalanceReportComponentsPage.entities),
        ec.visibilityOf(rouAccountBalanceReportComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create RouAccountBalanceReport page', async () => {
    await rouAccountBalanceReportComponentsPage.clickOnCreateButton();
    rouAccountBalanceReportUpdatePage = new RouAccountBalanceReportUpdatePage();
    expect(await rouAccountBalanceReportUpdatePage.getPageTitle()).to.eq('Create or edit a Rou Account Balance Report');
    await rouAccountBalanceReportUpdatePage.cancel();
  });

  /* it('should create and save RouAccountBalanceReports', async () => {
        const nbButtonsBeforeCreate = await rouAccountBalanceReportComponentsPage.countDeleteButtons();

        await rouAccountBalanceReportComponentsPage.clickOnCreateButton();

        await promise.all([
            rouAccountBalanceReportUpdatePage.setRequestIdInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            rouAccountBalanceReportUpdatePage.setTimeOfRequestInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            rouAccountBalanceReportUpdatePage.getReportIsCompiledInput().click(),
            rouAccountBalanceReportUpdatePage.setFileChecksumInput('fileChecksum'),
            rouAccountBalanceReportUpdatePage.getTamperedInput().click(),
            rouAccountBalanceReportUpdatePage.setFilenameInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            rouAccountBalanceReportUpdatePage.setReportParametersInput('reportParameters'),
            rouAccountBalanceReportUpdatePage.setReportFileInput(absolutePath),
            rouAccountBalanceReportUpdatePage.leasePeriodSelectLastOption(),
            rouAccountBalanceReportUpdatePage.requestedBySelectLastOption(),
        ]);

        await rouAccountBalanceReportUpdatePage.save();
        expect(await rouAccountBalanceReportUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await rouAccountBalanceReportComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last RouAccountBalanceReport', async () => {
        const nbButtonsBeforeDelete = await rouAccountBalanceReportComponentsPage.countDeleteButtons();
        await rouAccountBalanceReportComponentsPage.clickOnLastDeleteButton();

        rouAccountBalanceReportDeleteDialog = new RouAccountBalanceReportDeleteDialog();
        expect(await rouAccountBalanceReportDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Rou Account Balance Report?');
        await rouAccountBalanceReportDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(rouAccountBalanceReportComponentsPage.title), 5000);

        expect(await rouAccountBalanceReportComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
