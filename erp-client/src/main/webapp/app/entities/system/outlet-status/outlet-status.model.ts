import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { BranchStatusType } from 'app/entities/enumerations/branch-status-type.model';

export interface IOutletStatus {
  id?: number;
  branchStatusTypeCode?: string;
  branchStatusType?: BranchStatusType;
  branchStatusTypeDescription?: string | null;
  placeholders?: IPlaceholder[] | null;
}

export class OutletStatus implements IOutletStatus {
  constructor(
    public id?: number,
    public branchStatusTypeCode?: string,
    public branchStatusType?: BranchStatusType,
    public branchStatusTypeDescription?: string | null,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getOutletStatusIdentifier(outletStatus: IOutletStatus): number | undefined {
  return outletStatus.id;
}
