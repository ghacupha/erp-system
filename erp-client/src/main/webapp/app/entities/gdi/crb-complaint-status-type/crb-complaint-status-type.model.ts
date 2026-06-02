export interface ICrbComplaintStatusType {
  id?: number;
  complaintStatusTypeCode?: string;
  complaintStatusType?: string;
  complaintStatusDetails?: string | null;
}

export class CrbComplaintStatusType implements ICrbComplaintStatusType {
  constructor(
    public id?: number,
    public complaintStatusTypeCode?: string,
    public complaintStatusType?: string,
    public complaintStatusDetails?: string | null
  ) {}
}

export function getCrbComplaintStatusTypeIdentifier(crbComplaintStatusType: ICrbComplaintStatusType): number | undefined {
  return crbComplaintStatusType.id;
}
