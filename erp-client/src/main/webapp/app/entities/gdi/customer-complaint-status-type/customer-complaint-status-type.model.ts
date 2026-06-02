export interface ICustomerComplaintStatusType {
  id?: number;
  customerComplaintStatusTypeCode?: string;
  customerComplaintStatusType?: string;
  customerComplaintStatusTypeDetails?: string | null;
}

export class CustomerComplaintStatusType implements ICustomerComplaintStatusType {
  constructor(
    public id?: number,
    public customerComplaintStatusTypeCode?: string,
    public customerComplaintStatusType?: string,
    public customerComplaintStatusTypeDetails?: string | null
  ) {}
}

export function getCustomerComplaintStatusTypeIdentifier(customerComplaintStatusType: ICustomerComplaintStatusType): number | undefined {
  return customerComplaintStatusType.id;
}
