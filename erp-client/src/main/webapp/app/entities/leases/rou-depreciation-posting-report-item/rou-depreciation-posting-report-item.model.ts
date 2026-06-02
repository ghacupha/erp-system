export interface IRouDepreciationPostingReportItem {
  id?: number;
  leaseContractNumber?: string | null;
  leaseDescription?: string | null;
  fiscalMonthCode?: string | null;
  accountForCredit?: string | null;
  accountForDebit?: string | null;
  depreciationAmount?: number | null;
}

export class RouDepreciationPostingReportItem implements IRouDepreciationPostingReportItem {
  constructor(
    public id?: number,
    public leaseContractNumber?: string | null,
    public leaseDescription?: string | null,
    public fiscalMonthCode?: string | null,
    public accountForCredit?: string | null,
    public accountForDebit?: string | null,
    public depreciationAmount?: number | null
  ) {}
}

export function getRouDepreciationPostingReportItemIdentifier(
  rouDepreciationPostingReportItem: IRouDepreciationPostingReportItem
): number | undefined {
  return rouDepreciationPostingReportItem.id;
}
