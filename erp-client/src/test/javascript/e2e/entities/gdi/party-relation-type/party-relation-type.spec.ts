import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  PartyRelationTypeComponentsPage,
  PartyRelationTypeDeleteDialog,
  PartyRelationTypeUpdatePage,
} from './party-relation-type.page-object';

const expect = chai.expect;

describe('PartyRelationType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let partyRelationTypeComponentsPage: PartyRelationTypeComponentsPage;
  let partyRelationTypeUpdatePage: PartyRelationTypeUpdatePage;
  let partyRelationTypeDeleteDialog: PartyRelationTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load PartyRelationTypes', async () => {
    await navBarPage.goToEntity('party-relation-type');
    partyRelationTypeComponentsPage = new PartyRelationTypeComponentsPage();
    await browser.wait(ec.visibilityOf(partyRelationTypeComponentsPage.title), 5000);
    expect(await partyRelationTypeComponentsPage.getTitle()).to.eq('Party Relation Types');
    await browser.wait(
      ec.or(ec.visibilityOf(partyRelationTypeComponentsPage.entities), ec.visibilityOf(partyRelationTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create PartyRelationType page', async () => {
    await partyRelationTypeComponentsPage.clickOnCreateButton();
    partyRelationTypeUpdatePage = new PartyRelationTypeUpdatePage();
    expect(await partyRelationTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Party Relation Type');
    await partyRelationTypeUpdatePage.cancel();
  });

  it('should create and save PartyRelationTypes', async () => {
    const nbButtonsBeforeCreate = await partyRelationTypeComponentsPage.countDeleteButtons();

    await partyRelationTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      partyRelationTypeUpdatePage.setPartyRelationTypeCodeInput('partyRelationTypeCode'),
      partyRelationTypeUpdatePage.setPartyRelationTypeInput('partyRelationType'),
      partyRelationTypeUpdatePage.setPartyRelationTypeDescriptionInput('partyRelationTypeDescription'),
    ]);

    await partyRelationTypeUpdatePage.save();
    expect(await partyRelationTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await partyRelationTypeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last PartyRelationType', async () => {
    const nbButtonsBeforeDelete = await partyRelationTypeComponentsPage.countDeleteButtons();
    await partyRelationTypeComponentsPage.clickOnLastDeleteButton();

    partyRelationTypeDeleteDialog = new PartyRelationTypeDeleteDialog();
    expect(await partyRelationTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Party Relation Type?');
    await partyRelationTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(partyRelationTypeComponentsPage.title), 5000);

    expect(await partyRelationTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
