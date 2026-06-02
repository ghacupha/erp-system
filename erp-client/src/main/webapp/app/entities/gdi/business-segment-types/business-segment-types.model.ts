export interface IBusinessSegmentTypes {
  id?: number;
  businessEconomicSegmentCode?: string;
  businessEconomicSegment?: string;
  details?: string | null;
}

export class BusinessSegmentTypes implements IBusinessSegmentTypes {
  constructor(
    public id?: number,
    public businessEconomicSegmentCode?: string,
    public businessEconomicSegment?: string,
    public details?: string | null
  ) {}
}

export function getBusinessSegmentTypesIdentifier(businessSegmentTypes: IBusinessSegmentTypes): number | undefined {
  return businessSegmentTypes.id;
}
