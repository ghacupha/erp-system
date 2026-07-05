import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITACompilationRequest } from '../ta-compilation-request.model';

@Component({
  selector: 'jhi-ta-compilation-request-detail',
  templateUrl: './ta-compilation-request-detail.component.html',
})
export class TACompilationRequestDetailComponent implements OnInit {
  tACompilationRequest: ITACompilationRequest | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tACompilationRequest }) => {
      this.tACompilationRequest = tACompilationRequest;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
