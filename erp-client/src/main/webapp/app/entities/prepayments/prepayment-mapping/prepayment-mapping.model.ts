import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';

export interface IPrepaymentMapping {
  id?: number;
  parameterKey?: string;
  parameterGuid?: string;
  parameter?: string;
  placeholders?: IPlaceholder[] | null;
}

export class PrepaymentMapping implements IPrepaymentMapping {
  constructor(
    public id?: number,
    public parameterKey?: string,
    public parameterGuid?: string,
    public parameter?: string,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getPrepaymentMappingIdentifier(prepaymentMapping: IPrepaymentMapping): number | undefined {
  return prepaymentMapping.id;
}
