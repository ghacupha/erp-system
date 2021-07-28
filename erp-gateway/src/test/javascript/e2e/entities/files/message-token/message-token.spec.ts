///
/// Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
///

import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { MessageTokenComponentsPage, MessageTokenDeleteDialog, MessageTokenUpdatePage } from './message-token.page-object';

const expect = chai.expect;

describe('MessageToken e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let messageTokenComponentsPage: MessageTokenComponentsPage;
  let messageTokenUpdatePage: MessageTokenUpdatePage;
  let messageTokenDeleteDialog: MessageTokenDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load MessageTokens', async () => {
    await navBarPage.goToEntity('message-token');
    messageTokenComponentsPage = new MessageTokenComponentsPage();
    await browser.wait(ec.visibilityOf(messageTokenComponentsPage.title), 5000);
    expect(await messageTokenComponentsPage.getTitle()).to.eq('Message Tokens');
    await browser.wait(
      ec.or(ec.visibilityOf(messageTokenComponentsPage.entities), ec.visibilityOf(messageTokenComponentsPage.noResult)),
      1000
    );
  });

  it('should load create MessageToken page', async () => {
    await messageTokenComponentsPage.clickOnCreateButton();
    messageTokenUpdatePage = new MessageTokenUpdatePage();
    expect(await messageTokenUpdatePage.getPageTitle()).to.eq('Create or edit a Message Token');
    await messageTokenUpdatePage.cancel();
  });

  it('should create and save MessageTokens', async () => {
    const nbButtonsBeforeCreate = await messageTokenComponentsPage.countDeleteButtons();

    await messageTokenComponentsPage.clickOnCreateButton();

    await promise.all([
      messageTokenUpdatePage.setDescriptionInput('description'),
      messageTokenUpdatePage.setTimeSentInput('5'),
      messageTokenUpdatePage.setTokenValueInput('tokenValue'),
    ]);

    expect(await messageTokenUpdatePage.getDescriptionInput()).to.eq(
      'description',
      'Expected Description value to be equals to description'
    );
    expect(await messageTokenUpdatePage.getTimeSentInput()).to.eq('5', 'Expected timeSent value to be equals to 5');
    expect(await messageTokenUpdatePage.getTokenValueInput()).to.eq('tokenValue', 'Expected TokenValue value to be equals to tokenValue');
    const selectedReceived = messageTokenUpdatePage.getReceivedInput();
    if (await selectedReceived.isSelected()) {
      await messageTokenUpdatePage.getReceivedInput().click();
      expect(await messageTokenUpdatePage.getReceivedInput().isSelected(), 'Expected received not to be selected').to.be.false;
    } else {
      await messageTokenUpdatePage.getReceivedInput().click();
      expect(await messageTokenUpdatePage.getReceivedInput().isSelected(), 'Expected received to be selected').to.be.true;
    }
    const selectedActioned = messageTokenUpdatePage.getActionedInput();
    if (await selectedActioned.isSelected()) {
      await messageTokenUpdatePage.getActionedInput().click();
      expect(await messageTokenUpdatePage.getActionedInput().isSelected(), 'Expected actioned not to be selected').to.be.false;
    } else {
      await messageTokenUpdatePage.getActionedInput().click();
      expect(await messageTokenUpdatePage.getActionedInput().isSelected(), 'Expected actioned to be selected').to.be.true;
    }
    const selectedContentFullyEnqueued = messageTokenUpdatePage.getContentFullyEnqueuedInput();
    if (await selectedContentFullyEnqueued.isSelected()) {
      await messageTokenUpdatePage.getContentFullyEnqueuedInput().click();
      expect(await messageTokenUpdatePage.getContentFullyEnqueuedInput().isSelected(), 'Expected contentFullyEnqueued not to be selected')
        .to.be.false;
    } else {
      await messageTokenUpdatePage.getContentFullyEnqueuedInput().click();
      expect(await messageTokenUpdatePage.getContentFullyEnqueuedInput().isSelected(), 'Expected contentFullyEnqueued to be selected').to.be
        .true;
    }

    await messageTokenUpdatePage.save();
    expect(await messageTokenUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await messageTokenComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last MessageToken', async () => {
    const nbButtonsBeforeDelete = await messageTokenComponentsPage.countDeleteButtons();
    await messageTokenComponentsPage.clickOnLastDeleteButton();

    messageTokenDeleteDialog = new MessageTokenDeleteDialog();
    expect(await messageTokenDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Message Token?');
    await messageTokenDeleteDialog.clickOnConfirmButton();

    expect(await messageTokenComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
