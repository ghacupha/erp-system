import * as dayjs from 'dayjs';
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';

export interface IAutonomousReport {
  id?: number;
  reportName?: string;
  reportParameters?: string | null;
  createdAt?: dayjs.Dayjs;
  reportFileContentType?: string | null;
  reportFile?: string | null;
  reportTampered?: boolean | null;
  reportMappings?: IUniversallyUniqueMapping[] | null;
  placeholders?: IPlaceholder[] | null;
  createdBy?: IApplicationUser | null;
}

export class AutonomousReport implements IAutonomousReport {
  constructor(
    public id?: number,
    public reportName?: string,
    public reportParameters?: string | null,
    public createdAt?: dayjs.Dayjs,
    public reportFileContentType?: string | null,
    public reportFile?: string | null,
    public reportTampered?: boolean | null,
    public reportMappings?: IUniversallyUniqueMapping[] | null,
    public placeholders?: IPlaceholder[] | null,
    public createdBy?: IApplicationUser | null
  ) {
    this.reportTampered = this.reportTampered ?? false;
  }
}

export function getAutonomousReportIdentifier(autonomousReport: IAutonomousReport): number | undefined {
  return autonomousReport.id;
}
