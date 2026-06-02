import { element, by } from 'protractor';

export class AccountOwnershipTypeComponentsPage {
  title = element.all(by.css('jhi-account-ownership-type div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async getTitle(): Promise<string> {
    return this.title.getText();
  }
}
