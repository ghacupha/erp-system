import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { ReportStatusComponentsPage, ReportStatusDeleteDialog, ReportStatusUpdatePage } from './report-status.page-object';

const expect = chai.expect;

describe('ReportStatus e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let reportStatusComponentsPage: ReportStatusComponentsPage;
  let reportStatusUpdatePage: ReportStatusUpdatePage;
  let reportStatusDeleteDialog: ReportStatusDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ReportStatuses', async () => {
    await navBarPage.goToEntity('report-status');
    reportStatusComponentsPage = new ReportStatusComponentsPage();
    await browser.wait(ec.visibilityOf(reportStatusComponentsPage.title), 5000);
    expect(await reportStatusComponentsPage.getTitle()).to.eq('Report Statuses');
    await browser.wait(
      ec.or(ec.visibilityOf(reportStatusComponentsPage.entities), ec.visibilityOf(reportStatusComponentsPage.noResult)),
      1000
    );
  });

  it('should load create ReportStatus page', async () => {
    await reportStatusComponentsPage.clickOnCreateButton();
    reportStatusUpdatePage = new ReportStatusUpdatePage();
    expect(await reportStatusUpdatePage.getPageTitle()).to.eq('Create or edit a Report Status');
    await reportStatusUpdatePage.cancel();
  });

  it('should create and save ReportStatuses', async () => {
    const nbButtonsBeforeCreate = await reportStatusComponentsPage.countDeleteButtons();

    await reportStatusComponentsPage.clickOnCreateButton();

    await promise.all([
      reportStatusUpdatePage.setReportNameInput('reportName'),
      reportStatusUpdatePage.setReportIdInput('64c99148-3908-465d-8c4a-e510e3ade974'),
      // reportStatusUpdatePage.placeholderSelectLastOption(),
      reportStatusUpdatePage.processStatusSelectLastOption(),
    ]);

    await reportStatusUpdatePage.save();
    expect(await reportStatusUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await reportStatusComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last ReportStatus', async () => {
    const nbButtonsBeforeDelete = await reportStatusComponentsPage.countDeleteButtons();
    await reportStatusComponentsPage.clickOnLastDeleteButton();

    reportStatusDeleteDialog = new ReportStatusDeleteDialog();
    expect(await reportStatusDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Report Status?');
    await reportStatusDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(reportStatusComponentsPage.title), 5000);

    expect(await reportStatusComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
