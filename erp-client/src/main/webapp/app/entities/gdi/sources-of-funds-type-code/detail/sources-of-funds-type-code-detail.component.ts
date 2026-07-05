import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISourcesOfFundsTypeCode } from '../sources-of-funds-type-code.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-sources-of-funds-type-code-detail',
  templateUrl: './sources-of-funds-type-code-detail.component.html',
})
export class SourcesOfFundsTypeCodeDetailComponent implements OnInit {
  sourcesOfFundsTypeCode: ISourcesOfFundsTypeCode | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sourcesOfFundsTypeCode }) => {
      this.sourcesOfFundsTypeCode = sourcesOfFundsTypeCode;
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
