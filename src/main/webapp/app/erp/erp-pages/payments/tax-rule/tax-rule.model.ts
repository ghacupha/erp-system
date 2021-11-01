import { IPlaceholder } from 'app/entities/erpService/placeholder/placeholder.model';
import {IPayment} from '../payment/payment.model';

export interface ITaxRule {
  id?: number;
  telcoExciseDuty?: number | null;
  valueAddedTax?: number | null;
  withholdingVAT?: number | null;
  withholdingTaxConsultancy?: number | null;
  withholdingTaxRent?: number | null;
  cateringLevy?: number | null;
  serviceCharge?: number | null;
  withholdingTaxImportedService?: number | null;
  payments?: IPayment[] | null;
  placeholders?: IPlaceholder[] | null;
}

export class TaxRule implements ITaxRule {
  constructor(
    public id?: number,
    public telcoExciseDuty?: number | null,
    public valueAddedTax?: number | null,
    public withholdingVAT?: number | null,
    public withholdingTaxConsultancy?: number | null,
    public withholdingTaxRent?: number | null,
    public cateringLevy?: number | null,
    public serviceCharge?: number | null,
    public withholdingTaxImportedService?: number | null,
    public payments?: IPayment[] | null,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getTaxRuleIdentifier(taxRule: ITaxRule): number | undefined {
  return taxRule.id;
}
