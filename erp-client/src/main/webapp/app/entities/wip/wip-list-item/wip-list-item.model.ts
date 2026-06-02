import * as dayjs from 'dayjs';

export interface IWIPListItem {
  id?: number;
  sequenceNumber?: string | null;
  particulars?: string | null;
  instalmentDate?: dayjs.Dayjs | null;
  instalmentAmount?: number | null;
  settlementCurrency?: string | null;
  outletCode?: string | null;
  settlementTransaction?: string | null;
  settlementTransactionDate?: dayjs.Dayjs | null;
  dealerName?: string | null;
  workProject?: string | null;
}

export class WIPListItem implements IWIPListItem {
  constructor(
    public id?: number,
    public sequenceNumber?: string | null,
    public particulars?: string | null,
    public instalmentDate?: dayjs.Dayjs | null,
    public instalmentAmount?: number | null,
    public settlementCurrency?: string | null,
    public outletCode?: string | null,
    public settlementTransaction?: string | null,
    public settlementTransactionDate?: dayjs.Dayjs | null,
    public dealerName?: string | null,
    public workProject?: string | null
  ) {}
}

export function getWIPListItemIdentifier(wIPListItem: IWIPListItem): number | undefined {
  return wIPListItem.id;
}
