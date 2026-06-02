import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  CrbAccountHolderTypeComponentsPage,
  CrbAccountHolderTypeDeleteDialog,
  CrbAccountHolderTypeUpdatePage,
} from './crb-account-holder-type.page-object';

const expect = chai.expect;

describe('CrbAccountHolderType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let crbAccountHolderTypeComponentsPage: CrbAccountHolderTypeComponentsPage;
  let crbAccountHolderTypeUpdatePage: CrbAccountHolderTypeUpdatePage;
  let crbAccountHolderTypeDeleteDialog: CrbAccountHolderTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CrbAccountHolderTypes', async () => {
    await navBarPage.goToEntity('crb-account-holder-type');
    crbAccountHolderTypeComponentsPage = new CrbAccountHolderTypeComponentsPage();
    await browser.wait(ec.visibilityOf(crbAccountHolderTypeComponentsPage.title), 5000);
    expect(await crbAccountHolderTypeComponentsPage.getTitle()).to.eq('Crb Account Holder Types');
    await browser.wait(
      ec.or(ec.visibilityOf(crbAccountHolderTypeComponentsPage.entities), ec.visibilityOf(crbAccountHolderTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create CrbAccountHolderType page', async () => {
    await crbAccountHolderTypeComponentsPage.clickOnCreateButton();
    crbAccountHolderTypeUpdatePage = new CrbAccountHolderTypeUpdatePage();
    expect(await crbAccountHolderTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Crb Account Holder Type');
    await crbAccountHolderTypeUpdatePage.cancel();
  });

  it('should create and save CrbAccountHolderTypes', async () => {
    const nbButtonsBeforeCreate = await crbAccountHolderTypeComponentsPage.countDeleteButtons();

    await crbAccountHolderTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      crbAccountHolderTypeUpdatePage.setAccountHolderCategoryTypeCodeInput('accountHolderCategoryTypeCode'),
      crbAccountHolderTypeUpdatePage.setAccountHolderCategoryTypeInput('accountHolderCategoryType'),
    ]);

    await crbAccountHolderTypeUpdatePage.save();
    expect(await crbAccountHolderTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await crbAccountHolderTypeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last CrbAccountHolderType', async () => {
    const nbButtonsBeforeDelete = await crbAccountHolderTypeComponentsPage.countDeleteButtons();
    await crbAccountHolderTypeComponentsPage.clickOnLastDeleteButton();

    crbAccountHolderTypeDeleteDialog = new CrbAccountHolderTypeDeleteDialog();
    expect(await crbAccountHolderTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Crb Account Holder Type?');
    await crbAccountHolderTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(crbAccountHolderTypeComponentsPage.title), 5000);

    expect(await crbAccountHolderTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
