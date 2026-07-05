import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPrepaymentCompilationRequest } from '../prepayment-compilation-request.model';

@Component({
  selector: 'jhi-prepayment-compilation-request-detail',
  templateUrl: './prepayment-compilation-request-detail.component.html',
})
export class PrepaymentCompilationRequestDetailComponent implements OnInit {
  prepaymentCompilationRequest: IPrepaymentCompilationRequest | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prepaymentCompilationRequest }) => {
      this.prepaymentCompilationRequest = prepaymentCompilationRequest;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
