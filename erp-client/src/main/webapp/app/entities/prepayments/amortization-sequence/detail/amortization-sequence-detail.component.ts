import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAmortizationSequence } from '../amortization-sequence.model';

@Component({
  selector: 'jhi-amortization-sequence-detail',
  templateUrl: './amortization-sequence-detail.component.html',
})
export class AmortizationSequenceDetailComponent implements OnInit {
  amortizationSequence: IAmortizationSequence | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ amortizationSequence }) => {
      this.amortizationSequence = amortizationSequence;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
