import { browser, ExpectedConditions as ec /* , protractor, promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  AmortizationPostingReportRequisitionComponentsPage,
  /* AmortizationPostingReportRequisitionDeleteDialog, */
  AmortizationPostingReportRequisitionUpdatePage,
} from './amortization-posting-report-requisition.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('AmortizationPostingReportRequisition e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let amortizationPostingReportRequisitionComponentsPage: AmortizationPostingReportRequisitionComponentsPage;
  let amortizationPostingReportRequisitionUpdatePage: AmortizationPostingReportRequisitionUpdatePage;
  /* let amortizationPostingReportRequisitionDeleteDialog: AmortizationPostingReportRequisitionDeleteDialog; */
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
            amortizationPostingReportRequisitionUpdatePage.setTimeOfRequisitionInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            amortizationPostingReportRequisitionUpdatePage.setFileChecksumInput('fileChecksum'),
            amortizationPostingReportRequisitionUpdatePage.getTamperedInput().click(),
            amortizationPostingReportRequisitionUpdatePage.setFilenameInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            amortizationPostingReportRequisitionUpdatePage.setReportParametersInput('reportParameters'),
            amortizationPostingReportRequisitionUpdatePage.setReportFileInput(absolutePath),
            amortizationPostingReportRequisitionUpdatePage.amortizationPeriodSelectLastOption(),
            amortizationPostingReportRequisitionUpdatePage.requestedBySelectLastOption(),
            amortizationPostingReportRequisitionUpdatePage.lastAccessedBySelectLastOption(),
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
