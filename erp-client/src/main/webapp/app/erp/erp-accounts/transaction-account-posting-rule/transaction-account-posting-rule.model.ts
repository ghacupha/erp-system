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

import { ITransactionAccount } from '../transaction-account/transaction-account.model';
import { ITransactionAccountCategory } from 'app/entities/accounting/transaction-account-category/transaction-account-category.model';
import { IPlaceholder } from '../../erp-pages/placeholder/placeholder.model';

export interface ITransactionAccountPostingRuleTemplate {
  id?: number;
  lineDescription?: string | null;
  amountMultiplier?: number | null;
  debitAccount?: ITransactionAccount | null;
  creditAccount?: ITransactionAccount | null;
}

export interface ITransactionAccountPostingRuleCondition {
  id?: number;
  conditionKey?: string;
  conditionOperator?: string;
  conditionValue?: string;
}

export interface ITransactionAccountPostingRule {
  id?: number;
  name?: string;
  identifier?: string;
  debitAccountType?: ITransactionAccountCategory | null;
  creditAccountType?: ITransactionAccountCategory | null;
  transactionContext?: IPlaceholder | null;
  module?: string | null;
  eventType?: string | null;
  varianceType?: string | null;
  invoiceTiming?: string | null;
  postingRuleConditions?: ITransactionAccountPostingRuleCondition[] | null;
  postingRuleTemplates?: ITransactionAccountPostingRuleTemplate[] | null;
}

export class TransactionAccountPostingRule implements ITransactionAccountPostingRule {
  constructor(
    public id?: number,
    public name?: string,
    public identifier?: string,
    public debitAccountType?: ITransactionAccountCategory | null,
    public creditAccountType?: ITransactionAccountCategory | null,
    public transactionContext?: IPlaceholder | null,
    public module?: string | null,
    public eventType?: string | null,
    public varianceType?: string | null,
    public invoiceTiming?: string | null,
    public postingRuleConditions?: ITransactionAccountPostingRuleCondition[] | null,
    public postingRuleTemplates?: ITransactionAccountPostingRuleTemplate[] | null
  ) {}
}

export function getTransactionAccountPostingRuleIdentifier(rule: ITransactionAccountPostingRule): number | undefined {
  return rule.id;
}
