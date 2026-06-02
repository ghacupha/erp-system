import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { AlgorithmComponentsPage, AlgorithmDeleteDialog, AlgorithmUpdatePage } from './algorithm.page-object';

const expect = chai.expect;

describe('Algorithm e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let algorithmComponentsPage: AlgorithmComponentsPage;
  let algorithmUpdatePage: AlgorithmUpdatePage;
  let algorithmDeleteDialog: AlgorithmDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Algorithms', async () => {
    await navBarPage.goToEntity('algorithm');
    algorithmComponentsPage = new AlgorithmComponentsPage();
    await browser.wait(ec.visibilityOf(algorithmComponentsPage.title), 5000);
    expect(await algorithmComponentsPage.getTitle()).to.eq('Algorithms');
    await browser.wait(ec.or(ec.visibilityOf(algorithmComponentsPage.entities), ec.visibilityOf(algorithmComponentsPage.noResult)), 1000);
  });

  it('should load create Algorithm page', async () => {
    await algorithmComponentsPage.clickOnCreateButton();
    algorithmUpdatePage = new AlgorithmUpdatePage();
    expect(await algorithmUpdatePage.getPageTitle()).to.eq('Create or edit a Algorithm');
    await algorithmUpdatePage.cancel();
  });

  it('should create and save Algorithms', async () => {
    const nbButtonsBeforeCreate = await algorithmComponentsPage.countDeleteButtons();

    await algorithmComponentsPage.clickOnCreateButton();

    await promise.all([
      algorithmUpdatePage.setNameInput('name'),
      // algorithmUpdatePage.placeholderSelectLastOption(),
      // algorithmUpdatePage.parametersSelectLastOption(),
    ]);

    await algorithmUpdatePage.save();
    expect(await algorithmUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await algorithmComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Algorithm', async () => {
    const nbButtonsBeforeDelete = await algorithmComponentsPage.countDeleteButtons();
    await algorithmComponentsPage.clickOnLastDeleteButton();

    algorithmDeleteDialog = new AlgorithmDeleteDialog();
    expect(await algorithmDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Algorithm?');
    await algorithmDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(algorithmComponentsPage.title), 5000);

    expect(await algorithmComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
