import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IXlsxReportRequisition } from '../xlsx-report-requisition.model';

@Component({
  selector: 'jhi-xlsx-report-requisition-detail',
  templateUrl: './xlsx-report-requisition-detail.component.html',
})
export class XlsxReportRequisitionDetailComponent implements OnInit {
  xlsxReportRequisition: IXlsxReportRequisition | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ xlsxReportRequisition }) => {
      this.xlsxReportRequisition = xlsxReportRequisition;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
