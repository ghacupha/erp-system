import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRouInitialDirectCost } from '../rou-initial-direct-cost.model';

@Component({
  selector: 'jhi-rou-initial-direct-cost-detail',
  templateUrl: './rou-initial-direct-cost-detail.component.html',
})
export class RouInitialDirectCostDetailComponent implements OnInit {
  rouInitialDirectCost: IRouInitialDirectCost | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rouInitialDirectCost }) => {
      this.rouInitialDirectCost = rouInitialDirectCost;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
