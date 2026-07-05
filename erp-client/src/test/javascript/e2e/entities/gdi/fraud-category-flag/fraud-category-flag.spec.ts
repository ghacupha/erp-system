import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  FraudCategoryFlagComponentsPage,
  FraudCategoryFlagDeleteDialog,
  FraudCategoryFlagUpdatePage,
} from './fraud-category-flag.page-object';

const expect = chai.expect;

describe('FraudCategoryFlag e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let fraudCategoryFlagComponentsPage: FraudCategoryFlagComponentsPage;
  let fraudCategoryFlagUpdatePage: FraudCategoryFlagUpdatePage;
  let fraudCategoryFlagDeleteDialog: FraudCategoryFlagDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load FraudCategoryFlags', async () => {
    await navBarPage.goToEntity('fraud-category-flag');
    fraudCategoryFlagComponentsPage = new FraudCategoryFlagComponentsPage();
    await browser.wait(ec.visibilityOf(fraudCategoryFlagComponentsPage.title), 5000);
    expect(await fraudCategoryFlagComponentsPage.getTitle()).to.eq('Fraud Category Flags');
    await browser.wait(
      ec.or(ec.visibilityOf(fraudCategoryFlagComponentsPage.entities), ec.visibilityOf(fraudCategoryFlagComponentsPage.noResult)),
      1000
    );
  });

  it('should load create FraudCategoryFlag page', async () => {
    await fraudCategoryFlagComponentsPage.clickOnCreateButton();
    fraudCategoryFlagUpdatePage = new FraudCategoryFlagUpdatePage();
    expect(await fraudCategoryFlagUpdatePage.getPageTitle()).to.eq('Create or edit a Fraud Category Flag');
    await fraudCategoryFlagUpdatePage.cancel();
  });

  it('should create and save FraudCategoryFlags', async () => {
    const nbButtonsBeforeCreate = await fraudCategoryFlagComponentsPage.countDeleteButtons();

    await fraudCategoryFlagComponentsPage.clickOnCreateButton();

    await promise.all([
      fraudCategoryFlagUpdatePage.fraudCategoryFlagSelectLastOption(),
      fraudCategoryFlagUpdatePage.setFraudCategoryTypeDetailsInput('fraudCategoryTypeDetails'),
    ]);

    await fraudCategoryFlagUpdatePage.save();
    expect(await fraudCategoryFlagUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await fraudCategoryFlagComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last FraudCategoryFlag', async () => {
    const nbButtonsBeforeDelete = await fraudCategoryFlagComponentsPage.countDeleteButtons();
    await fraudCategoryFlagComponentsPage.clickOnLastDeleteButton();

    fraudCategoryFlagDeleteDialog = new FraudCategoryFlagDeleteDialog();
    expect(await fraudCategoryFlagDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Fraud Category Flag?');
    await fraudCategoryFlagDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(fraudCategoryFlagComponentsPage.title), 5000);

    expect(await fraudCategoryFlagComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
