import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { GenderTypeComponentsPage, GenderTypeDeleteDialog, GenderTypeUpdatePage } from './gender-type.page-object';

const expect = chai.expect;

describe('GenderType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let genderTypeComponentsPage: GenderTypeComponentsPage;
  let genderTypeUpdatePage: GenderTypeUpdatePage;
  let genderTypeDeleteDialog: GenderTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load GenderTypes', async () => {
    await navBarPage.goToEntity('gender-type');
    genderTypeComponentsPage = new GenderTypeComponentsPage();
    await browser.wait(ec.visibilityOf(genderTypeComponentsPage.title), 5000);
    expect(await genderTypeComponentsPage.getTitle()).to.eq('Gender Types');
    await browser.wait(ec.or(ec.visibilityOf(genderTypeComponentsPage.entities), ec.visibilityOf(genderTypeComponentsPage.noResult)), 1000);
  });

  it('should load create GenderType page', async () => {
    await genderTypeComponentsPage.clickOnCreateButton();
    genderTypeUpdatePage = new GenderTypeUpdatePage();
    expect(await genderTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Gender Type');
    await genderTypeUpdatePage.cancel();
  });

  it('should create and save GenderTypes', async () => {
    const nbButtonsBeforeCreate = await genderTypeComponentsPage.countDeleteButtons();

    await genderTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      genderTypeUpdatePage.setGenderCodeInput('genderCode'),
      genderTypeUpdatePage.genderTypeSelectLastOption(),
      genderTypeUpdatePage.setGenderDescriptionInput('genderDescription'),
    ]);

    await genderTypeUpdatePage.save();
    expect(await genderTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await genderTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last GenderType', async () => {
    const nbButtonsBeforeDelete = await genderTypeComponentsPage.countDeleteButtons();
    await genderTypeComponentsPage.clickOnLastDeleteButton();

    genderTypeDeleteDialog = new GenderTypeDeleteDialog();
    expect(await genderTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Gender Type?');
    await genderTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(genderTypeComponentsPage.title), 5000);

    expect(await genderTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
