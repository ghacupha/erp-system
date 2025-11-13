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

import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { ReportTemplateComponentsPage, ReportTemplateDeleteDialog, ReportTemplateUpdatePage } from './report-template.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('ReportTemplate e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let reportTemplateComponentsPage: ReportTemplateComponentsPage;
  let reportTemplateUpdatePage: ReportTemplateUpdatePage;
  let reportTemplateDeleteDialog: ReportTemplateDeleteDialog;
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

  it('should load ReportTemplates', async () => {
    await navBarPage.goToEntity('report-template');
    reportTemplateComponentsPage = new ReportTemplateComponentsPage();
    await browser.wait(ec.visibilityOf(reportTemplateComponentsPage.title), 5000);
    expect(await reportTemplateComponentsPage.getTitle()).to.eq('Report Templates');
    await browser.wait(
      ec.or(ec.visibilityOf(reportTemplateComponentsPage.entities), ec.visibilityOf(reportTemplateComponentsPage.noResult)),
      1000
    );
  });

  it('should load create ReportTemplate page', async () => {
    await reportTemplateComponentsPage.clickOnCreateButton();
    reportTemplateUpdatePage = new ReportTemplateUpdatePage();
    expect(await reportTemplateUpdatePage.getPageTitle()).to.eq('Create or edit a Report Template');
    await reportTemplateUpdatePage.cancel();
  });

  it('should create and save ReportTemplates', async () => {
    const nbButtonsBeforeCreate = await reportTemplateComponentsPage.countDeleteButtons();

    await reportTemplateComponentsPage.clickOnCreateButton();

    await promise.all([
      reportTemplateUpdatePage.setCatalogueNumberInput('catalogueNumber'),
      reportTemplateUpdatePage.setDescriptionInput('description'),
      reportTemplateUpdatePage.setNotesInput(absolutePath),
      reportTemplateUpdatePage.setReportFileInput(absolutePath),
      reportTemplateUpdatePage.setCompileReportFileInput(absolutePath),
      // reportTemplateUpdatePage.placeholderSelectLastOption(),
    ]);

    await reportTemplateUpdatePage.save();
    expect(await reportTemplateUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await reportTemplateComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last ReportTemplate', async () => {
    const nbButtonsBeforeDelete = await reportTemplateComponentsPage.countDeleteButtons();
    await reportTemplateComponentsPage.clickOnLastDeleteButton();

    reportTemplateDeleteDialog = new ReportTemplateDeleteDialog();
    expect(await reportTemplateDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Report Template?');
    await reportTemplateDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(reportTemplateComponentsPage.title), 5000);

    expect(await reportTemplateComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
