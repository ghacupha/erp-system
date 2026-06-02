import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  PrepaymentReportRequisitionComponentsPage,
  PrepaymentReportRequisitionDeleteDialog,
  PrepaymentReportRequisitionUpdatePage,
} from './prepayment-report-requisition.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('PrepaymentReportRequisition e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let prepaymentReportRequisitionComponentsPage: PrepaymentReportRequisitionComponentsPage;
  let prepaymentReportRequisitionUpdatePage: PrepaymentReportRequisitionUpdatePage;
  let prepaymentReportRequisitionDeleteDialog: PrepaymentReportRequisitionDeleteDialog;
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

  it('should load PrepaymentReportRequisitions', async () => {
    await navBarPage.goToEntity('prepayment-report-requisition');
    prepaymentReportRequisitionComponentsPage = new PrepaymentReportRequisitionComponentsPage();
    await browser.wait(ec.visibilityOf(prepaymentReportRequisitionComponentsPage.title), 5000);
    expect(await prepaymentReportRequisitionComponentsPage.getTitle()).to.eq('Prepayment Report Requisitions');
    await browser.wait(
      ec.or(
        ec.visibilityOf(prepaymentReportRequisitionComponentsPage.entities),
        ec.visibilityOf(prepaymentReportRequisitionComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create PrepaymentReportRequisition page', async () => {
    await prepaymentReportRequisitionComponentsPage.clickOnCreateButton();
    prepaymentReportRequisitionUpdatePage = new PrepaymentReportRequisitionUpdatePage();
    expect(await prepaymentReportRequisitionUpdatePage.getPageTitle()).to.eq('Create or edit a Prepayment Report Requisition');
    await prepaymentReportRequisitionUpdatePage.cancel();
  });

  it('should create and save PrepaymentReportRequisitions', async () => {
    const nbButtonsBeforeCreate = await prepaymentReportRequisitionComponentsPage.countDeleteButtons();

    await prepaymentReportRequisitionComponentsPage.clickOnCreateButton();

    await promise.all([
      prepaymentReportRequisitionUpdatePage.setReportNameInput('reportName'),
      prepaymentReportRequisitionUpdatePage.setReportDateInput('2000-12-31'),
      prepaymentReportRequisitionUpdatePage.setTimeOfRequisitionInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      prepaymentReportRequisitionUpdatePage.setFileChecksumInput('fileChecksum'),
      prepaymentReportRequisitionUpdatePage.getTamperedInput().click(),
      prepaymentReportRequisitionUpdatePage.setFilenameInput('64c99148-3908-465d-8c4a-e510e3ade974'),
      prepaymentReportRequisitionUpdatePage.setReportParametersInput('reportParameters'),
      prepaymentReportRequisitionUpdatePage.setReportFileInput(absolutePath),
      prepaymentReportRequisitionUpdatePage.requestedBySelectLastOption(),
      prepaymentReportRequisitionUpdatePage.lastAccessedBySelectLastOption(),
    ]);

    await prepaymentReportRequisitionUpdatePage.save();
    expect(await prepaymentReportRequisitionUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await prepaymentReportRequisitionComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last PrepaymentReportRequisition', async () => {
    const nbButtonsBeforeDelete = await prepaymentReportRequisitionComponentsPage.countDeleteButtons();
    await prepaymentReportRequisitionComponentsPage.clickOnLastDeleteButton();

    prepaymentReportRequisitionDeleteDialog = new PrepaymentReportRequisitionDeleteDialog();
    expect(await prepaymentReportRequisitionDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Prepayment Report Requisition?'
    );
    await prepaymentReportRequisitionDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(prepaymentReportRequisitionComponentsPage.title), 5000);

    expect(await prepaymentReportRequisitionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
