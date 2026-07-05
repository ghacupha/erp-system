import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICollateralInformation } from '../collateral-information.model';

@Component({
  selector: 'jhi-collateral-information-detail',
  templateUrl: './collateral-information-detail.component.html',
})
export class CollateralInformationDetailComponent implements OnInit {
  collateralInformation: ICollateralInformation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ collateralInformation }) => {
      this.collateralInformation = collateralInformation;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
