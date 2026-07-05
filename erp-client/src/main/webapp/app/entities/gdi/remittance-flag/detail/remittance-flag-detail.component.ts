import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRemittanceFlag } from '../remittance-flag.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-remittance-flag-detail',
  templateUrl: './remittance-flag-detail.component.html',
})
export class RemittanceFlagDetailComponent implements OnInit {
  remittanceFlag: IRemittanceFlag | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ remittanceFlag }) => {
      this.remittanceFlag = remittanceFlag;
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
