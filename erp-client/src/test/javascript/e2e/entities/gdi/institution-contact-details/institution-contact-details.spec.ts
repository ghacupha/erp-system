///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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
  InstitutionContactDetailsComponentsPage,
  InstitutionContactDetailsDeleteDialog,
  InstitutionContactDetailsUpdatePage,
} from './institution-contact-details.page-object';

const expect = chai.expect;

describe('InstitutionContactDetails e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let institutionContactDetailsComponentsPage: InstitutionContactDetailsComponentsPage;
  let institutionContactDetailsUpdatePage: InstitutionContactDetailsUpdatePage;
  let institutionContactDetailsDeleteDialog: InstitutionContactDetailsDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load InstitutionContactDetails', async () => {
    await navBarPage.goToEntity('institution-contact-details');
    institutionContactDetailsComponentsPage = new InstitutionContactDetailsComponentsPage();
    await browser.wait(ec.visibilityOf(institutionContactDetailsComponentsPage.title), 5000);
    expect(await institutionContactDetailsComponentsPage.getTitle()).to.eq('Institution Contact Details');
    await browser.wait(
      ec.or(
        ec.visibilityOf(institutionContactDetailsComponentsPage.entities),
        ec.visibilityOf(institutionContactDetailsComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create InstitutionContactDetails page', async () => {
    await institutionContactDetailsComponentsPage.clickOnCreateButton();
    institutionContactDetailsUpdatePage = new InstitutionContactDetailsUpdatePage();
    expect(await institutionContactDetailsUpdatePage.getPageTitle()).to.eq('Create or edit a Institution Contact Details');
    await institutionContactDetailsUpdatePage.cancel();
  });

  it('should create and save InstitutionContactDetails', async () => {
    const nbButtonsBeforeCreate = await institutionContactDetailsComponentsPage.countDeleteButtons();

    await institutionContactDetailsComponentsPage.clickOnCreateButton();

    await promise.all([
      institutionContactDetailsUpdatePage.setEntityIdInput('entityId'),
      institutionContactDetailsUpdatePage.setEntityNameInput('entityName'),
      institutionContactDetailsUpdatePage.setContactTypeInput('contactType'),
      institutionContactDetailsUpdatePage.setContactLevelInput('contactLevel'),
      institutionContactDetailsUpdatePage.setContactValueInput('contactValue'),
      institutionContactDetailsUpdatePage.setContactNameInput('contactName'),
      institutionContactDetailsUpdatePage.setContactDesignationInput('contactDesignation'),
    ]);

    await institutionContactDetailsUpdatePage.save();
    expect(await institutionContactDetailsUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await institutionContactDetailsComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last InstitutionContactDetails', async () => {
    const nbButtonsBeforeDelete = await institutionContactDetailsComponentsPage.countDeleteButtons();
    await institutionContactDetailsComponentsPage.clickOnLastDeleteButton();

    institutionContactDetailsDeleteDialog = new InstitutionContactDetailsDeleteDialog();
    expect(await institutionContactDetailsDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Institution Contact Details?'
    );
    await institutionContactDetailsDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(institutionContactDetailsComponentsPage.title), 5000);

    expect(await institutionContactDetailsComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
