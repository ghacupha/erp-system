export interface IGdiMasterDataIndex {
  id?: number;
  entityName?: string;
  databaseName?: string;
  businessDescription?: string | null;
}

export class GdiMasterDataIndex implements IGdiMasterDataIndex {
  constructor(public id?: number, public entityName?: string, public databaseName?: string, public businessDescription?: string | null) {}
}

export function getGdiMasterDataIndexIdentifier(gdiMasterDataIndex: IGdiMasterDataIndex): number | undefined {
  return gdiMasterDataIndex.id;
}
