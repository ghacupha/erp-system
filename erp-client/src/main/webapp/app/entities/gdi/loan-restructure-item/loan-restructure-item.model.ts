export interface ILoanRestructureItem {
  id?: number;
  loanRestructureItemCode?: string;
  loanRestructureItemType?: string;
  loanRestructureItemDetails?: string | null;
}

export class LoanRestructureItem implements ILoanRestructureItem {
  constructor(
    public id?: number,
    public loanRestructureItemCode?: string,
    public loanRestructureItemType?: string,
    public loanRestructureItemDetails?: string | null
  ) {}
}

export function getLoanRestructureItemIdentifier(loanRestructureItem: ILoanRestructureItem): number | undefined {
  return loanRestructureItem.id;
}
