import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { CountyCodeComponentsPage, CountyCodeDeleteDialog, CountyCodeUpdatePage } from './county-code.page-object';

const expect = chai.expect;

describe('CountyCode e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let countyCodeComponentsPage: CountyCodeComponentsPage;
  let countyCodeUpdatePage: CountyCodeUpdatePage;
  let countyCodeDeleteDialog: CountyCodeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CountyCodes', async () => {
    await navBarPage.goToEntity('county-code');
    countyCodeComponentsPage = new CountyCodeComponentsPage();
    await browser.wait(ec.visibilityOf(countyCodeComponentsPage.title), 5000);
    expect(await countyCodeComponentsPage.getTitle()).to.eq('County Codes');
    await browser.wait(ec.or(ec.visibilityOf(countyCodeComponentsPage.entities), ec.visibilityOf(countyCodeComponentsPage.noResult)), 1000);
  });

  it('should load create CountyCode page', async () => {
    await countyCodeComponentsPage.clickOnCreateButton();
    countyCodeUpdatePage = new CountyCodeUpdatePage();
    expect(await countyCodeUpdatePage.getPageTitle()).to.eq('Create or edit a County Code');
    await countyCodeUpdatePage.cancel();
  });

  it('should create and save CountyCodes', async () => {
    const nbButtonsBeforeCreate = await countyCodeComponentsPage.countDeleteButtons();

    await countyCodeComponentsPage.clickOnCreateButton();

    await promise.all([
      countyCodeUpdatePage.setCountyCodeInput('5'),
      countyCodeUpdatePage.setCountyNameInput('countyName'),
      countyCodeUpdatePage.setSubCountyCodeInput('5'),
      countyCodeUpdatePage.setSubCountyNameInput('subCountyName'),
      // countyCodeUpdatePage.placeholderSelectLastOption(),
    ]);

    await countyCodeUpdatePage.save();
    expect(await countyCodeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await countyCodeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last CountyCode', async () => {
    const nbButtonsBeforeDelete = await countyCodeComponentsPage.countDeleteButtons();
    await countyCodeComponentsPage.clickOnLastDeleteButton();

    countyCodeDeleteDialog = new CountyCodeDeleteDialog();
    expect(await countyCodeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this County Code?');
    await countyCodeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(countyCodeComponentsPage.title), 5000);

    expect(await countyCodeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
