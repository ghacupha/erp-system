///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

import { IBusinessDocument } from '../business-document.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-business-document-detail',
  templateUrl: './business-document-detail.component.html',
  styleUrls: ['./business-document-detail.component.scss'],
})
export class BusinessDocumentDetailComponent implements OnInit {
  businessDocument: IBusinessDocument | null = null;
  documentPreviewUrl: SafeResourceUrl | null = null;
  private previewObjectUrl: string | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute, protected sanitizer: DomSanitizer) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ businessDocument }) => {
      this.businessDocument = businessDocument;
      this.preparePreview();
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

  clearPreview(): void {
    this.revokePreviewUrl();
    this.documentPreviewUrl = null;
  }

  private preparePreview(): void {
    this.clearPreview();
    if (!this.businessDocument?.documentFile || !this.businessDocument.documentFileContentType) {
      return;
    }

    const blob = this.base64ToBlob(this.businessDocument.documentFile, this.businessDocument.documentFileContentType);
    this.previewObjectUrl = URL.createObjectURL(blob);
    this.documentPreviewUrl = this.sanitizer.bypassSecurityTrustResourceUrl(this.previewObjectUrl);
  }

  private revokePreviewUrl(): void {
    if (this.previewObjectUrl) {
      URL.revokeObjectURL(this.previewObjectUrl);
      this.previewObjectUrl = null;
    }
  }

  private base64ToBlob(base64: string, contentType: string): Blob {
    const byteCharacters = atob(base64);
    const byteNumbers = new Array(byteCharacters.length);
    for (let index = 0; index < byteCharacters.length; index++) {
      byteNumbers[index] = byteCharacters.charCodeAt(index);
    }
    return new Blob([new Uint8Array(byteNumbers)], { type: contentType });
  }
}
