import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { CollateralTypeComponentsPage, CollateralTypeDeleteDialog, CollateralTypeUpdatePage } from './collateral-type.page-object';

const expect = chai.expect;

describe('CollateralType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let collateralTypeComponentsPage: CollateralTypeComponentsPage;
  let collateralTypeUpdatePage: CollateralTypeUpdatePage;
  let collateralTypeDeleteDialog: CollateralTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CollateralTypes', async () => {
    await navBarPage.goToEntity('collateral-type');
    collateralTypeComponentsPage = new CollateralTypeComponentsPage();
    await browser.wait(ec.visibilityOf(collateralTypeComponentsPage.title), 5000);
    expect(await collateralTypeComponentsPage.getTitle()).to.eq('Collateral Types');
    await browser.wait(
      ec.or(ec.visibilityOf(collateralTypeComponentsPage.entities), ec.visibilityOf(collateralTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create CollateralType page', async () => {
    await collateralTypeComponentsPage.clickOnCreateButton();
    collateralTypeUpdatePage = new CollateralTypeUpdatePage();
    expect(await collateralTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Collateral Type');
    await collateralTypeUpdatePage.cancel();
  });

  it('should create and save CollateralTypes', async () => {
    const nbButtonsBeforeCreate = await collateralTypeComponentsPage.countDeleteButtons();

    await collateralTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      collateralTypeUpdatePage.setCollateralTypeCodeInput('collateralTypeCode'),
      collateralTypeUpdatePage.setCollateralTypeInput('collateralType'),
      collateralTypeUpdatePage.setCollateralTypeDescriptionInput('collateralTypeDescription'),
    ]);

    await collateralTypeUpdatePage.save();
    expect(await collateralTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await collateralTypeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last CollateralType', async () => {
    const nbButtonsBeforeDelete = await collateralTypeComponentsPage.countDeleteButtons();
    await collateralTypeComponentsPage.clickOnLastDeleteButton();

    collateralTypeDeleteDialog = new CollateralTypeDeleteDialog();
    expect(await collateralTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Collateral Type?');
    await collateralTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(collateralTypeComponentsPage.title), 5000);

    expect(await collateralTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
