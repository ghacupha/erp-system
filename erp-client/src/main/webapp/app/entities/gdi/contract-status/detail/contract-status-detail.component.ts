import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContractStatus } from '../contract-status.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-contract-status-detail',
  templateUrl: './contract-status-detail.component.html',
})
export class ContractStatusDetailComponent implements OnInit {
  contractStatus: IContractStatus | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contractStatus }) => {
      this.contractStatus = contractStatus;
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
