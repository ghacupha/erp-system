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

import { ChannelTypeComponentsPage, ChannelTypeDeleteDialog, ChannelTypeUpdatePage } from './channel-type.page-object';

const expect = chai.expect;

describe('ChannelType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let channelTypeComponentsPage: ChannelTypeComponentsPage;
  let channelTypeUpdatePage: ChannelTypeUpdatePage;
  let channelTypeDeleteDialog: ChannelTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ChannelTypes', async () => {
    await navBarPage.goToEntity('channel-type');
    channelTypeComponentsPage = new ChannelTypeComponentsPage();
    await browser.wait(ec.visibilityOf(channelTypeComponentsPage.title), 5000);
    expect(await channelTypeComponentsPage.getTitle()).to.eq('Channel Types');
    await browser.wait(
      ec.or(ec.visibilityOf(channelTypeComponentsPage.entities), ec.visibilityOf(channelTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create ChannelType page', async () => {
    await channelTypeComponentsPage.clickOnCreateButton();
    channelTypeUpdatePage = new ChannelTypeUpdatePage();
    expect(await channelTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Channel Type');
    await channelTypeUpdatePage.cancel();
  });

  it('should create and save ChannelTypes', async () => {
    const nbButtonsBeforeCreate = await channelTypeComponentsPage.countDeleteButtons();

    await channelTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      channelTypeUpdatePage.setChannelsTypeCodeInput('channelsTypeCode'),
      channelTypeUpdatePage.setChannelTypesInput('channelTypes'),
      channelTypeUpdatePage.setChannelTypeDetailsInput('channelTypeDetails'),
    ]);

    await channelTypeUpdatePage.save();
    expect(await channelTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await channelTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last ChannelType', async () => {
    const nbButtonsBeforeDelete = await channelTypeComponentsPage.countDeleteButtons();
    await channelTypeComponentsPage.clickOnLastDeleteButton();

    channelTypeDeleteDialog = new ChannelTypeDeleteDialog();
    expect(await channelTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Channel Type?');
    await channelTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(channelTypeComponentsPage.title), 5000);

    expect(await channelTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
