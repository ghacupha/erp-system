import { FlagCodes } from 'app/entities/enumerations/flag-codes.model';

export interface ILoanRestructureFlag {
  id?: number;
  loanRestructureFlagCode?: FlagCodes;
  loanRestructureFlagType?: string;
  loanRestructureFlagDetails?: string | null;
}

export class LoanRestructureFlag implements ILoanRestructureFlag {
  constructor(
    public id?: number,
    public loanRestructureFlagCode?: FlagCodes,
    public loanRestructureFlagType?: string,
    public loanRestructureFlagDetails?: string | null
  ) {}
}

export function getLoanRestructureFlagIdentifier(loanRestructureFlag: ILoanRestructureFlag): number | undefined {
  return loanRestructureFlag.id;
}
