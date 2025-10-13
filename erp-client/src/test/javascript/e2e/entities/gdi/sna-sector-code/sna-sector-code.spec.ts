///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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

import { SnaSectorCodeComponentsPage, SnaSectorCodeDeleteDialog, SnaSectorCodeUpdatePage } from './sna-sector-code.page-object';

const expect = chai.expect;

describe('SnaSectorCode e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let snaSectorCodeComponentsPage: SnaSectorCodeComponentsPage;
  let snaSectorCodeUpdatePage: SnaSectorCodeUpdatePage;
  let snaSectorCodeDeleteDialog: SnaSectorCodeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load SnaSectorCodes', async () => {
    await navBarPage.goToEntity('sna-sector-code');
    snaSectorCodeComponentsPage = new SnaSectorCodeComponentsPage();
    await browser.wait(ec.visibilityOf(snaSectorCodeComponentsPage.title), 5000);
    expect(await snaSectorCodeComponentsPage.getTitle()).to.eq('Sna Sector Codes');
    await browser.wait(
      ec.or(ec.visibilityOf(snaSectorCodeComponentsPage.entities), ec.visibilityOf(snaSectorCodeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create SnaSectorCode page', async () => {
    await snaSectorCodeComponentsPage.clickOnCreateButton();
    snaSectorCodeUpdatePage = new SnaSectorCodeUpdatePage();
    expect(await snaSectorCodeUpdatePage.getPageTitle()).to.eq('Create or edit a Sna Sector Code');
    await snaSectorCodeUpdatePage.cancel();
  });

  it('should create and save SnaSectorCodes', async () => {
    const nbButtonsBeforeCreate = await snaSectorCodeComponentsPage.countDeleteButtons();

    await snaSectorCodeComponentsPage.clickOnCreateButton();

    await promise.all([
      snaSectorCodeUpdatePage.setSectorTypeCodeInput('sectorTypeCode'),
      snaSectorCodeUpdatePage.setMainSectorCodeInput('mainSectorCode'),
      snaSectorCodeUpdatePage.setMainSectorTypeNameInput('mainSectorTypeName'),
      snaSectorCodeUpdatePage.setSubSectorCodeInput('subSectorCode'),
      snaSectorCodeUpdatePage.setSubSectorNameInput('subSectorName'),
      snaSectorCodeUpdatePage.setSubSubSectorCodeInput('subSubSectorCode'),
      snaSectorCodeUpdatePage.setSubSubSectorNameInput('subSubSectorName'),
    ]);

    await snaSectorCodeUpdatePage.save();
    expect(await snaSectorCodeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await snaSectorCodeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last SnaSectorCode', async () => {
    const nbButtonsBeforeDelete = await snaSectorCodeComponentsPage.countDeleteButtons();
    await snaSectorCodeComponentsPage.clickOnLastDeleteButton();

    snaSectorCodeDeleteDialog = new SnaSectorCodeDeleteDialog();
    expect(await snaSectorCodeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Sna Sector Code?');
    await snaSectorCodeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(snaSectorCodeComponentsPage.title), 5000);

    expect(await snaSectorCodeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
