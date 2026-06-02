import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { CrbGlCodeComponentsPage, CrbGlCodeDeleteDialog, CrbGlCodeUpdatePage } from './crb-gl-code.page-object';

const expect = chai.expect;

describe('CrbGlCode e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let crbGlCodeComponentsPage: CrbGlCodeComponentsPage;
  let crbGlCodeUpdatePage: CrbGlCodeUpdatePage;
  let crbGlCodeDeleteDialog: CrbGlCodeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CrbGlCodes', async () => {
    await navBarPage.goToEntity('crb-gl-code');
    crbGlCodeComponentsPage = new CrbGlCodeComponentsPage();
    await browser.wait(ec.visibilityOf(crbGlCodeComponentsPage.title), 5000);
    expect(await crbGlCodeComponentsPage.getTitle()).to.eq('Crb Gl Codes');
    await browser.wait(ec.or(ec.visibilityOf(crbGlCodeComponentsPage.entities), ec.visibilityOf(crbGlCodeComponentsPage.noResult)), 1000);
  });

  it('should load create CrbGlCode page', async () => {
    await crbGlCodeComponentsPage.clickOnCreateButton();
    crbGlCodeUpdatePage = new CrbGlCodeUpdatePage();
    expect(await crbGlCodeUpdatePage.getPageTitle()).to.eq('Create or edit a Crb Gl Code');
    await crbGlCodeUpdatePage.cancel();
  });

  it('should create and save CrbGlCodes', async () => {
    const nbButtonsBeforeCreate = await crbGlCodeComponentsPage.countDeleteButtons();

    await crbGlCodeComponentsPage.clickOnCreateButton();

    await promise.all([
      crbGlCodeUpdatePage.setGlCodeInput('glCode'),
      crbGlCodeUpdatePage.setGlDescriptionInput('glDescription'),
      crbGlCodeUpdatePage.setGlTypeInput('glType'),
      crbGlCodeUpdatePage.setInstitutionCategoryInput('institutionCategory'),
    ]);

    await crbGlCodeUpdatePage.save();
    expect(await crbGlCodeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await crbGlCodeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last CrbGlCode', async () => {
    const nbButtonsBeforeDelete = await crbGlCodeComponentsPage.countDeleteButtons();
    await crbGlCodeComponentsPage.clickOnLastDeleteButton();

    crbGlCodeDeleteDialog = new CrbGlCodeDeleteDialog();
    expect(await crbGlCodeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Crb Gl Code?');
    await crbGlCodeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(crbGlCodeComponentsPage.title), 5000);

    expect(await crbGlCodeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
