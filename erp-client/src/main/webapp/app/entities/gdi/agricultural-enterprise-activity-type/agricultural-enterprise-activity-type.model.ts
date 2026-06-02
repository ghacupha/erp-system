export interface IAgriculturalEnterpriseActivityType {
  id?: number;
  agriculturalEnterpriseActivityTypeCode?: string;
  agriculturalEnterpriseActivityType?: string;
  agriculturalEnterpriseActivityTypeDescription?: string | null;
}

export class AgriculturalEnterpriseActivityType implements IAgriculturalEnterpriseActivityType {
  constructor(
    public id?: number,
    public agriculturalEnterpriseActivityTypeCode?: string,
    public agriculturalEnterpriseActivityType?: string,
    public agriculturalEnterpriseActivityTypeDescription?: string | null
  ) {}
}

export function getAgriculturalEnterpriseActivityTypeIdentifier(
  agriculturalEnterpriseActivityType: IAgriculturalEnterpriseActivityType
): number | undefined {
  return agriculturalEnterpriseActivityType.id;
}
