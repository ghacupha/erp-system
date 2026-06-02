import { ShareholdingFlagTypes } from 'app/entities/enumerations/shareholding-flag-types.model';

export interface IShareHoldingFlag {
  id?: number;
  shareholdingFlagTypeCode?: ShareholdingFlagTypes;
  shareholdingFlagType?: string;
  shareholdingTypeDescription?: string | null;
}

export class ShareHoldingFlag implements IShareHoldingFlag {
  constructor(
    public id?: number,
    public shareholdingFlagTypeCode?: ShareholdingFlagTypes,
    public shareholdingFlagType?: string,
    public shareholdingTypeDescription?: string | null
  ) {}
}

export function getShareHoldingFlagIdentifier(shareHoldingFlag: IShareHoldingFlag): number | undefined {
  return shareHoldingFlag.id;
}
