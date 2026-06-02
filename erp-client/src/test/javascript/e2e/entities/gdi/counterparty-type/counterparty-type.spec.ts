import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { CounterpartyTypeComponentsPage, CounterpartyTypeDeleteDialog, CounterpartyTypeUpdatePage } from './counterparty-type.page-object';

const expect = chai.expect;

describe('CounterpartyType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let counterpartyTypeComponentsPage: CounterpartyTypeComponentsPage;
  let counterpartyTypeUpdatePage: CounterpartyTypeUpdatePage;
  let counterpartyTypeDeleteDialog: CounterpartyTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CounterpartyTypes', async () => {
    await navBarPage.goToEntity('counterparty-type');
    counterpartyTypeComponentsPage = new CounterpartyTypeComponentsPage();
    await browser.wait(ec.visibilityOf(counterpartyTypeComponentsPage.title), 5000);
    expect(await counterpartyTypeComponentsPage.getTitle()).to.eq('Counterparty Types');
    await browser.wait(
      ec.or(ec.visibilityOf(counterpartyTypeComponentsPage.entities), ec.visibilityOf(counterpartyTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create CounterpartyType page', async () => {
    await counterpartyTypeComponentsPage.clickOnCreateButton();
    counterpartyTypeUpdatePage = new CounterpartyTypeUpdatePage();
    expect(await counterpartyTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Counterparty Type');
    await counterpartyTypeUpdatePage.cancel();
  });

  it('should create and save CounterpartyTypes', async () => {
    const nbButtonsBeforeCreate = await counterpartyTypeComponentsPage.countDeleteButtons();

    await counterpartyTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      counterpartyTypeUpdatePage.setCounterpartyTypeCodeInput('counterpartyTypeCode'),
      counterpartyTypeUpdatePage.setCounterPartyTypeInput('counterPartyType'),
      counterpartyTypeUpdatePage.setCounterpartyTypeDescriptionInput('counterpartyTypeDescription'),
    ]);

    await counterpartyTypeUpdatePage.save();
    expect(await counterpartyTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await counterpartyTypeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last CounterpartyType', async () => {
    const nbButtonsBeforeDelete = await counterpartyTypeComponentsPage.countDeleteButtons();
    await counterpartyTypeComponentsPage.clickOnLastDeleteButton();

    counterpartyTypeDeleteDialog = new CounterpartyTypeDeleteDialog();
    expect(await counterpartyTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Counterparty Type?');
    await counterpartyTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(counterpartyTypeComponentsPage.title), 5000);

    expect(await counterpartyTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
