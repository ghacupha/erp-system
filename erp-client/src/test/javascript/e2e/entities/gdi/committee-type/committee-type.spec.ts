import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { CommitteeTypeComponentsPage, CommitteeTypeDeleteDialog, CommitteeTypeUpdatePage } from './committee-type.page-object';

const expect = chai.expect;

describe('CommitteeType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let committeeTypeComponentsPage: CommitteeTypeComponentsPage;
  let committeeTypeUpdatePage: CommitteeTypeUpdatePage;
  let committeeTypeDeleteDialog: CommitteeTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CommitteeTypes', async () => {
    await navBarPage.goToEntity('committee-type');
    committeeTypeComponentsPage = new CommitteeTypeComponentsPage();
    await browser.wait(ec.visibilityOf(committeeTypeComponentsPage.title), 5000);
    expect(await committeeTypeComponentsPage.getTitle()).to.eq('Committee Types');
    await browser.wait(
      ec.or(ec.visibilityOf(committeeTypeComponentsPage.entities), ec.visibilityOf(committeeTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create CommitteeType page', async () => {
    await committeeTypeComponentsPage.clickOnCreateButton();
    committeeTypeUpdatePage = new CommitteeTypeUpdatePage();
    expect(await committeeTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Committee Type');
    await committeeTypeUpdatePage.cancel();
  });

  it('should create and save CommitteeTypes', async () => {
    const nbButtonsBeforeCreate = await committeeTypeComponentsPage.countDeleteButtons();

    await committeeTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      committeeTypeUpdatePage.setCommitteeTypeCodeInput('committeeTypeCode'),
      committeeTypeUpdatePage.setCommitteeTypeInput('committeeType'),
      committeeTypeUpdatePage.setCommitteeTypeDetailsInput('committeeTypeDetails'),
    ]);

    await committeeTypeUpdatePage.save();
    expect(await committeeTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await committeeTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last CommitteeType', async () => {
    const nbButtonsBeforeDelete = await committeeTypeComponentsPage.countDeleteButtons();
    await committeeTypeComponentsPage.clickOnLastDeleteButton();

    committeeTypeDeleteDialog = new CommitteeTypeDeleteDialog();
    expect(await committeeTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Committee Type?');
    await committeeTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(committeeTypeComponentsPage.title), 5000);

    expect(await committeeTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
