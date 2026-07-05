import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAmortizationRecurrence } from '../amortization-recurrence.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-amortization-recurrence-detail',
  templateUrl: './amortization-recurrence-detail.component.html',
})
export class AmortizationRecurrenceDetailComponent implements OnInit {
  amortizationRecurrence: IAmortizationRecurrence | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ amortizationRecurrence }) => {
      this.amortizationRecurrence = amortizationRecurrence;
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
