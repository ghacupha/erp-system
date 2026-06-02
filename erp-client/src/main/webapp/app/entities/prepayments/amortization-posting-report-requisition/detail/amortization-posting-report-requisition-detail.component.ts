import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAmortizationPostingReportRequisition } from '../amortization-posting-report-requisition.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-amortization-posting-report-requisition-detail',
  templateUrl: './amortization-posting-report-requisition-detail.component.html',
})
export class AmortizationPostingReportRequisitionDetailComponent implements OnInit {
  amortizationPostingReportRequisition: IAmortizationPostingReportRequisition | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ amortizationPostingReportRequisition }) => {
      this.amortizationPostingReportRequisition = amortizationPostingReportRequisition;
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
