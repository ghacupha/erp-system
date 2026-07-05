import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAlgorithm } from '../algorithm.model';

@Component({
  selector: 'jhi-algorithm-detail',
  templateUrl: './algorithm-detail.component.html',
})
export class AlgorithmDetailComponent implements OnInit {
  algorithm: IAlgorithm | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ algorithm }) => {
      this.algorithm = algorithm;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
