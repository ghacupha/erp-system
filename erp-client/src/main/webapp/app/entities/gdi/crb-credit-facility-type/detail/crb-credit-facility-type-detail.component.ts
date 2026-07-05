import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICrbCreditFacilityType } from '../crb-credit-facility-type.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-crb-credit-facility-type-detail',
  templateUrl: './crb-credit-facility-type-detail.component.html',
})
export class CrbCreditFacilityTypeDetailComponent implements OnInit {
  crbCreditFacilityType: ICrbCreditFacilityType | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ crbCreditFacilityType }) => {
      this.crbCreditFacilityType = crbCreditFacilityType;
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
