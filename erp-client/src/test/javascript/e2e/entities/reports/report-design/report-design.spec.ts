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

import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  ReportDesignComponentsPage,
  /* ReportDesignDeleteDialog, */
  ReportDesignUpdatePage,
} from './report-design.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('ReportDesign e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let reportDesignComponentsPage: ReportDesignComponentsPage;
  let reportDesignUpdatePage: ReportDesignUpdatePage;
  /* let reportDesignDeleteDialog: ReportDesignDeleteDialog; */
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

  it('should load ReportDesigns', async () => {
    await navBarPage.goToEntity('report-design');
    reportDesignComponentsPage = new ReportDesignComponentsPage();
    await browser.wait(ec.visibilityOf(reportDesignComponentsPage.title), 5000);
    expect(await reportDesignComponentsPage.getTitle()).to.eq('Report Designs');
    await browser.wait(
      ec.or(ec.visibilityOf(reportDesignComponentsPage.entities), ec.visibilityOf(reportDesignComponentsPage.noResult)),
      1000
    );
  });

  it('should load create ReportDesign page', async () => {
    await reportDesignComponentsPage.clickOnCreateButton();
    reportDesignUpdatePage = new ReportDesignUpdatePage();
    expect(await reportDesignUpdatePage.getPageTitle()).to.eq('Create or edit a Report Design');
    await reportDesignUpdatePage.cancel();
  });

  /* it('should create and save ReportDesigns', async () => {
        const nbButtonsBeforeCreate = await reportDesignComponentsPage.countDeleteButtons();

        await reportDesignComponentsPage.clickOnCreateButton();

        await promise.all([
            reportDesignUpdatePage.setCatalogueNumberInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            reportDesignUpdatePage.setDesignationInput('designation'),
            reportDesignUpdatePage.setDescriptionInput('description'),
            reportDesignUpdatePage.setNotesInput(absolutePath),
            reportDesignUpdatePage.setReportFileInput(absolutePath),
            reportDesignUpdatePage.setReportFileChecksumInput('reportFileChecksum'),
            // reportDesignUpdatePage.parametersSelectLastOption(),
            reportDesignUpdatePage.securityClearanceSelectLastOption(),
            reportDesignUpdatePage.reportDesignerSelectLastOption(),
            reportDesignUpdatePage.organizationSelectLastOption(),
            reportDesignUpdatePage.departmentSelectLastOption(),
            // reportDesignUpdatePage.placeholderSelectLastOption(),
            reportDesignUpdatePage.systemModuleSelectLastOption(),
            reportDesignUpdatePage.fileCheckSumAlgorithmSelectLastOption(),
        ]);

        await reportDesignUpdatePage.save();
        expect(await reportDesignUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await reportDesignComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last ReportDesign', async () => {
        const nbButtonsBeforeDelete = await reportDesignComponentsPage.countDeleteButtons();
        await reportDesignComponentsPage.clickOnLastDeleteButton();

        reportDesignDeleteDialog = new ReportDesignDeleteDialog();
        expect(await reportDesignDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Report Design?');
        await reportDesignDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(reportDesignComponentsPage.title), 5000);

        expect(await reportDesignComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
