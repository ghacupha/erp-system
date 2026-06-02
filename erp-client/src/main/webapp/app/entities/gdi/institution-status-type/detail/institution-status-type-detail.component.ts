import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInstitutionStatusType } from '../institution-status-type.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-institution-status-type-detail',
  templateUrl: './institution-status-type-detail.component.html',
})
export class InstitutionStatusTypeDetailComponent implements OnInit {
  institutionStatusType: IInstitutionStatusType | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ institutionStatusType }) => {
      this.institutionStatusType = institutionStatusType;
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
