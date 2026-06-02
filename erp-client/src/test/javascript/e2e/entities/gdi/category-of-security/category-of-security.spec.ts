import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  CategoryOfSecurityComponentsPage,
  CategoryOfSecurityDeleteDialog,
  CategoryOfSecurityUpdatePage,
} from './category-of-security.page-object';

const expect = chai.expect;

describe('CategoryOfSecurity e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let categoryOfSecurityComponentsPage: CategoryOfSecurityComponentsPage;
  let categoryOfSecurityUpdatePage: CategoryOfSecurityUpdatePage;
  let categoryOfSecurityDeleteDialog: CategoryOfSecurityDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CategoryOfSecurities', async () => {
    await navBarPage.goToEntity('category-of-security');
    categoryOfSecurityComponentsPage = new CategoryOfSecurityComponentsPage();
    await browser.wait(ec.visibilityOf(categoryOfSecurityComponentsPage.title), 5000);
    expect(await categoryOfSecurityComponentsPage.getTitle()).to.eq('Category Of Securities');
    await browser.wait(
      ec.or(ec.visibilityOf(categoryOfSecurityComponentsPage.entities), ec.visibilityOf(categoryOfSecurityComponentsPage.noResult)),
      1000
    );
  });

  it('should load create CategoryOfSecurity page', async () => {
    await categoryOfSecurityComponentsPage.clickOnCreateButton();
    categoryOfSecurityUpdatePage = new CategoryOfSecurityUpdatePage();
    expect(await categoryOfSecurityUpdatePage.getPageTitle()).to.eq('Create or edit a Category Of Security');
    await categoryOfSecurityUpdatePage.cancel();
  });

  it('should create and save CategoryOfSecurities', async () => {
    const nbButtonsBeforeCreate = await categoryOfSecurityComponentsPage.countDeleteButtons();

    await categoryOfSecurityComponentsPage.clickOnCreateButton();

    await promise.all([
      categoryOfSecurityUpdatePage.setCategoryOfSecurityInput('categoryOfSecurity'),
      categoryOfSecurityUpdatePage.setCategoryOfSecurityDetailsInput('categoryOfSecurityDetails'),
      categoryOfSecurityUpdatePage.setCategoryOfSecurityDescriptionInput('categoryOfSecurityDescription'),
    ]);

    await categoryOfSecurityUpdatePage.save();
    expect(await categoryOfSecurityUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await categoryOfSecurityComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last CategoryOfSecurity', async () => {
    const nbButtonsBeforeDelete = await categoryOfSecurityComponentsPage.countDeleteButtons();
    await categoryOfSecurityComponentsPage.clickOnLastDeleteButton();

    categoryOfSecurityDeleteDialog = new CategoryOfSecurityDeleteDialog();
    expect(await categoryOfSecurityDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Category Of Security?');
    await categoryOfSecurityDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(categoryOfSecurityComponentsPage.title), 5000);

    expect(await categoryOfSecurityComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
