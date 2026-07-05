import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRouModelMetadata } from '../rou-model-metadata.model';

@Component({
  selector: 'jhi-rou-model-metadata-detail',
  templateUrl: './rou-model-metadata-detail.component.html',
})
export class RouModelMetadataDetailComponent implements OnInit {
  rouModelMetadata: IRouModelMetadata | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rouModelMetadata }) => {
      this.rouModelMetadata = rouModelMetadata;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
