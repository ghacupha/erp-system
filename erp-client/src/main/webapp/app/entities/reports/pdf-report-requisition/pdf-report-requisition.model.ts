import * as dayjs from 'dayjs';
import { IReportTemplate } from 'app/entities/reports/report-template/report-template.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';
import { ReportStatusTypes } from 'app/entities/enumerations/report-status-types.model';

export interface IPdfReportRequisition {
  id?: number;
  reportName?: string;
  reportDate?: dayjs.Dayjs | null;
  userPassword?: string;
  ownerPassword?: string;
  reportFileChecksum?: string | null;
  reportStatus?: ReportStatusTypes | null;
  reportId?: string;
  reportTemplate?: IReportTemplate;
  placeholders?: IPlaceholder[] | null;
  parameters?: IUniversallyUniqueMapping[] | null;
}

export class PdfReportRequisition implements IPdfReportRequisition {
  constructor(
    public id?: number,
    public reportName?: string,
    public reportDate?: dayjs.Dayjs | null,
    public userPassword?: string,
    public ownerPassword?: string,
    public reportFileChecksum?: string | null,
    public reportStatus?: ReportStatusTypes | null,
    public reportId?: string,
    public reportTemplate?: IReportTemplate,
    public placeholders?: IPlaceholder[] | null,
    public parameters?: IUniversallyUniqueMapping[] | null
  ) {}
}

export function getPdfReportRequisitionIdentifier(pdfReportRequisition: IPdfReportRequisition): number | undefined {
  return pdfReportRequisition.id;
}
