import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISecurityTenure } from '../security-tenure.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-security-tenure-detail',
  templateUrl: './security-tenure-detail.component.html',
})
export class SecurityTenureDetailComponent implements OnInit {
  securityTenure: ISecurityTenure | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ securityTenure }) => {
      this.securityTenure = securityTenure;
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
