import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { MoratoriumItemComponentsPage, MoratoriumItemDeleteDialog, MoratoriumItemUpdatePage } from './moratorium-item.page-object';

const expect = chai.expect;

describe('MoratoriumItem e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let moratoriumItemComponentsPage: MoratoriumItemComponentsPage;
  let moratoriumItemUpdatePage: MoratoriumItemUpdatePage;
  let moratoriumItemDeleteDialog: MoratoriumItemDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load MoratoriumItems', async () => {
    await navBarPage.goToEntity('moratorium-item');
    moratoriumItemComponentsPage = new MoratoriumItemComponentsPage();
    await browser.wait(ec.visibilityOf(moratoriumItemComponentsPage.title), 5000);
    expect(await moratoriumItemComponentsPage.getTitle()).to.eq('Moratorium Items');
    await browser.wait(
      ec.or(ec.visibilityOf(moratoriumItemComponentsPage.entities), ec.visibilityOf(moratoriumItemComponentsPage.noResult)),
      1000
    );
  });

  it('should load create MoratoriumItem page', async () => {
    await moratoriumItemComponentsPage.clickOnCreateButton();
    moratoriumItemUpdatePage = new MoratoriumItemUpdatePage();
    expect(await moratoriumItemUpdatePage.getPageTitle()).to.eq('Create or edit a Moratorium Item');
    await moratoriumItemUpdatePage.cancel();
  });

  it('should create and save MoratoriumItems', async () => {
    const nbButtonsBeforeCreate = await moratoriumItemComponentsPage.countDeleteButtons();

    await moratoriumItemComponentsPage.clickOnCreateButton();

    await promise.all([
      moratoriumItemUpdatePage.setMoratoriumItemTypeCodeInput('moratoriumItemTypeCode'),
      moratoriumItemUpdatePage.setMoratoriumItemTypeInput('moratoriumItemType'),
      moratoriumItemUpdatePage.setMoratoriumTypeDetailsInput('moratoriumTypeDetails'),
    ]);

    await moratoriumItemUpdatePage.save();
    expect(await moratoriumItemUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await moratoriumItemComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last MoratoriumItem', async () => {
    const nbButtonsBeforeDelete = await moratoriumItemComponentsPage.countDeleteButtons();
    await moratoriumItemComponentsPage.clickOnLastDeleteButton();

    moratoriumItemDeleteDialog = new MoratoriumItemDeleteDialog();
    expect(await moratoriumItemDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Moratorium Item?');
    await moratoriumItemDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(moratoriumItemComponentsPage.title), 5000);

    expect(await moratoriumItemComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
