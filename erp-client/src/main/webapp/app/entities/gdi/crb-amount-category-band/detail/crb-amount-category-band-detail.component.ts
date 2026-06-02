import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICrbAmountCategoryBand } from '../crb-amount-category-band.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-crb-amount-category-band-detail',
  templateUrl: './crb-amount-category-band-detail.component.html',
})
export class CrbAmountCategoryBandDetailComponent implements OnInit {
  crbAmountCategoryBand: ICrbAmountCategoryBand | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ crbAmountCategoryBand }) => {
      this.crbAmountCategoryBand = crbAmountCategoryBand;
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
