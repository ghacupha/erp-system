import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFiscalYear } from '../fiscal-year.model';

@Component({
  selector: 'jhi-fiscal-year-detail',
  templateUrl: './fiscal-year-detail.component.html',
})
export class FiscalYearDetailComponent implements OnInit {
  fiscalYear: IFiscalYear | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fiscalYear }) => {
      this.fiscalYear = fiscalYear;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
