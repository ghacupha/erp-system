export interface ICrbReportRequestReasons {
  id?: number;
  creditReportRequestReasonTypeCode?: string;
  creditReportRequestReasonType?: string;
  creditReportRequestDetails?: string | null;
}

export class CrbReportRequestReasons implements ICrbReportRequestReasons {
  constructor(
    public id?: number,
    public creditReportRequestReasonTypeCode?: string,
    public creditReportRequestReasonType?: string,
    public creditReportRequestDetails?: string | null
  ) {}
}

export function getCrbReportRequestReasonsIdentifier(crbReportRequestReasons: ICrbReportRequestReasons): number | undefined {
  return crbReportRequestReasons.id;
}
