import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFiscalQuarter } from '../fiscal-quarter.model';

@Component({
  selector: 'jhi-fiscal-quarter-detail',
  templateUrl: './fiscal-quarter-detail.component.html',
})
export class FiscalQuarterDetailComponent implements OnInit {
  fiscalQuarter: IFiscalQuarter | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fiscalQuarter }) => {
      this.fiscalQuarter = fiscalQuarter;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
