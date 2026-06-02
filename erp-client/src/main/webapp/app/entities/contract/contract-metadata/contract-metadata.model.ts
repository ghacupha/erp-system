import * as dayjs from 'dayjs';
import { IDealer } from 'app/entities/people/dealer/dealer.model';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { ISecurityClearance } from 'app/entities/people/security-clearance/security-clearance.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { IBusinessDocument } from 'app/entities/documentation/business-document/business-document.model';
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';
import { ContractType } from 'app/entities/enumerations/contract-type.model';
import { ContractStatus } from 'app/entities/enumerations/contract-status.model';

export interface IContractMetadata {
  id?: number;
  description?: string | null;
  typeOfContract?: ContractType;
  contractStatus?: ContractStatus;
  startDate?: dayjs.Dayjs;
  terminationDate?: dayjs.Dayjs;
  commentsAndAttachment?: string | null;
  contractTitle?: string;
  contractIdentifier?: string;
  contractIdentifierShort?: string;
  relatedContracts?: IContractMetadata[] | null;
  department?: IDealer | null;
  contractPartner?: IDealer | null;
  responsiblePerson?: IApplicationUser | null;
  signatories?: IApplicationUser[] | null;
  securityClearance?: ISecurityClearance | null;
  placeholders?: IPlaceholder[] | null;
  contractDocumentFiles?: IBusinessDocument[] | null;
  contractMappings?: IUniversallyUniqueMapping[] | null;
}

export class ContractMetadata implements IContractMetadata {
  constructor(
    public id?: number,
    public description?: string | null,
    public typeOfContract?: ContractType,
    public contractStatus?: ContractStatus,
    public startDate?: dayjs.Dayjs,
    public terminationDate?: dayjs.Dayjs,
    public commentsAndAttachment?: string | null,
    public contractTitle?: string,
    public contractIdentifier?: string,
    public contractIdentifierShort?: string,
    public relatedContracts?: IContractMetadata[] | null,
    public department?: IDealer | null,
    public contractPartner?: IDealer | null,
    public responsiblePerson?: IApplicationUser | null,
    public signatories?: IApplicationUser[] | null,
    public securityClearance?: ISecurityClearance | null,
    public placeholders?: IPlaceholder[] | null,
    public contractDocumentFiles?: IBusinessDocument[] | null,
    public contractMappings?: IUniversallyUniqueMapping[] | null
  ) {}
}

export function getContractMetadataIdentifier(contractMetadata: IContractMetadata): number | undefined {
  return contractMetadata.id;
}
