export interface IAcquiringIssuingFlag {
  id?: number;
  cardAcquiringIssuingFlagCode?: string;
  cardAcquiringIssuingDescription?: string;
  cardAcquiringIssuingDetails?: string | null;
}

export class AcquiringIssuingFlag implements IAcquiringIssuingFlag {
  constructor(
    public id?: number,
    public cardAcquiringIssuingFlagCode?: string,
    public cardAcquiringIssuingDescription?: string,
    public cardAcquiringIssuingDetails?: string | null
  ) {}
}

export function getAcquiringIssuingFlagIdentifier(acquiringIssuingFlag: IAcquiringIssuingFlag): number | undefined {
  return acquiringIssuingFlag.id;
}
