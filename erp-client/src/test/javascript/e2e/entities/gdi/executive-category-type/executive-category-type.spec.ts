import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  ExecutiveCategoryTypeComponentsPage,
  ExecutiveCategoryTypeDeleteDialog,
  ExecutiveCategoryTypeUpdatePage,
} from './executive-category-type.page-object';

const expect = chai.expect;

describe('ExecutiveCategoryType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let executiveCategoryTypeComponentsPage: ExecutiveCategoryTypeComponentsPage;
  let executiveCategoryTypeUpdatePage: ExecutiveCategoryTypeUpdatePage;
  let executiveCategoryTypeDeleteDialog: ExecutiveCategoryTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ExecutiveCategoryTypes', async () => {
    await navBarPage.goToEntity('executive-category-type');
    executiveCategoryTypeComponentsPage = new ExecutiveCategoryTypeComponentsPage();
    await browser.wait(ec.visibilityOf(executiveCategoryTypeComponentsPage.title), 5000);
    expect(await executiveCategoryTypeComponentsPage.getTitle()).to.eq('Executive Category Types');
    await browser.wait(
      ec.or(ec.visibilityOf(executiveCategoryTypeComponentsPage.entities), ec.visibilityOf(executiveCategoryTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create ExecutiveCategoryType page', async () => {
    await executiveCategoryTypeComponentsPage.clickOnCreateButton();
    executiveCategoryTypeUpdatePage = new ExecutiveCategoryTypeUpdatePage();
    expect(await executiveCategoryTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Executive Category Type');
    await executiveCategoryTypeUpdatePage.cancel();
  });

  it('should create and save ExecutiveCategoryTypes', async () => {
    const nbButtonsBeforeCreate = await executiveCategoryTypeComponentsPage.countDeleteButtons();

    await executiveCategoryTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      executiveCategoryTypeUpdatePage.setDirectorCategoryTypeCodeInput('directorCategoryTypeCode'),
      executiveCategoryTypeUpdatePage.setDirectorCategoryTypeInput('directorCategoryType'),
      executiveCategoryTypeUpdatePage.setDirectorCategoryTypeDetailsInput('directorCategoryTypeDetails'),
    ]);

    await executiveCategoryTypeUpdatePage.save();
    expect(await executiveCategoryTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await executiveCategoryTypeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last ExecutiveCategoryType', async () => {
    const nbButtonsBeforeDelete = await executiveCategoryTypeComponentsPage.countDeleteButtons();
    await executiveCategoryTypeComponentsPage.clickOnLastDeleteButton();

    executiveCategoryTypeDeleteDialog = new ExecutiveCategoryTypeDeleteDialog();
    expect(await executiveCategoryTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Executive Category Type?');
    await executiveCategoryTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(executiveCategoryTypeComponentsPage.title), 5000);

    expect(await executiveCategoryTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
