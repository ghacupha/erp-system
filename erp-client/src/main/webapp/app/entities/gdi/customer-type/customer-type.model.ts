export interface ICustomerType {
  id?: number;
  customerTypeCode?: string | null;
  customerType?: string | null;
  customerTypeDescription?: string | null;
}

export class CustomerType implements ICustomerType {
  constructor(
    public id?: number,
    public customerTypeCode?: string | null,
    public customerType?: string | null,
    public customerTypeDescription?: string | null
  ) {}
}

export function getCustomerTypeIdentifier(customerType: ICustomerType): number | undefined {
  return customerType.id;
}
