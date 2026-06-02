import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  CrbComplaintStatusTypeComponentsPage,
  CrbComplaintStatusTypeDeleteDialog,
  CrbComplaintStatusTypeUpdatePage,
} from './crb-complaint-status-type.page-object';

const expect = chai.expect;

describe('CrbComplaintStatusType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let crbComplaintStatusTypeComponentsPage: CrbComplaintStatusTypeComponentsPage;
  let crbComplaintStatusTypeUpdatePage: CrbComplaintStatusTypeUpdatePage;
  let crbComplaintStatusTypeDeleteDialog: CrbComplaintStatusTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CrbComplaintStatusTypes', async () => {
    await navBarPage.goToEntity('crb-complaint-status-type');
    crbComplaintStatusTypeComponentsPage = new CrbComplaintStatusTypeComponentsPage();
    await browser.wait(ec.visibilityOf(crbComplaintStatusTypeComponentsPage.title), 5000);
    expect(await crbComplaintStatusTypeComponentsPage.getTitle()).to.eq('Crb Complaint Status Types');
    await browser.wait(
      ec.or(ec.visibilityOf(crbComplaintStatusTypeComponentsPage.entities), ec.visibilityOf(crbComplaintStatusTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create CrbComplaintStatusType page', async () => {
    await crbComplaintStatusTypeComponentsPage.clickOnCreateButton();
    crbComplaintStatusTypeUpdatePage = new CrbComplaintStatusTypeUpdatePage();
    expect(await crbComplaintStatusTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Crb Complaint Status Type');
    await crbComplaintStatusTypeUpdatePage.cancel();
  });

  it('should create and save CrbComplaintStatusTypes', async () => {
    const nbButtonsBeforeCreate = await crbComplaintStatusTypeComponentsPage.countDeleteButtons();

    await crbComplaintStatusTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      crbComplaintStatusTypeUpdatePage.setComplaintStatusTypeCodeInput('complaintStatusTypeCode'),
      crbComplaintStatusTypeUpdatePage.setComplaintStatusTypeInput('complaintStatusType'),
      crbComplaintStatusTypeUpdatePage.setComplaintStatusDetailsInput('complaintStatusDetails'),
    ]);

    await crbComplaintStatusTypeUpdatePage.save();
    expect(await crbComplaintStatusTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await crbComplaintStatusTypeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last CrbComplaintStatusType', async () => {
    const nbButtonsBeforeDelete = await crbComplaintStatusTypeComponentsPage.countDeleteButtons();
    await crbComplaintStatusTypeComponentsPage.clickOnLastDeleteButton();

    crbComplaintStatusTypeDeleteDialog = new CrbComplaintStatusTypeDeleteDialog();
    expect(await crbComplaintStatusTypeDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Crb Complaint Status Type?'
    );
    await crbComplaintStatusTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(crbComplaintStatusTypeComponentsPage.title), 5000);

    expect(await crbComplaintStatusTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
