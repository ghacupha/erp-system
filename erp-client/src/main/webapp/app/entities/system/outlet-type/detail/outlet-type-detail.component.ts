import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOutletType } from '../outlet-type.model';

@Component({
  selector: 'jhi-outlet-type-detail',
  templateUrl: './outlet-type-detail.component.html',
})
export class OutletTypeDetailComponent implements OnInit {
  outletType: IOutletType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ outletType }) => {
      this.outletType = outletType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
