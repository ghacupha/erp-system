import * as dayjs from 'dayjs';
import { IReportTemplate } from 'app/entities/reports/report-template/report-template.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';
import { ReportStatusTypes } from 'app/entities/enumerations/report-status-types.model';

export interface IXlsxReportRequisition {
  id?: number;
  reportName?: string;
  reportDate?: dayjs.Dayjs | null;
  userPassword?: string;
  reportFileChecksum?: string | null;
  reportStatus?: ReportStatusTypes | null;
  reportId?: string;
  reportTemplate?: IReportTemplate;
  placeholders?: IPlaceholder[] | null;
  parameters?: IUniversallyUniqueMapping[] | null;
}

export class XlsxReportRequisition implements IXlsxReportRequisition {
  constructor(
    public id?: number,
    public reportName?: string,
    public reportDate?: dayjs.Dayjs | null,
    public userPassword?: string,
    public reportFileChecksum?: string | null,
    public reportStatus?: ReportStatusTypes | null,
    public reportId?: string,
    public reportTemplate?: IReportTemplate,
    public placeholders?: IPlaceholder[] | null,
    public parameters?: IUniversallyUniqueMapping[] | null
  ) {}
}

export function getXlsxReportRequisitionIdentifier(xlsxReportRequisition: IXlsxReportRequisition): number | undefined {
  return xlsxReportRequisition.id;
}
