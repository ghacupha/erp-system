import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPrepaymentByAccountReportRequisition } from '../prepayment-by-account-report-requisition.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-prepayment-by-account-report-requisition-detail',
  templateUrl: './prepayment-by-account-report-requisition-detail.component.html',
})
export class PrepaymentByAccountReportRequisitionDetailComponent implements OnInit {
  prepaymentByAccountReportRequisition: IPrepaymentByAccountReportRequisition | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prepaymentByAccountReportRequisition }) => {
      this.prepaymentByAccountReportRequisition = prepaymentByAccountReportRequisition;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
