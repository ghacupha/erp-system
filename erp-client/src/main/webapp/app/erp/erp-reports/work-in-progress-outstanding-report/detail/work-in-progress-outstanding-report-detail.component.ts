///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWorkInProgressOutstandingReport } from '../work-in-progress-outstanding-report.model';
import { WorkInProgressTransferService } from '../../../erp-assets/work-in-progress-transfer/service/work-in-progress-transfer.service';
import { IWorkInProgressTransfer } from '../../../erp-assets/work-in-progress-transfer/work-in-progress-transfer.model';
import { SettlementService } from '../../../erp-settlements/settlement/service/settlement.service';
import { ISettlement } from '../../../erp-settlements/settlement/settlement.model';
import { WorkInProgressRegistrationService } from '../../../erp-assets/work-in-progress-registration/service/work-in-progress-registration.service';

@Component({
  selector: 'jhi-work-in-progress-outstanding-report-detail',
  templateUrl: './work-in-progress-outstanding-report-detail.component.html',
})
export class WorkInProgressOutstandingReportDetailComponent implements OnInit {
  workInProgressOutstandingReport: IWorkInProgressOutstandingReport | null = null;
  settlementTransaction: ISettlement | null = null;
  appliedWorkInProgressTransferItems: IWorkInProgressTransfer[] = []

  constructor(
    protected settlementService: SettlementService,
    protected workInProgressRegistrationService: WorkInProgressRegistrationService,
    protected workInProgressTransferService: WorkInProgressTransferService,
    protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workInProgressOutstandingReport }) => {
      this.workInProgressOutstandingReport = workInProgressOutstandingReport;

      this.workInProgressTransferService.query({'workInProgressRegistrationId.equals': workInProgressOutstandingReport.id})
        .subscribe(response => {
            this.appliedWorkInProgressTransferItems = response.body ?? [];
        })

      this.workInProgressRegistrationService.find(workInProgressOutstandingReport.id).subscribe(registrationRes => {
        if (registrationRes.body?.settlementTransaction?.id) {
          this.settlementService.find(registrationRes.body.settlementTransaction.id)
            .subscribe(response => {
              this.settlementTransaction = response.body;
            });
        }
      });
    });
  }

  previousState(): void {
    window.history.back();
  }
}
