import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INbvCompilationBatch } from '../nbv-compilation-batch.model';

@Component({
  selector: 'jhi-nbv-compilation-batch-detail',
  templateUrl: './nbv-compilation-batch-detail.component.html',
})
export class NbvCompilationBatchDetailComponent implements OnInit {
  nbvCompilationBatch: INbvCompilationBatch | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nbvCompilationBatch }) => {
      this.nbvCompilationBatch = nbvCompilationBatch;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
