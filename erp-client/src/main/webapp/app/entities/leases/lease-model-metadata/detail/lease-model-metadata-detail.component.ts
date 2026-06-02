import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILeaseModelMetadata } from '../lease-model-metadata.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-lease-model-metadata-detail',
  templateUrl: './lease-model-metadata-detail.component.html',
})
export class LeaseModelMetadataDetailComponent implements OnInit {
  leaseModelMetadata: ILeaseModelMetadata | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaseModelMetadata }) => {
      this.leaseModelMetadata = leaseModelMetadata;
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
