import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFxRateType } from '../fx-rate-type.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-fx-rate-type-detail',
  templateUrl: './fx-rate-type-detail.component.html',
})
export class FxRateTypeDetailComponent implements OnInit {
  fxRateType: IFxRateType | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fxRateType }) => {
      this.fxRateType = fxRateType;
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
