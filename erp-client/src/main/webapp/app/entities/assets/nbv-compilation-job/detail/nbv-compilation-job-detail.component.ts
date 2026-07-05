import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INbvCompilationJob } from '../nbv-compilation-job.model';

@Component({
  selector: 'jhi-nbv-compilation-job-detail',
  templateUrl: './nbv-compilation-job-detail.component.html',
})
export class NbvCompilationJobDetailComponent implements OnInit {
  nbvCompilationJob: INbvCompilationJob | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nbvCompilationJob }) => {
      this.nbvCompilationJob = nbvCompilationJob;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
