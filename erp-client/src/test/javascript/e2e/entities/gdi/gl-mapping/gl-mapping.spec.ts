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

import { GlMappingComponentsPage, GlMappingDeleteDialog, GlMappingUpdatePage } from './gl-mapping.page-object';

const expect = chai.expect;

describe('GlMapping e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let glMappingComponentsPage: GlMappingComponentsPage;
  let glMappingUpdatePage: GlMappingUpdatePage;
  let glMappingDeleteDialog: GlMappingDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load GlMappings', async () => {
    await navBarPage.goToEntity('gl-mapping');
    glMappingComponentsPage = new GlMappingComponentsPage();
    await browser.wait(ec.visibilityOf(glMappingComponentsPage.title), 5000);
    expect(await glMappingComponentsPage.getTitle()).to.eq('Gl Mappings');
    await browser.wait(ec.or(ec.visibilityOf(glMappingComponentsPage.entities), ec.visibilityOf(glMappingComponentsPage.noResult)), 1000);
  });

  it('should load create GlMapping page', async () => {
    await glMappingComponentsPage.clickOnCreateButton();
    glMappingUpdatePage = new GlMappingUpdatePage();
    expect(await glMappingUpdatePage.getPageTitle()).to.eq('Create or edit a Gl Mapping');
    await glMappingUpdatePage.cancel();
  });

  it('should create and save GlMappings', async () => {
    const nbButtonsBeforeCreate = await glMappingComponentsPage.countDeleteButtons();

    await glMappingComponentsPage.clickOnCreateButton();

    await promise.all([
      glMappingUpdatePage.setSubGLCodeInput('subGLCode'),
      glMappingUpdatePage.setSubGLDescriptionInput('subGLDescription'),
      glMappingUpdatePage.setMainGLCodeInput('mainGLCode'),
      glMappingUpdatePage.setMainGLDescriptionInput('mainGLDescription'),
      glMappingUpdatePage.setGlTypeInput('glType'),
    ]);

    await glMappingUpdatePage.save();
    expect(await glMappingUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await glMappingComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last GlMapping', async () => {
    const nbButtonsBeforeDelete = await glMappingComponentsPage.countDeleteButtons();
    await glMappingComponentsPage.clickOnLastDeleteButton();

    glMappingDeleteDialog = new GlMappingDeleteDialog();
    expect(await glMappingDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Gl Mapping?');
    await glMappingDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(glMappingComponentsPage.title), 5000);

    expect(await glMappingComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
