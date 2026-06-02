import { ShareHolderTypes } from 'app/entities/enumerations/share-holder-types.model';

export interface IShareholderType {
  id?: number;
  shareHolderTypeCode?: string;
  shareHolderType?: ShareHolderTypes;
}

export class ShareholderType implements IShareholderType {
  constructor(public id?: number, public shareHolderTypeCode?: string, public shareHolderType?: ShareHolderTypes) {}
}

export function getShareholderTypeIdentifier(shareholderType: IShareholderType): number | undefined {
  return shareholderType.id;
}
