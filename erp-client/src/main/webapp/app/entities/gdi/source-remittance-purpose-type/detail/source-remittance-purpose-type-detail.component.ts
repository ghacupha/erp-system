import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISourceRemittancePurposeType } from '../source-remittance-purpose-type.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-source-remittance-purpose-type-detail',
  templateUrl: './source-remittance-purpose-type-detail.component.html',
})
export class SourceRemittancePurposeTypeDetailComponent implements OnInit {
  sourceRemittancePurposeType: ISourceRemittancePurposeType | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sourceRemittancePurposeType }) => {
      this.sourceRemittancePurposeType = sourceRemittancePurposeType;
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
