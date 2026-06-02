import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRouDepreciationRequest } from '../rou-depreciation-request.model';

@Component({
  selector: 'jhi-rou-depreciation-request-detail',
  templateUrl: './rou-depreciation-request-detail.component.html',
})
export class RouDepreciationRequestDetailComponent implements OnInit {
  rouDepreciationRequest: IRouDepreciationRequest | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rouDepreciationRequest }) => {
      this.rouDepreciationRequest = rouDepreciationRequest;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
