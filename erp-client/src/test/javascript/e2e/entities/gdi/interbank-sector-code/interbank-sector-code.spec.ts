///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  InterbankSectorCodeComponentsPage,
  InterbankSectorCodeDeleteDialog,
  InterbankSectorCodeUpdatePage,
} from './interbank-sector-code.page-object';

const expect = chai.expect;

describe('InterbankSectorCode e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let interbankSectorCodeComponentsPage: InterbankSectorCodeComponentsPage;
  let interbankSectorCodeUpdatePage: InterbankSectorCodeUpdatePage;
  let interbankSectorCodeDeleteDialog: InterbankSectorCodeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load InterbankSectorCodes', async () => {
    await navBarPage.goToEntity('interbank-sector-code');
    interbankSectorCodeComponentsPage = new InterbankSectorCodeComponentsPage();
    await browser.wait(ec.visibilityOf(interbankSectorCodeComponentsPage.title), 5000);
    expect(await interbankSectorCodeComponentsPage.getTitle()).to.eq('Interbank Sector Codes');
    await browser.wait(
      ec.or(ec.visibilityOf(interbankSectorCodeComponentsPage.entities), ec.visibilityOf(interbankSectorCodeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create InterbankSectorCode page', async () => {
    await interbankSectorCodeComponentsPage.clickOnCreateButton();
    interbankSectorCodeUpdatePage = new InterbankSectorCodeUpdatePage();
    expect(await interbankSectorCodeUpdatePage.getPageTitle()).to.eq('Create or edit a Interbank Sector Code');
    await interbankSectorCodeUpdatePage.cancel();
  });

  it('should create and save InterbankSectorCodes', async () => {
    const nbButtonsBeforeCreate = await interbankSectorCodeComponentsPage.countDeleteButtons();

    await interbankSectorCodeComponentsPage.clickOnCreateButton();

    await promise.all([
      interbankSectorCodeUpdatePage.setInterbankSectorCodeInput('interbankSectorCode'),
      interbankSectorCodeUpdatePage.setInterbankSectorCodeDescriptionInput('interbankSectorCodeDescription'),
    ]);

    await interbankSectorCodeUpdatePage.save();
    expect(await interbankSectorCodeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await interbankSectorCodeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last InterbankSectorCode', async () => {
    const nbButtonsBeforeDelete = await interbankSectorCodeComponentsPage.countDeleteButtons();
    await interbankSectorCodeComponentsPage.clickOnLastDeleteButton();

    interbankSectorCodeDeleteDialog = new InterbankSectorCodeDeleteDialog();
    expect(await interbankSectorCodeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Interbank Sector Code?');
    await interbankSectorCodeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(interbankSectorCodeComponentsPage.title), 5000);

    expect(await interbankSectorCodeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
