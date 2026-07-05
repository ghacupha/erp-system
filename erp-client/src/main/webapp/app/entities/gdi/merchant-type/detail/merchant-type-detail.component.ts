import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMerchantType } from '../merchant-type.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-merchant-type-detail',
  templateUrl: './merchant-type-detail.component.html',
})
export class MerchantTypeDetailComponent implements OnInit {
  merchantType: IMerchantType | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ merchantType }) => {
      this.merchantType = merchantType;
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
