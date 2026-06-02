import * as dayjs from 'dayjs';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { IDepreciationPeriod } from 'app/entities/assets/depreciation-period/depreciation-period.model';
import { IServiceOutlet } from 'app/entities/system/service-outlet/service-outlet.model';
import { IAssetCategory } from 'app/entities/assets/asset-category/asset-category.model';

export interface IDepreciationReport {
  id?: number;
  reportName?: string;
  timeOfReportRequest?: dayjs.Dayjs;
  fileChecksum?: string | null;
  tampered?: boolean | null;
  filename?: string | null;
  reportParameters?: string | null;
  reportFileContentType?: string | null;
  reportFile?: string | null;
  requestedBy?: IApplicationUser | null;
  depreciationPeriod?: IDepreciationPeriod;
  serviceOutlet?: IServiceOutlet | null;
  assetCategory?: IAssetCategory | null;
}

export class DepreciationReport implements IDepreciationReport {
  constructor(
    public id?: number,
    public reportName?: string,
    public timeOfReportRequest?: dayjs.Dayjs,
    public fileChecksum?: string | null,
    public tampered?: boolean | null,
    public filename?: string | null,
    public reportParameters?: string | null,
    public reportFileContentType?: string | null,
    public reportFile?: string | null,
    public requestedBy?: IApplicationUser | null,
    public depreciationPeriod?: IDepreciationPeriod,
    public serviceOutlet?: IServiceOutlet | null,
    public assetCategory?: IAssetCategory | null
  ) {
    this.tampered = this.tampered ?? false;
  }
}

export function getDepreciationReportIdentifier(depreciationReport: IDepreciationReport): number | undefined {
  return depreciationReport.id;
}
