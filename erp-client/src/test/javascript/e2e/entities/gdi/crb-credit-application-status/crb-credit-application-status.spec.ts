import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  CrbCreditApplicationStatusComponentsPage,
  CrbCreditApplicationStatusDeleteDialog,
  CrbCreditApplicationStatusUpdatePage,
} from './crb-credit-application-status.page-object';

const expect = chai.expect;

describe('CrbCreditApplicationStatus e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let crbCreditApplicationStatusComponentsPage: CrbCreditApplicationStatusComponentsPage;
  let crbCreditApplicationStatusUpdatePage: CrbCreditApplicationStatusUpdatePage;
  let crbCreditApplicationStatusDeleteDialog: CrbCreditApplicationStatusDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CrbCreditApplicationStatuses', async () => {
    await navBarPage.goToEntity('crb-credit-application-status');
    crbCreditApplicationStatusComponentsPage = new CrbCreditApplicationStatusComponentsPage();
    await browser.wait(ec.visibilityOf(crbCreditApplicationStatusComponentsPage.title), 5000);
    expect(await crbCreditApplicationStatusComponentsPage.getTitle()).to.eq('Crb Credit Application Statuses');
    await browser.wait(
      ec.or(
        ec.visibilityOf(crbCreditApplicationStatusComponentsPage.entities),
        ec.visibilityOf(crbCreditApplicationStatusComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create CrbCreditApplicationStatus page', async () => {
    await crbCreditApplicationStatusComponentsPage.clickOnCreateButton();
    crbCreditApplicationStatusUpdatePage = new CrbCreditApplicationStatusUpdatePage();
    expect(await crbCreditApplicationStatusUpdatePage.getPageTitle()).to.eq('Create or edit a Crb Credit Application Status');
    await crbCreditApplicationStatusUpdatePage.cancel();
  });

  it('should create and save CrbCreditApplicationStatuses', async () => {
    const nbButtonsBeforeCreate = await crbCreditApplicationStatusComponentsPage.countDeleteButtons();

    await crbCreditApplicationStatusComponentsPage.clickOnCreateButton();

    await promise.all([
      crbCreditApplicationStatusUpdatePage.setCrbCreditApplicationStatusTypeCodeInput('crbCreditApplicationStatusTypeCode'),
      crbCreditApplicationStatusUpdatePage.setCrbCreditApplicationStatusTypeInput('crbCreditApplicationStatusType'),
      crbCreditApplicationStatusUpdatePage.setCrbCreditApplicationStatusDetailsInput('crbCreditApplicationStatusDetails'),
    ]);

    await crbCreditApplicationStatusUpdatePage.save();
    expect(await crbCreditApplicationStatusUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await crbCreditApplicationStatusComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last CrbCreditApplicationStatus', async () => {
    const nbButtonsBeforeDelete = await crbCreditApplicationStatusComponentsPage.countDeleteButtons();
    await crbCreditApplicationStatusComponentsPage.clickOnLastDeleteButton();

    crbCreditApplicationStatusDeleteDialog = new CrbCreditApplicationStatusDeleteDialog();
    expect(await crbCreditApplicationStatusDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Crb Credit Application Status?'
    );
    await crbCreditApplicationStatusDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(crbCreditApplicationStatusComponentsPage.title), 5000);

    expect(await crbCreditApplicationStatusComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
