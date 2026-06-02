import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  CrbReportRequestReasonsComponentsPage,
  CrbReportRequestReasonsDeleteDialog,
  CrbReportRequestReasonsUpdatePage,
} from './crb-report-request-reasons.page-object';

const expect = chai.expect;

describe('CrbReportRequestReasons e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let crbReportRequestReasonsComponentsPage: CrbReportRequestReasonsComponentsPage;
  let crbReportRequestReasonsUpdatePage: CrbReportRequestReasonsUpdatePage;
  let crbReportRequestReasonsDeleteDialog: CrbReportRequestReasonsDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CrbReportRequestReasons', async () => {
    await navBarPage.goToEntity('crb-report-request-reasons');
    crbReportRequestReasonsComponentsPage = new CrbReportRequestReasonsComponentsPage();
    await browser.wait(ec.visibilityOf(crbReportRequestReasonsComponentsPage.title), 5000);
    expect(await crbReportRequestReasonsComponentsPage.getTitle()).to.eq('Crb Report Request Reasons');
    await browser.wait(
      ec.or(
        ec.visibilityOf(crbReportRequestReasonsComponentsPage.entities),
        ec.visibilityOf(crbReportRequestReasonsComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create CrbReportRequestReasons page', async () => {
    await crbReportRequestReasonsComponentsPage.clickOnCreateButton();
    crbReportRequestReasonsUpdatePage = new CrbReportRequestReasonsUpdatePage();
    expect(await crbReportRequestReasonsUpdatePage.getPageTitle()).to.eq('Create or edit a Crb Report Request Reasons');
    await crbReportRequestReasonsUpdatePage.cancel();
  });

  it('should create and save CrbReportRequestReasons', async () => {
    const nbButtonsBeforeCreate = await crbReportRequestReasonsComponentsPage.countDeleteButtons();

    await crbReportRequestReasonsComponentsPage.clickOnCreateButton();

    await promise.all([
      crbReportRequestReasonsUpdatePage.setCreditReportRequestReasonTypeCodeInput('creditReportRequestReasonTypeCode'),
      crbReportRequestReasonsUpdatePage.setCreditReportRequestReasonTypeInput('creditReportRequestReasonType'),
      crbReportRequestReasonsUpdatePage.setCreditReportRequestDetailsInput('creditReportRequestDetails'),
    ]);

    await crbReportRequestReasonsUpdatePage.save();
    expect(await crbReportRequestReasonsUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await crbReportRequestReasonsComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last CrbReportRequestReasons', async () => {
    const nbButtonsBeforeDelete = await crbReportRequestReasonsComponentsPage.countDeleteButtons();
    await crbReportRequestReasonsComponentsPage.clickOnLastDeleteButton();

    crbReportRequestReasonsDeleteDialog = new CrbReportRequestReasonsDeleteDialog();
    expect(await crbReportRequestReasonsDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Crb Report Request Reasons?'
    );
    await crbReportRequestReasonsDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(crbReportRequestReasonsComponentsPage.title), 5000);

    expect(await crbReportRequestReasonsComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
