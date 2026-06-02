import * as dayjs from 'dayjs';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { IBusinessDocument } from 'app/entities/documentation/business-document/business-document.model';
import { IAssetCategory } from 'app/entities/assets/asset-category/asset-category.model';
import { IWorkInProgressRegistration } from 'app/entities/wip/work-in-progress-registration/work-in-progress-registration.model';
import { IServiceOutlet } from 'app/entities/system/service-outlet/service-outlet.model';
import { ISettlement } from 'app/entities/settlement/settlement/settlement.model';
import { IWorkProjectRegister } from 'app/entities/wip/work-project-register/work-project-register.model';
import { WorkInProgressTransferType } from 'app/entities/enumerations/work-in-progress-transfer-type.model';

export interface IWorkInProgressTransfer {
  id?: number;
  description?: string | null;
  targetAssetNumber?: string | null;
  transferAmount?: number;
  transferDate?: dayjs.Dayjs;
  transferType?: WorkInProgressTransferType;
  placeholders?: IPlaceholder[] | null;
  businessDocuments?: IBusinessDocument[] | null;
  assetCategory?: IAssetCategory | null;
  workInProgressRegistration?: IWorkInProgressRegistration | null;
  serviceOutlet?: IServiceOutlet | null;
  transferSettlement?: ISettlement | null;
  originalSettlement?: ISettlement | null;
  workProjectRegister?: IWorkProjectRegister | null;
}

export class WorkInProgressTransfer implements IWorkInProgressTransfer {
  constructor(
    public id?: number,
    public description?: string | null,
    public targetAssetNumber?: string | null,
    public transferAmount?: number,
    public transferDate?: dayjs.Dayjs,
    public transferType?: WorkInProgressTransferType,
    public placeholders?: IPlaceholder[] | null,
    public businessDocuments?: IBusinessDocument[] | null,
    public assetCategory?: IAssetCategory | null,
    public workInProgressRegistration?: IWorkInProgressRegistration | null,
    public serviceOutlet?: IServiceOutlet | null,
    public transferSettlement?: ISettlement | null,
    public originalSettlement?: ISettlement | null,
    public workProjectRegister?: IWorkProjectRegister | null
  ) {}
}

export function getWorkInProgressTransferIdentifier(workInProgressTransfer: IWorkInProgressTransfer): number | undefined {
  return workInProgressTransfer.id;
}
