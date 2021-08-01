import * as dayjs from 'dayjs';
import { IPayment } from 'app/entities/payments/payment/payment.model';

export interface ITaxRule {
  id?: number;
  paymentNumber?: string;
  paymentDate?: dayjs.Dayjs;
  telcoExciseDuty?: number | null;
  valueAddedTax?: number | null;
  withholdingVAT?: number | null;
  withholdingTaxConsultancy?: number | null;
  withholdingTaxRent?: number | null;
  cateringLevy?: number | null;
  serviceCharge?: number | null;
  withholdingTaxImportedService?: number | null;
  payment?: IPayment | null;
}

export class TaxRule implements ITaxRule {
  constructor(
    public id?: number,
    public paymentNumber?: string,
    public paymentDate?: dayjs.Dayjs,
    public telcoExciseDuty?: number | null,
    public valueAddedTax?: number | null,
    public withholdingVAT?: number | null,
    public withholdingTaxConsultancy?: number | null,
    public withholdingTaxRent?: number | null,
    public cateringLevy?: number | null,
    public serviceCharge?: number | null,
    public withholdingTaxImportedService?: number | null,
    public payment?: IPayment | null
  ) {}
}

export function getTaxRuleIdentifier(taxRule: ITaxRule): number | undefined {
  return taxRule.id;
}
