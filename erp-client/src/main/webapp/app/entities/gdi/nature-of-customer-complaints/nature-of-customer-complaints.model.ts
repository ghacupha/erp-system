export interface INatureOfCustomerComplaints {
  id?: number;
  natureOfComplaintTypeCode?: string;
  natureOfComplaintType?: string;
  natureOfComplaintTypeDetails?: string | null;
}

export class NatureOfCustomerComplaints implements INatureOfCustomerComplaints {
  constructor(
    public id?: number,
    public natureOfComplaintTypeCode?: string,
    public natureOfComplaintType?: string,
    public natureOfComplaintTypeDetails?: string | null
  ) {}
}

export function getNatureOfCustomerComplaintsIdentifier(natureOfCustomerComplaints: INatureOfCustomerComplaints): number | undefined {
  return natureOfCustomerComplaints.id;
}
