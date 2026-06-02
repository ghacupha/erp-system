import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { FxRateTypeComponentsPage, FxRateTypeDeleteDialog, FxRateTypeUpdatePage } from './fx-rate-type.page-object';

const expect = chai.expect;

describe('FxRateType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let fxRateTypeComponentsPage: FxRateTypeComponentsPage;
  let fxRateTypeUpdatePage: FxRateTypeUpdatePage;
  let fxRateTypeDeleteDialog: FxRateTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load FxRateTypes', async () => {
    await navBarPage.goToEntity('fx-rate-type');
    fxRateTypeComponentsPage = new FxRateTypeComponentsPage();
    await browser.wait(ec.visibilityOf(fxRateTypeComponentsPage.title), 5000);
    expect(await fxRateTypeComponentsPage.getTitle()).to.eq('Fx Rate Types');
    await browser.wait(ec.or(ec.visibilityOf(fxRateTypeComponentsPage.entities), ec.visibilityOf(fxRateTypeComponentsPage.noResult)), 1000);
  });

  it('should load create FxRateType page', async () => {
    await fxRateTypeComponentsPage.clickOnCreateButton();
    fxRateTypeUpdatePage = new FxRateTypeUpdatePage();
    expect(await fxRateTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Fx Rate Type');
    await fxRateTypeUpdatePage.cancel();
  });

  it('should create and save FxRateTypes', async () => {
    const nbButtonsBeforeCreate = await fxRateTypeComponentsPage.countDeleteButtons();

    await fxRateTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      fxRateTypeUpdatePage.setFxRateCodeInput('fxRateCode'),
      fxRateTypeUpdatePage.setFxRateTypeInput('fxRateType'),
      fxRateTypeUpdatePage.setFxRateDetailsInput('fxRateDetails'),
    ]);

    await fxRateTypeUpdatePage.save();
    expect(await fxRateTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await fxRateTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last FxRateType', async () => {
    const nbButtonsBeforeDelete = await fxRateTypeComponentsPage.countDeleteButtons();
    await fxRateTypeComponentsPage.clickOnLastDeleteButton();

    fxRateTypeDeleteDialog = new FxRateTypeDeleteDialog();
    expect(await fxRateTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Fx Rate Type?');
    await fxRateTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(fxRateTypeComponentsPage.title), 5000);

    expect(await fxRateTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
