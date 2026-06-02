export interface ICrbComplaintType {
  id?: number;
  complaintTypeCode?: string;
  complaintType?: string;
  complaintTypeDetails?: string | null;
}

export class CrbComplaintType implements ICrbComplaintType {
  constructor(
    public id?: number,
    public complaintTypeCode?: string,
    public complaintType?: string,
    public complaintTypeDetails?: string | null
  ) {}
}

export function getCrbComplaintTypeIdentifier(crbComplaintType: ICrbComplaintType): number | undefined {
  return crbComplaintType.id;
}
