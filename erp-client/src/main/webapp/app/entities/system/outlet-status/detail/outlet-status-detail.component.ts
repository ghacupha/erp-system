import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOutletStatus } from '../outlet-status.model';

@Component({
  selector: 'jhi-outlet-status-detail',
  templateUrl: './outlet-status-detail.component.html',
})
export class OutletStatusDetailComponent implements OnInit {
  outletStatus: IOutletStatus | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ outletStatus }) => {
      this.outletStatus = outletStatus;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
