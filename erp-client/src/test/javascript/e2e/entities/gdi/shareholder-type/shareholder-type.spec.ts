import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { ShareholderTypeComponentsPage, ShareholderTypeDeleteDialog, ShareholderTypeUpdatePage } from './shareholder-type.page-object';

const expect = chai.expect;

describe('ShareholderType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let shareholderTypeComponentsPage: ShareholderTypeComponentsPage;
  let shareholderTypeUpdatePage: ShareholderTypeUpdatePage;
  let shareholderTypeDeleteDialog: ShareholderTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ShareholderTypes', async () => {
    await navBarPage.goToEntity('shareholder-type');
    shareholderTypeComponentsPage = new ShareholderTypeComponentsPage();
    await browser.wait(ec.visibilityOf(shareholderTypeComponentsPage.title), 5000);
    expect(await shareholderTypeComponentsPage.getTitle()).to.eq('Shareholder Types');
    await browser.wait(
      ec.or(ec.visibilityOf(shareholderTypeComponentsPage.entities), ec.visibilityOf(shareholderTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create ShareholderType page', async () => {
    await shareholderTypeComponentsPage.clickOnCreateButton();
    shareholderTypeUpdatePage = new ShareholderTypeUpdatePage();
    expect(await shareholderTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Shareholder Type');
    await shareholderTypeUpdatePage.cancel();
  });

  it('should create and save ShareholderTypes', async () => {
    const nbButtonsBeforeCreate = await shareholderTypeComponentsPage.countDeleteButtons();

    await shareholderTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      shareholderTypeUpdatePage.setShareHolderTypeCodeInput('shareHolderTypeCode'),
      shareholderTypeUpdatePage.shareHolderTypeSelectLastOption(),
    ]);

    await shareholderTypeUpdatePage.save();
    expect(await shareholderTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await shareholderTypeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last ShareholderType', async () => {
    const nbButtonsBeforeDelete = await shareholderTypeComponentsPage.countDeleteButtons();
    await shareholderTypeComponentsPage.clickOnLastDeleteButton();

    shareholderTypeDeleteDialog = new ShareholderTypeDeleteDialog();
    expect(await shareholderTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Shareholder Type?');
    await shareholderTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(shareholderTypeComponentsPage.title), 5000);

    expect(await shareholderTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
