import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  CountySubCountyCodeComponentsPage,
  CountySubCountyCodeDeleteDialog,
  CountySubCountyCodeUpdatePage,
} from './county-sub-county-code.page-object';

const expect = chai.expect;

describe('CountySubCountyCode e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let countySubCountyCodeComponentsPage: CountySubCountyCodeComponentsPage;
  let countySubCountyCodeUpdatePage: CountySubCountyCodeUpdatePage;
  let countySubCountyCodeDeleteDialog: CountySubCountyCodeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CountySubCountyCodes', async () => {
    await navBarPage.goToEntity('county-sub-county-code');
    countySubCountyCodeComponentsPage = new CountySubCountyCodeComponentsPage();
    await browser.wait(ec.visibilityOf(countySubCountyCodeComponentsPage.title), 5000);
    expect(await countySubCountyCodeComponentsPage.getTitle()).to.eq('County Sub County Codes');
    await browser.wait(
      ec.or(ec.visibilityOf(countySubCountyCodeComponentsPage.entities), ec.visibilityOf(countySubCountyCodeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create CountySubCountyCode page', async () => {
    await countySubCountyCodeComponentsPage.clickOnCreateButton();
    countySubCountyCodeUpdatePage = new CountySubCountyCodeUpdatePage();
    expect(await countySubCountyCodeUpdatePage.getPageTitle()).to.eq('Create or edit a County Sub County Code');
    await countySubCountyCodeUpdatePage.cancel();
  });

  it('should create and save CountySubCountyCodes', async () => {
    const nbButtonsBeforeCreate = await countySubCountyCodeComponentsPage.countDeleteButtons();

    await countySubCountyCodeComponentsPage.clickOnCreateButton();

    await promise.all([
      countySubCountyCodeUpdatePage.setSubCountyCodeInput('5403'),
      countySubCountyCodeUpdatePage.setSubCountyNameInput('subCountyName'),
      countySubCountyCodeUpdatePage.setCountyCodeInput('44'),
      countySubCountyCodeUpdatePage.setCountyNameInput('countyName'),
    ]);

    await countySubCountyCodeUpdatePage.save();
    expect(await countySubCountyCodeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await countySubCountyCodeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last CountySubCountyCode', async () => {
    const nbButtonsBeforeDelete = await countySubCountyCodeComponentsPage.countDeleteButtons();
    await countySubCountyCodeComponentsPage.clickOnLastDeleteButton();

    countySubCountyCodeDeleteDialog = new CountySubCountyCodeDeleteDialog();
    expect(await countySubCountyCodeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this County Sub County Code?');
    await countySubCountyCodeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(countySubCountyCodeComponentsPage.title), 5000);

    expect(await countySubCountyCodeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
