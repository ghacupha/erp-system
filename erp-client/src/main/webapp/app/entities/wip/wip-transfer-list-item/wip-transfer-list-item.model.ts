import * as dayjs from 'dayjs';

export interface IWIPTransferListItem {
  id?: number;
  wipSequence?: number | null;
  wipParticulars?: string | null;
  transferType?: string | null;
  transferSettlement?: string | null;
  transferSettlementDate?: dayjs.Dayjs | null;
  transferAmount?: number | null;
  wipTransferDate?: dayjs.Dayjs | null;
  originalSettlement?: string | null;
  originalSettlementDate?: dayjs.Dayjs | null;
  assetCategory?: string | null;
  serviceOutlet?: string | null;
  workProject?: string | null;
}

export class WIPTransferListItem implements IWIPTransferListItem {
  constructor(
    public id?: number,
    public wipSequence?: number | null,
    public wipParticulars?: string | null,
    public transferType?: string | null,
    public transferSettlement?: string | null,
    public transferSettlementDate?: dayjs.Dayjs | null,
    public transferAmount?: number | null,
    public wipTransferDate?: dayjs.Dayjs | null,
    public originalSettlement?: string | null,
    public originalSettlementDate?: dayjs.Dayjs | null,
    public assetCategory?: string | null,
    public serviceOutlet?: string | null,
    public workProject?: string | null
  ) {}
}

export function getWIPTransferListItemIdentifier(wIPTransferListItem: IWIPTransferListItem): number | undefined {
  return wIPTransferListItem.id;
}
