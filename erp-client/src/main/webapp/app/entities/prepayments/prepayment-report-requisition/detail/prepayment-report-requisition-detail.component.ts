import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPrepaymentReportRequisition } from '../prepayment-report-requisition.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-prepayment-report-requisition-detail',
  templateUrl: './prepayment-report-requisition-detail.component.html',
})
export class PrepaymentReportRequisitionDetailComponent implements OnInit {
  prepaymentReportRequisition: IPrepaymentReportRequisition | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prepaymentReportRequisition }) => {
      this.prepaymentReportRequisition = prepaymentReportRequisition;
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
