import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { CrbComplaintTypeComponentsPage, CrbComplaintTypeDeleteDialog, CrbComplaintTypeUpdatePage } from './crb-complaint-type.page-object';

const expect = chai.expect;

describe('CrbComplaintType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let crbComplaintTypeComponentsPage: CrbComplaintTypeComponentsPage;
  let crbComplaintTypeUpdatePage: CrbComplaintTypeUpdatePage;
  let crbComplaintTypeDeleteDialog: CrbComplaintTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CrbComplaintTypes', async () => {
    await navBarPage.goToEntity('crb-complaint-type');
    crbComplaintTypeComponentsPage = new CrbComplaintTypeComponentsPage();
    await browser.wait(ec.visibilityOf(crbComplaintTypeComponentsPage.title), 5000);
    expect(await crbComplaintTypeComponentsPage.getTitle()).to.eq('Crb Complaint Types');
    await browser.wait(
      ec.or(ec.visibilityOf(crbComplaintTypeComponentsPage.entities), ec.visibilityOf(crbComplaintTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create CrbComplaintType page', async () => {
    await crbComplaintTypeComponentsPage.clickOnCreateButton();
    crbComplaintTypeUpdatePage = new CrbComplaintTypeUpdatePage();
    expect(await crbComplaintTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Crb Complaint Type');
    await crbComplaintTypeUpdatePage.cancel();
  });

  it('should create and save CrbComplaintTypes', async () => {
    const nbButtonsBeforeCreate = await crbComplaintTypeComponentsPage.countDeleteButtons();

    await crbComplaintTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      crbComplaintTypeUpdatePage.setComplaintTypeCodeInput('complaintTypeCode'),
      crbComplaintTypeUpdatePage.setComplaintTypeInput('complaintType'),
      crbComplaintTypeUpdatePage.setComplaintTypeDetailsInput('complaintTypeDetails'),
    ]);

    await crbComplaintTypeUpdatePage.save();
    expect(await crbComplaintTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await crbComplaintTypeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last CrbComplaintType', async () => {
    const nbButtonsBeforeDelete = await crbComplaintTypeComponentsPage.countDeleteButtons();
    await crbComplaintTypeComponentsPage.clickOnLastDeleteButton();

    crbComplaintTypeDeleteDialog = new CrbComplaintTypeDeleteDialog();
    expect(await crbComplaintTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Crb Complaint Type?');
    await crbComplaintTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(crbComplaintTypeComponentsPage.title), 5000);

    expect(await crbComplaintTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
