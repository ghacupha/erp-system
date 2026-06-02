import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  SourcesOfFundsTypeCodeComponentsPage,
  SourcesOfFundsTypeCodeDeleteDialog,
  SourcesOfFundsTypeCodeUpdatePage,
} from './sources-of-funds-type-code.page-object';

const expect = chai.expect;

describe('SourcesOfFundsTypeCode e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let sourcesOfFundsTypeCodeComponentsPage: SourcesOfFundsTypeCodeComponentsPage;
  let sourcesOfFundsTypeCodeUpdatePage: SourcesOfFundsTypeCodeUpdatePage;
  let sourcesOfFundsTypeCodeDeleteDialog: SourcesOfFundsTypeCodeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load SourcesOfFundsTypeCodes', async () => {
    await navBarPage.goToEntity('sources-of-funds-type-code');
    sourcesOfFundsTypeCodeComponentsPage = new SourcesOfFundsTypeCodeComponentsPage();
    await browser.wait(ec.visibilityOf(sourcesOfFundsTypeCodeComponentsPage.title), 5000);
    expect(await sourcesOfFundsTypeCodeComponentsPage.getTitle()).to.eq('Sources Of Funds Type Codes');
    await browser.wait(
      ec.or(ec.visibilityOf(sourcesOfFundsTypeCodeComponentsPage.entities), ec.visibilityOf(sourcesOfFundsTypeCodeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create SourcesOfFundsTypeCode page', async () => {
    await sourcesOfFundsTypeCodeComponentsPage.clickOnCreateButton();
    sourcesOfFundsTypeCodeUpdatePage = new SourcesOfFundsTypeCodeUpdatePage();
    expect(await sourcesOfFundsTypeCodeUpdatePage.getPageTitle()).to.eq('Create or edit a Sources Of Funds Type Code');
    await sourcesOfFundsTypeCodeUpdatePage.cancel();
  });

  it('should create and save SourcesOfFundsTypeCodes', async () => {
    const nbButtonsBeforeCreate = await sourcesOfFundsTypeCodeComponentsPage.countDeleteButtons();

    await sourcesOfFundsTypeCodeComponentsPage.clickOnCreateButton();

    await promise.all([
      sourcesOfFundsTypeCodeUpdatePage.setSourceOfFundsTypeCodeInput('sourceOfFundsTypeCode'),
      sourcesOfFundsTypeCodeUpdatePage.setSourceOfFundsTypeInput('sourceOfFundsType'),
      sourcesOfFundsTypeCodeUpdatePage.setSourceOfFundsTypeDetailsInput('sourceOfFundsTypeDetails'),
    ]);

    await sourcesOfFundsTypeCodeUpdatePage.save();
    expect(await sourcesOfFundsTypeCodeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await sourcesOfFundsTypeCodeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last SourcesOfFundsTypeCode', async () => {
    const nbButtonsBeforeDelete = await sourcesOfFundsTypeCodeComponentsPage.countDeleteButtons();
    await sourcesOfFundsTypeCodeComponentsPage.clickOnLastDeleteButton();

    sourcesOfFundsTypeCodeDeleteDialog = new SourcesOfFundsTypeCodeDeleteDialog();
    expect(await sourcesOfFundsTypeCodeDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Sources Of Funds Type Code?'
    );
    await sourcesOfFundsTypeCodeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(sourcesOfFundsTypeCodeComponentsPage.title), 5000);

    expect(await sourcesOfFundsTypeCodeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
