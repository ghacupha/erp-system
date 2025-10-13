///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';

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
  fileUploadToken?: string | null;
  compilationToken?: string | null;
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
    public fileUploadToken?: string | null,
    public compilationToken?: string | null,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getTaxRuleIdentifier(taxRule: ITaxRule): number | undefined {
  return taxRule.id;
}
