import * as dayjs from 'dayjs';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';
import { IBusinessDocument } from 'app/entities/documentation/business-document/business-document.model';
import { IContractMetadata } from 'app/entities/contract/contract-metadata/contract-metadata.model';

export interface ILeaseContract {
  id?: number;
  bookingId?: string;
  leaseTitle?: string;
  identifier?: string;
  description?: string | null;
  commencementDate?: dayjs.Dayjs;
  terminalDate?: dayjs.Dayjs;
  placeholders?: IPlaceholder[] | null;
  systemMappings?: IUniversallyUniqueMapping[] | null;
  businessDocuments?: IBusinessDocument[] | null;
  contractMetadata?: IContractMetadata[] | null;
}

export class LeaseContract implements ILeaseContract {
  constructor(
    public id?: number,
    public bookingId?: string,
    public leaseTitle?: string,
    public identifier?: string,
    public description?: string | null,
    public commencementDate?: dayjs.Dayjs,
    public terminalDate?: dayjs.Dayjs,
    public placeholders?: IPlaceholder[] | null,
    public systemMappings?: IUniversallyUniqueMapping[] | null,
    public businessDocuments?: IBusinessDocument[] | null,
    public contractMetadata?: IContractMetadata[] | null
  ) {}
}

export function getLeaseContractIdentifier(leaseContract: ILeaseContract): number | undefined {
  return leaseContract.id;
}
