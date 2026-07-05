import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { LegalStatusComponentsPage, LegalStatusDeleteDialog, LegalStatusUpdatePage } from './legal-status.page-object';

const expect = chai.expect;

describe('LegalStatus e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let legalStatusComponentsPage: LegalStatusComponentsPage;
  let legalStatusUpdatePage: LegalStatusUpdatePage;
  let legalStatusDeleteDialog: LegalStatusDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load LegalStatuses', async () => {
    await navBarPage.goToEntity('legal-status');
    legalStatusComponentsPage = new LegalStatusComponentsPage();
    await browser.wait(ec.visibilityOf(legalStatusComponentsPage.title), 5000);
    expect(await legalStatusComponentsPage.getTitle()).to.eq('Legal Statuses');
    await browser.wait(
      ec.or(ec.visibilityOf(legalStatusComponentsPage.entities), ec.visibilityOf(legalStatusComponentsPage.noResult)),
      1000
    );
  });

  it('should load create LegalStatus page', async () => {
    await legalStatusComponentsPage.clickOnCreateButton();
    legalStatusUpdatePage = new LegalStatusUpdatePage();
    expect(await legalStatusUpdatePage.getPageTitle()).to.eq('Create or edit a Legal Status');
    await legalStatusUpdatePage.cancel();
  });

  it('should create and save LegalStatuses', async () => {
    const nbButtonsBeforeCreate = await legalStatusComponentsPage.countDeleteButtons();

    await legalStatusComponentsPage.clickOnCreateButton();

    await promise.all([
      legalStatusUpdatePage.setLegalStatusCodeInput('legalStatusCode'),
      legalStatusUpdatePage.setLegalStatusTypeInput('legalStatusType'),
      legalStatusUpdatePage.setLegalStatusDescriptionInput('legalStatusDescription'),
    ]);

    await legalStatusUpdatePage.save();
    expect(await legalStatusUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await legalStatusComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last LegalStatus', async () => {
    const nbButtonsBeforeDelete = await legalStatusComponentsPage.countDeleteButtons();
    await legalStatusComponentsPage.clickOnLastDeleteButton();

    legalStatusDeleteDialog = new LegalStatusDeleteDialog();
    expect(await legalStatusDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Legal Status?');
    await legalStatusDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(legalStatusComponentsPage.title), 5000);

    expect(await legalStatusComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
