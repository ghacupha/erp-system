import * as dayjs from 'dayjs';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';

export interface IAssetAdditionsReport {
  id?: number;
  timeOfRequest?: dayjs.Dayjs | null;
  reportStartDate?: dayjs.Dayjs | null;
  reportEndDate?: dayjs.Dayjs | null;
  requestId?: string | null;
  tampered?: boolean | null;
  reportParameters?: string | null;
  reportFileContentType?: string | null;
  reportFile?: string | null;
  requestedBy?: IApplicationUser | null;
}

export class AssetAdditionsReport implements IAssetAdditionsReport {
  constructor(
    public id?: number,
    public timeOfRequest?: dayjs.Dayjs | null,
    public reportStartDate?: dayjs.Dayjs | null,
    public reportEndDate?: dayjs.Dayjs | null,
    public requestId?: string | null,
    public tampered?: boolean | null,
    public reportParameters?: string | null,
    public reportFileContentType?: string | null,
    public reportFile?: string | null,
    public requestedBy?: IApplicationUser | null
  ) {
    this.tampered = this.tampered ?? false;
  }
}

export function getAssetAdditionsReportIdentifier(assetAdditionsReport: IAssetAdditionsReport): number | undefined {
  return assetAdditionsReport.id;
}
