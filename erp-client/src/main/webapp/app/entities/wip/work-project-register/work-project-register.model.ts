import { IDealer } from 'app/entities/people/dealer/dealer.model';
import { ISettlementCurrency } from 'app/entities/system/settlement-currency/settlement-currency.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { IBusinessDocument } from 'app/entities/documentation/business-document/business-document.model';

export interface IWorkProjectRegister {
  id?: number;
  catalogueNumber?: string;
  projectTitle?: string;
  description?: string | null;
  detailsContentType?: string | null;
  details?: string | null;
  totalProjectCost?: number | null;
  additionalNotesContentType?: string | null;
  additionalNotes?: string | null;
  dealers?: IDealer[];
  settlementCurrency?: ISettlementCurrency | null;
  placeholders?: IPlaceholder[] | null;
  businessDocuments?: IBusinessDocument[] | null;
}

export class WorkProjectRegister implements IWorkProjectRegister {
  constructor(
    public id?: number,
    public catalogueNumber?: string,
    public projectTitle?: string,
    public description?: string | null,
    public detailsContentType?: string | null,
    public details?: string | null,
    public totalProjectCost?: number | null,
    public additionalNotesContentType?: string | null,
    public additionalNotes?: string | null,
    public dealers?: IDealer[],
    public settlementCurrency?: ISettlementCurrency | null,
    public placeholders?: IPlaceholder[] | null,
    public businessDocuments?: IBusinessDocument[] | null
  ) {}
}

export function getWorkProjectRegisterIdentifier(workProjectRegister: IWorkProjectRegister): number | undefined {
  return workProjectRegister.id;
}
