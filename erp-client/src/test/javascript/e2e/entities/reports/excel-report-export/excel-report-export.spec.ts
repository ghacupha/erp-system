import { browser, ExpectedConditions as ec /* , protractor, promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  ExcelReportExportComponentsPage,
  /* ExcelReportExportDeleteDialog, */
  ExcelReportExportUpdatePage,
} from './excel-report-export.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('ExcelReportExport e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let excelReportExportComponentsPage: ExcelReportExportComponentsPage;
  let excelReportExportUpdatePage: ExcelReportExportUpdatePage;
  /* let excelReportExportDeleteDialog: ExcelReportExportDeleteDialog; */
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

  it('should load ExcelReportExports', async () => {
    await navBarPage.goToEntity('excel-report-export');
    excelReportExportComponentsPage = new ExcelReportExportComponentsPage();
    await browser.wait(ec.visibilityOf(excelReportExportComponentsPage.title), 5000);
    expect(await excelReportExportComponentsPage.getTitle()).to.eq('Excel Report Exports');
    await browser.wait(
      ec.or(ec.visibilityOf(excelReportExportComponentsPage.entities), ec.visibilityOf(excelReportExportComponentsPage.noResult)),
      1000
    );
  });

  it('should load create ExcelReportExport page', async () => {
    await excelReportExportComponentsPage.clickOnCreateButton();
    excelReportExportUpdatePage = new ExcelReportExportUpdatePage();
    expect(await excelReportExportUpdatePage.getPageTitle()).to.eq('Create or edit a Excel Report Export');
    await excelReportExportUpdatePage.cancel();
  });

  /* it('should create and save ExcelReportExports', async () => {
        const nbButtonsBeforeCreate = await excelReportExportComponentsPage.countDeleteButtons();

        await excelReportExportComponentsPage.clickOnCreateButton();

        await promise.all([
            excelReportExportUpdatePage.setReportNameInput('reportName'),
            excelReportExportUpdatePage.setReportPasswordInput('reportPassword'),
            excelReportExportUpdatePage.setReportNotesInput(absolutePath),
            excelReportExportUpdatePage.setFileCheckSumInput('fileCheckSum'),
            excelReportExportUpdatePage.setReportFileInput(absolutePath),
            excelReportExportUpdatePage.setReportTimeStampInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            excelReportExportUpdatePage.setReportIdInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            // excelReportExportUpdatePage.placeholderSelectLastOption(),
            // excelReportExportUpdatePage.parametersSelectLastOption(),
            excelReportExportUpdatePage.reportStatusSelectLastOption(),
            excelReportExportUpdatePage.securityClearanceSelectLastOption(),
            excelReportExportUpdatePage.reportCreatorSelectLastOption(),
            excelReportExportUpdatePage.organizationSelectLastOption(),
            excelReportExportUpdatePage.departmentSelectLastOption(),
            excelReportExportUpdatePage.systemModuleSelectLastOption(),
            excelReportExportUpdatePage.reportDesignSelectLastOption(),
            excelReportExportUpdatePage.fileCheckSumAlgorithmSelectLastOption(),
        ]);

        await excelReportExportUpdatePage.save();
        expect(await excelReportExportUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await excelReportExportComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last ExcelReportExport', async () => {
        const nbButtonsBeforeDelete = await excelReportExportComponentsPage.countDeleteButtons();
        await excelReportExportComponentsPage.clickOnLastDeleteButton();

        excelReportExportDeleteDialog = new ExcelReportExportDeleteDialog();
        expect(await excelReportExportDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Excel Report Export?');
        await excelReportExportDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(excelReportExportComponentsPage.title), 5000);

        expect(await excelReportExportComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
