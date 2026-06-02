export interface IAnticipatedMaturityPeriood {
  id?: number;
  anticipatedMaturityTenorCode?: string;
  aniticipatedMaturityTenorType?: string;
  anticipatedMaturityTenorDetails?: string | null;
}

export class AnticipatedMaturityPeriood implements IAnticipatedMaturityPeriood {
  constructor(
    public id?: number,
    public anticipatedMaturityTenorCode?: string,
    public aniticipatedMaturityTenorType?: string,
    public anticipatedMaturityTenorDetails?: string | null
  ) {}
}

export function getAnticipatedMaturityPerioodIdentifier(anticipatedMaturityPeriood: IAnticipatedMaturityPeriood): number | undefined {
  return anticipatedMaturityPeriood.id;
}
