export interface IFraudType {
  id?: number;
  fraudTypeCode?: string;
  fraudType?: string;
  fraudTypeDetails?: string | null;
}

export class FraudType implements IFraudType {
  constructor(public id?: number, public fraudTypeCode?: string, public fraudType?: string, public fraudTypeDetails?: string | null) {}
}

export function getFraudTypeIdentifier(fraudType: IFraudType): number | undefined {
  return fraudType.id;
}
