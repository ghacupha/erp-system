import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBusinessDocument } from '../business-document.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-business-document-detail',
  templateUrl: './business-document-detail.component.html',
})
export class BusinessDocumentDetailComponent implements OnInit {
  businessDocument: IBusinessDocument | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ businessDocument }) => {
      this.businessDocument = businessDocument;
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
