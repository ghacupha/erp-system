import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWorkInProgressOutstandingReportRequisition } from '../work-in-progress-outstanding-report-requisition.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-work-in-progress-outstanding-report-requisition-detail',
  templateUrl: './work-in-progress-outstanding-report-requisition-detail.component.html',
})
export class WorkInProgressOutstandingReportRequisitionDetailComponent implements OnInit {
  workInProgressOutstandingReportRequisition: IWorkInProgressOutstandingReportRequisition | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workInProgressOutstandingReportRequisition }) => {
      this.workInProgressOutstandingReportRequisition = workInProgressOutstandingReportRequisition;
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
