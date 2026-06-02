import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  InsiderCategoryTypesComponentsPage,
  InsiderCategoryTypesDeleteDialog,
  InsiderCategoryTypesUpdatePage,
} from './insider-category-types.page-object';

const expect = chai.expect;

describe('InsiderCategoryTypes e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let insiderCategoryTypesComponentsPage: InsiderCategoryTypesComponentsPage;
  let insiderCategoryTypesUpdatePage: InsiderCategoryTypesUpdatePage;
  let insiderCategoryTypesDeleteDialog: InsiderCategoryTypesDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load InsiderCategoryTypes', async () => {
    await navBarPage.goToEntity('insider-category-types');
    insiderCategoryTypesComponentsPage = new InsiderCategoryTypesComponentsPage();
    await browser.wait(ec.visibilityOf(insiderCategoryTypesComponentsPage.title), 5000);
    expect(await insiderCategoryTypesComponentsPage.getTitle()).to.eq('Insider Category Types');
    await browser.wait(
      ec.or(ec.visibilityOf(insiderCategoryTypesComponentsPage.entities), ec.visibilityOf(insiderCategoryTypesComponentsPage.noResult)),
      1000
    );
  });

  it('should load create InsiderCategoryTypes page', async () => {
    await insiderCategoryTypesComponentsPage.clickOnCreateButton();
    insiderCategoryTypesUpdatePage = new InsiderCategoryTypesUpdatePage();
    expect(await insiderCategoryTypesUpdatePage.getPageTitle()).to.eq('Create or edit a Insider Category Types');
    await insiderCategoryTypesUpdatePage.cancel();
  });

  it('should create and save InsiderCategoryTypes', async () => {
    const nbButtonsBeforeCreate = await insiderCategoryTypesComponentsPage.countDeleteButtons();

    await insiderCategoryTypesComponentsPage.clickOnCreateButton();

    await promise.all([
      insiderCategoryTypesUpdatePage.setInsiderCategoryTypeCodeInput('insiderCategoryTypeCode'),
      insiderCategoryTypesUpdatePage.setInsiderCategoryTypeDetailInput('insiderCategoryTypeDetail'),
      insiderCategoryTypesUpdatePage.setInsiderCategoryDescriptionInput('insiderCategoryDescription'),
    ]);

    await insiderCategoryTypesUpdatePage.save();
    expect(await insiderCategoryTypesUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await insiderCategoryTypesComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last InsiderCategoryTypes', async () => {
    const nbButtonsBeforeDelete = await insiderCategoryTypesComponentsPage.countDeleteButtons();
    await insiderCategoryTypesComponentsPage.clickOnLastDeleteButton();

    insiderCategoryTypesDeleteDialog = new InsiderCategoryTypesDeleteDialog();
    expect(await insiderCategoryTypesDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Insider Category Types?');
    await insiderCategoryTypesDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(insiderCategoryTypesComponentsPage.title), 5000);

    expect(await insiderCategoryTypesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
