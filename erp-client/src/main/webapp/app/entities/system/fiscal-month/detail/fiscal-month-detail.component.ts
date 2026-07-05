import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFiscalMonth } from '../fiscal-month.model';

@Component({
  selector: 'jhi-fiscal-month-detail',
  templateUrl: './fiscal-month-detail.component.html',
})
export class FiscalMonthDetailComponent implements OnInit {
  fiscalMonth: IFiscalMonth | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fiscalMonth }) => {
      this.fiscalMonth = fiscalMonth;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
