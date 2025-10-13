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

import { Component, Input, OnInit } from '@angular/core';
import { ILeaseAmortizationCalculation } from '../../../erp-leases/lease-amortization-calculation/lease-amortization-calculation.model';
import { LeaseLiabilityService } from '../../../erp-leases/lease-liability/service/lease-liability.service';
import { LeaseAmortizationCalculationService } from '../../../erp-leases/lease-amortization-calculation/service/lease-amortization-calculation.service';
import { IFRS16LeaseContractService } from '../../../erp-leases/ifrs-16-lease-contract/service/ifrs-16-lease-contract.service';

@Component({
  selector: 'jhi-lease-amortization-calculation-option-view',
  template: `
    # {{item.id}} # {{ leaseId }} | No. Of Periods: {{ numberOfPeriods }} Periodicity: {{ periodicity }} Amount: {{ leaseAmount | currency }}
  `
})
export class LeaseAmortizationCalculationOptionViewComponent implements OnInit {

  leaseId = '';

  @Input() item: ILeaseAmortizationCalculation = {};

  numberOfPeriods!: number;

  periodicity!: string;

  leaseAmount!: number;

  constructor(protected leaseLiabilityService: LeaseLiabilityService,
              protected ifrs16LeaseContractService: IFRS16LeaseContractService,
              protected leaseAmortizationCalculationService: LeaseAmortizationCalculationService) {
  }

  ngOnInit(): void {

    if (this.item.leaseContract?.id) {
      this.ifrs16LeaseContractService.find(this.item.leaseContract.id).subscribe(lease => {
        if (lease.body?.shortTitle) {
          this.leaseId = lease.body.shortTitle;
        }
      });
    }

    if (this.item.id != null) {
      this.leaseAmortizationCalculationService.find(this.item.id).subscribe(calc => {

        if (calc.body?.numberOfPeriods) {
          this.numberOfPeriods = calc.body.numberOfPeriods;
        }

        if (calc.body?.periodicity) {
          this.periodicity = calc.body.periodicity;
        }

        if (calc.body?.leaseAmount) {
          this.leaseAmount = calc.body.leaseAmount;
        }

      });
    }
  }

}
