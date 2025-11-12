///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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

import { OnInit, Pipe, PipeTransform } from '@angular/core';
import { ILeaseAmortizationCalculation } from '../../../erp-leases/lease-amortization-calculation/lease-amortization-calculation.model';
import { LeaseLiabilityService } from '../../../erp-leases/lease-liability/service/lease-liability.service';
import { IFRS16LeaseContractService } from '../../../erp-leases/ifrs-16-lease-contract/service/ifrs-16-lease-contract.service';
import { formatCurrency } from '@angular/common';

@Pipe({
  name: 'formatLeaseAmortizationCalculation'
})
export class FormatLeaseAmortizationCalculationPipe implements PipeTransform {


  constructor(protected ifrs16LeaseContractService :IFRS16LeaseContractService) {
  }

  transform(value: ILeaseAmortizationCalculation): string {

    let leaseId = 'lease item';
    let currencyAmount = '';

    if (value.leaseContract?.bookingId) {
      leaseId = value.leaseContract.bookingId;
    }

    if (value.leaseAmount) {
      currencyAmount = formatCurrency(value.leaseAmount, 'en','')
    }

    return `Id: ${value.id} | ${leaseId} | Amount: ${currencyAmount} | No. of Periods: ${value.numberOfPeriods} | Periodicity: ${value.periodicity}`;
  }
}
