import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISecurityClassificationType } from '../security-classification-type.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-security-classification-type-detail',
  templateUrl: './security-classification-type-detail.component.html',
})
export class SecurityClassificationTypeDetailComponent implements OnInit {
  securityClassificationType: ISecurityClassificationType | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ securityClassificationType }) => {
      this.securityClassificationType = securityClassificationType;
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
