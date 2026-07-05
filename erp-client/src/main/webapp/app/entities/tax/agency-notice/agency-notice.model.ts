import * as dayjs from 'dayjs';
import { IDealer } from 'app/entities/people/dealer/dealer.model';
import { ISettlementCurrency } from 'app/entities/system/settlement-currency/settlement-currency.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { IBusinessDocument } from 'app/entities/documentation/business-document/business-document.model';
import { AgencyStatusType } from 'app/entities/enumerations/agency-status-type.model';

export interface IAgencyNotice {
  id?: number;
  referenceNumber?: string;
  referenceDate?: dayjs.Dayjs | null;
  assessmentAmount?: number;
  agencyStatus?: AgencyStatusType;
  assessmentNoticeContentType?: string | null;
  assessmentNotice?: string | null;
  correspondents?: IDealer[] | null;
  settlementCurrency?: ISettlementCurrency | null;
  assessor?: IDealer | null;
  placeholders?: IPlaceholder[] | null;
  businessDocuments?: IBusinessDocument[] | null;
}

export class AgencyNotice implements IAgencyNotice {
  constructor(
    public id?: number,
    public referenceNumber?: string,
    public referenceDate?: dayjs.Dayjs | null,
    public assessmentAmount?: number,
    public agencyStatus?: AgencyStatusType,
    public assessmentNoticeContentType?: string | null,
    public assessmentNotice?: string | null,
    public correspondents?: IDealer[] | null,
    public settlementCurrency?: ISettlementCurrency | null,
    public assessor?: IDealer | null,
    public placeholders?: IPlaceholder[] | null,
    public businessDocuments?: IBusinessDocument[] | null
  ) {}
}

export function getAgencyNoticeIdentifier(agencyNotice: IAgencyNotice): number | undefined {
  return agencyNotice.id;
}
