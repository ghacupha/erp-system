import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPdfReportRequisition } from '../pdf-report-requisition.model';

@Component({
  selector: 'jhi-pdf-report-requisition-detail',
  templateUrl: './pdf-report-requisition-detail.component.html',
})
export class PdfReportRequisitionDetailComponent implements OnInit {
  pdfReportRequisition: IPdfReportRequisition | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pdfReportRequisition }) => {
      this.pdfReportRequisition = pdfReportRequisition;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
