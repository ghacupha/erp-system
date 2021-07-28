import { Moment } from 'moment';

export interface ITaxRule {
  id?: number;
  paymentNumber?: string;
  paymentDate?: Moment;
  telcoExciseDuty?: number;
  valueAddedTax?: number;
  withholdingVAT?: number;
  withholdingTaxConsultancy?: number;
  withholdingTaxRent?: number;
  cateringLevy?: number;
  serviceCharge?: number;
  withholdingTaxImportedService?: number;
  paymentId?: number;
}

export class TaxRule implements ITaxRule {
  constructor(
    public id?: number,
    public paymentNumber?: string,
    public paymentDate?: Moment,
    public telcoExciseDuty?: number,
    public valueAddedTax?: number,
    public withholdingVAT?: number,
    public withholdingTaxConsultancy?: number,
    public withholdingTaxRent?: number,
    public cateringLevy?: number,
    public serviceCharge?: number,
    public withholdingTaxImportedService?: number,
    public paymentId?: number
  ) {}
}
