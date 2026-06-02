import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';

export interface IReportTemplate {
  id?: number;
  catalogueNumber?: string;
  description?: string | null;
  notesContentType?: string | null;
  notes?: string | null;
  reportFileContentType?: string | null;
  reportFile?: string | null;
  compileReportFileContentType?: string | null;
  compileReportFile?: string | null;
  placeholders?: IPlaceholder[] | null;
}

export class ReportTemplate implements IReportTemplate {
  constructor(
    public id?: number,
    public catalogueNumber?: string,
    public description?: string | null,
    public notesContentType?: string | null,
    public notes?: string | null,
    public reportFileContentType?: string | null,
    public reportFile?: string | null,
    public compileReportFileContentType?: string | null,
    public compileReportFile?: string | null,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getReportTemplateIdentifier(reportTemplate: IReportTemplate): number | undefined {
  return reportTemplate.id;
}
