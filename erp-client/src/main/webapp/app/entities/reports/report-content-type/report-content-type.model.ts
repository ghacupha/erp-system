import { ISystemContentType } from 'app/entities/system/system-content-type/system-content-type.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';

export interface IReportContentType {
  id?: number;
  reportTypeName?: string;
  reportFileExtension?: string;
  systemContentType?: ISystemContentType;
  placeholders?: IPlaceholder[] | null;
}

export class ReportContentType implements IReportContentType {
  constructor(
    public id?: number,
    public reportTypeName?: string,
    public reportFileExtension?: string,
    public systemContentType?: ISystemContentType,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getReportContentTypeIdentifier(reportContentType: IReportContentType): number | undefined {
  return reportContentType.id;
}
