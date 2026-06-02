import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { PlaceholderComponentsPage, PlaceholderDeleteDialog, PlaceholderUpdatePage } from './placeholder.page-object';

const expect = chai.expect;

describe('Placeholder e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let placeholderComponentsPage: PlaceholderComponentsPage;
  let placeholderUpdatePage: PlaceholderUpdatePage;
  let placeholderDeleteDialog: PlaceholderDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Placeholders', async () => {
    await navBarPage.goToEntity('placeholder');
    placeholderComponentsPage = new PlaceholderComponentsPage();
    await browser.wait(ec.visibilityOf(placeholderComponentsPage.title), 5000);
    expect(await placeholderComponentsPage.getTitle()).to.eq('Placeholders');
    await browser.wait(
      ec.or(ec.visibilityOf(placeholderComponentsPage.entities), ec.visibilityOf(placeholderComponentsPage.noResult)),
      1000
    );
  });

  it('should load create Placeholder page', async () => {
    await placeholderComponentsPage.clickOnCreateButton();
    placeholderUpdatePage = new PlaceholderUpdatePage();
    expect(await placeholderUpdatePage.getPageTitle()).to.eq('Create or edit a Placeholder');
    await placeholderUpdatePage.cancel();
  });

  it('should create and save Placeholders', async () => {
    const nbButtonsBeforeCreate = await placeholderComponentsPage.countDeleteButtons();

    await placeholderComponentsPage.clickOnCreateButton();

    await promise.all([
      placeholderUpdatePage.setDescriptionInput('description'),
      placeholderUpdatePage.setTokenInput('token'),
      placeholderUpdatePage.setFileUploadTokenInput('fileUploadToken'),
      placeholderUpdatePage.setCompilationTokenInput('compilationToken'),
      placeholderUpdatePage.containingPlaceholderSelectLastOption(),
    ]);

    await placeholderUpdatePage.save();
    expect(await placeholderUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await placeholderComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Placeholder', async () => {
    const nbButtonsBeforeDelete = await placeholderComponentsPage.countDeleteButtons();
    await placeholderComponentsPage.clickOnLastDeleteButton();

    placeholderDeleteDialog = new PlaceholderDeleteDialog();
    expect(await placeholderDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Placeholder?');
    await placeholderDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(placeholderComponentsPage.title), 5000);

    expect(await placeholderComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
