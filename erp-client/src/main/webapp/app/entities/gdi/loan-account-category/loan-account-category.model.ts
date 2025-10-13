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

import { LoanAccountMutationTypes } from 'app/entities/enumerations/loan-account-mutation-types.model';

export interface ILoanAccountCategory {
  id?: number;
  loanAccountMutationCode?: string;
  loanAccountMutationType?: LoanAccountMutationTypes;
  loanAccountMutationDetails?: string;
  loanAccountMutationDescription?: string | null;
}

export class LoanAccountCategory implements ILoanAccountCategory {
  constructor(
    public id?: number,
    public loanAccountMutationCode?: string,
    public loanAccountMutationType?: LoanAccountMutationTypes,
    public loanAccountMutationDetails?: string,
    public loanAccountMutationDescription?: string | null
  ) {}
}

export function getLoanAccountCategoryIdentifier(loanAccountCategory: ILoanAccountCategory): number | undefined {
  return loanAccountCategory.id;
}
