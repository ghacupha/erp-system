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
  StringQuestionBaseComponentsPage,
  StringQuestionBaseDeleteDialog,
  StringQuestionBaseUpdatePage,
} from './string-question-base.page-object';

const expect = chai.expect;

describe('StringQuestionBase e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let stringQuestionBaseComponentsPage: StringQuestionBaseComponentsPage;
  let stringQuestionBaseUpdatePage: StringQuestionBaseUpdatePage;
  let stringQuestionBaseDeleteDialog: StringQuestionBaseDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load StringQuestionBases', async () => {
    await navBarPage.goToEntity('string-question-base');
    stringQuestionBaseComponentsPage = new StringQuestionBaseComponentsPage();
    await browser.wait(ec.visibilityOf(stringQuestionBaseComponentsPage.title), 5000);
    expect(await stringQuestionBaseComponentsPage.getTitle()).to.eq('String Question Bases');
    await browser.wait(
      ec.or(ec.visibilityOf(stringQuestionBaseComponentsPage.entities), ec.visibilityOf(stringQuestionBaseComponentsPage.noResult)),
      1000
    );
  });

  it('should load create StringQuestionBase page', async () => {
    await stringQuestionBaseComponentsPage.clickOnCreateButton();
    stringQuestionBaseUpdatePage = new StringQuestionBaseUpdatePage();
    expect(await stringQuestionBaseUpdatePage.getPageTitle()).to.eq('Create or edit a String Question Base');
    await stringQuestionBaseUpdatePage.cancel();
  });

  it('should create and save StringQuestionBases', async () => {
    const nbButtonsBeforeCreate = await stringQuestionBaseComponentsPage.countDeleteButtons();

    await stringQuestionBaseComponentsPage.clickOnCreateButton();

    await promise.all([
      stringQuestionBaseUpdatePage.setValueInput('value'),
      stringQuestionBaseUpdatePage.setKeyInput('key'),
      stringQuestionBaseUpdatePage.setLabelInput('label'),
      stringQuestionBaseUpdatePage.getRequiredInput().click(),
      stringQuestionBaseUpdatePage.setOrderInput('5'),
      stringQuestionBaseUpdatePage.controlTypeSelectLastOption(),
      stringQuestionBaseUpdatePage.setPlaceholderInput('placeholder'),
      stringQuestionBaseUpdatePage.getIterableInput().click(),
      // stringQuestionBaseUpdatePage.parametersSelectLastOption(),
      // stringQuestionBaseUpdatePage.placeholderItemSelectLastOption(),
    ]);

    await stringQuestionBaseUpdatePage.save();
    expect(await stringQuestionBaseUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await stringQuestionBaseComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last StringQuestionBase', async () => {
    const nbButtonsBeforeDelete = await stringQuestionBaseComponentsPage.countDeleteButtons();
    await stringQuestionBaseComponentsPage.clickOnLastDeleteButton();

    stringQuestionBaseDeleteDialog = new StringQuestionBaseDeleteDialog();
    expect(await stringQuestionBaseDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this String Question Base?');
    await stringQuestionBaseDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(stringQuestionBaseComponentsPage.title), 5000);

    expect(await stringQuestionBaseComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
