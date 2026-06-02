import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';

export interface IOutletType {
  id?: number;
  outletTypeCode?: string;
  outletType?: string;
  outletTypeDetails?: string | null;
  placeholders?: IPlaceholder[] | null;
}

export class OutletType implements IOutletType {
  constructor(
    public id?: number,
    public outletTypeCode?: string,
    public outletType?: string,
    public outletTypeDetails?: string | null,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getOutletTypeIdentifier(outletType: IOutletType): number | undefined {
  return outletType.id;
}
