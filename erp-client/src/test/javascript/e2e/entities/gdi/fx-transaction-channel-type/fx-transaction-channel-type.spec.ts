import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  FxTransactionChannelTypeComponentsPage,
  FxTransactionChannelTypeDeleteDialog,
  FxTransactionChannelTypeUpdatePage,
} from './fx-transaction-channel-type.page-object';

const expect = chai.expect;

describe('FxTransactionChannelType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let fxTransactionChannelTypeComponentsPage: FxTransactionChannelTypeComponentsPage;
  let fxTransactionChannelTypeUpdatePage: FxTransactionChannelTypeUpdatePage;
  let fxTransactionChannelTypeDeleteDialog: FxTransactionChannelTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load FxTransactionChannelTypes', async () => {
    await navBarPage.goToEntity('fx-transaction-channel-type');
    fxTransactionChannelTypeComponentsPage = new FxTransactionChannelTypeComponentsPage();
    await browser.wait(ec.visibilityOf(fxTransactionChannelTypeComponentsPage.title), 5000);
    expect(await fxTransactionChannelTypeComponentsPage.getTitle()).to.eq('Fx Transaction Channel Types');
    await browser.wait(
      ec.or(
        ec.visibilityOf(fxTransactionChannelTypeComponentsPage.entities),
        ec.visibilityOf(fxTransactionChannelTypeComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create FxTransactionChannelType page', async () => {
    await fxTransactionChannelTypeComponentsPage.clickOnCreateButton();
    fxTransactionChannelTypeUpdatePage = new FxTransactionChannelTypeUpdatePage();
    expect(await fxTransactionChannelTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Fx Transaction Channel Type');
    await fxTransactionChannelTypeUpdatePage.cancel();
  });

  it('should create and save FxTransactionChannelTypes', async () => {
    const nbButtonsBeforeCreate = await fxTransactionChannelTypeComponentsPage.countDeleteButtons();

    await fxTransactionChannelTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      fxTransactionChannelTypeUpdatePage.setFxTransactionChannelCodeInput('fxTransactionChannelCode'),
      fxTransactionChannelTypeUpdatePage.setFxTransactionChannelTypeInput('fxTransactionChannelType'),
      fxTransactionChannelTypeUpdatePage.setFxChannelTypeDetailsInput('fxChannelTypeDetails'),
    ]);

    await fxTransactionChannelTypeUpdatePage.save();
    expect(await fxTransactionChannelTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await fxTransactionChannelTypeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last FxTransactionChannelType', async () => {
    const nbButtonsBeforeDelete = await fxTransactionChannelTypeComponentsPage.countDeleteButtons();
    await fxTransactionChannelTypeComponentsPage.clickOnLastDeleteButton();

    fxTransactionChannelTypeDeleteDialog = new FxTransactionChannelTypeDeleteDialog();
    expect(await fxTransactionChannelTypeDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Fx Transaction Channel Type?'
    );
    await fxTransactionChannelTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(fxTransactionChannelTypeComponentsPage.title), 5000);

    expect(await fxTransactionChannelTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
