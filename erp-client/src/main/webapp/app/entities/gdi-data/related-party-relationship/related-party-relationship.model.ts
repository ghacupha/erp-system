import * as dayjs from 'dayjs';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { IBankBranchCode } from 'app/entities/system/bank-branch-code/bank-branch-code.model';
import { IPartyRelationType } from 'app/entities/gdi/party-relation-type/party-relation-type.model';

export interface IRelatedPartyRelationship {
  id?: number;
  reportingDate?: dayjs.Dayjs;
  customerId?: string;
  relatedPartyId?: string;
  bankCode?: IInstitutionCode;
  branchId?: IBankBranchCode;
  relationshipType?: IPartyRelationType;
}

export class RelatedPartyRelationship implements IRelatedPartyRelationship {
  constructor(
    public id?: number,
    public reportingDate?: dayjs.Dayjs,
    public customerId?: string,
    public relatedPartyId?: string,
    public bankCode?: IInstitutionCode,
    public branchId?: IBankBranchCode,
    public relationshipType?: IPartyRelationType
  ) {}
}

export function getRelatedPartyRelationshipIdentifier(relatedPartyRelationship: IRelatedPartyRelationship): number | undefined {
  return relatedPartyRelationship.id;
}
