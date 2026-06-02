import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  SystemContentTypeComponentsPage,
  SystemContentTypeDeleteDialog,
  SystemContentTypeUpdatePage,
} from './system-content-type.page-object';

const expect = chai.expect;

describe('SystemContentType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let systemContentTypeComponentsPage: SystemContentTypeComponentsPage;
  let systemContentTypeUpdatePage: SystemContentTypeUpdatePage;
  let systemContentTypeDeleteDialog: SystemContentTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load SystemContentTypes', async () => {
    await navBarPage.goToEntity('system-content-type');
    systemContentTypeComponentsPage = new SystemContentTypeComponentsPage();
    await browser.wait(ec.visibilityOf(systemContentTypeComponentsPage.title), 5000);
    expect(await systemContentTypeComponentsPage.getTitle()).to.eq('System Content Types');
    await browser.wait(
      ec.or(ec.visibilityOf(systemContentTypeComponentsPage.entities), ec.visibilityOf(systemContentTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create SystemContentType page', async () => {
    await systemContentTypeComponentsPage.clickOnCreateButton();
    systemContentTypeUpdatePage = new SystemContentTypeUpdatePage();
    expect(await systemContentTypeUpdatePage.getPageTitle()).to.eq('Create or edit a System Content Type');
    await systemContentTypeUpdatePage.cancel();
  });

  it('should create and save SystemContentTypes', async () => {
    const nbButtonsBeforeCreate = await systemContentTypeComponentsPage.countDeleteButtons();

    await systemContentTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      systemContentTypeUpdatePage.setContentTypeNameInput('contentTypeName'),
      systemContentTypeUpdatePage.setContentTypeHeaderInput('contentTypeHeader'),
      systemContentTypeUpdatePage.setCommentsInput('comments'),
      systemContentTypeUpdatePage.availabilitySelectLastOption(),
      // systemContentTypeUpdatePage.placeholdersSelectLastOption(),
      // systemContentTypeUpdatePage.sysMapsSelectLastOption(),
    ]);

    await systemContentTypeUpdatePage.save();
    expect(await systemContentTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await systemContentTypeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last SystemContentType', async () => {
    const nbButtonsBeforeDelete = await systemContentTypeComponentsPage.countDeleteButtons();
    await systemContentTypeComponentsPage.clickOnLastDeleteButton();

    systemContentTypeDeleteDialog = new SystemContentTypeDeleteDialog();
    expect(await systemContentTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this System Content Type?');
    await systemContentTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(systemContentTypeComponentsPage.title), 5000);

    expect(await systemContentTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
