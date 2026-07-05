import * as dayjs from 'dayjs';
import { IDealer } from 'app/entities/people/dealer/dealer.model';
import { IBusinessStamp } from 'app/entities/settlement/business-stamp/business-stamp.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { IPaymentLabel } from 'app/entities/settlement/payment-label/payment-label.model';
import { IBusinessDocument } from 'app/entities/documentation/business-document/business-document.model';

export interface IJobSheet {
  id?: number;
  serialNumber?: string;
  jobSheetDate?: dayjs.Dayjs | null;
  details?: string | null;
  remarks?: string | null;
  biller?: IDealer;
  signatories?: IDealer[] | null;
  contactPerson?: IDealer | null;
  businessStamps?: IBusinessStamp[] | null;
  placeholders?: IPlaceholder[] | null;
  paymentLabels?: IPaymentLabel[] | null;
  businessDocuments?: IBusinessDocument[] | null;
}

export class JobSheet implements IJobSheet {
  constructor(
    public id?: number,
    public serialNumber?: string,
    public jobSheetDate?: dayjs.Dayjs | null,
    public details?: string | null,
    public remarks?: string | null,
    public biller?: IDealer,
    public signatories?: IDealer[] | null,
    public contactPerson?: IDealer | null,
    public businessStamps?: IBusinessStamp[] | null,
    public placeholders?: IPlaceholder[] | null,
    public paymentLabels?: IPaymentLabel[] | null,
    public businessDocuments?: IBusinessDocument[] | null
  ) {}
}

export function getJobSheetIdentifier(jobSheet: IJobSheet): number | undefined {
  return jobSheet.id;
}
