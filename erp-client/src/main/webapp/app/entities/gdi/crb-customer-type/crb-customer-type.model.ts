export interface ICrbCustomerType {
  id?: number;
  customerTypeCode?: string;
  customerType?: string;
  description?: string | null;
}

export class CrbCustomerType implements ICrbCustomerType {
  constructor(public id?: number, public customerTypeCode?: string, public customerType?: string, public description?: string | null) {}
}

export function getCrbCustomerTypeIdentifier(crbCustomerType: ICrbCustomerType): number | undefined {
  return crbCustomerType.id;
}
