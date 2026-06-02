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
