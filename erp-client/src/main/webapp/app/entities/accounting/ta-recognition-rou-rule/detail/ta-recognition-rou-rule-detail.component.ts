import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITARecognitionROURule } from '../ta-recognition-rou-rule.model';

@Component({
  selector: 'jhi-ta-recognition-rou-rule-detail',
  templateUrl: './ta-recognition-rou-rule-detail.component.html',
})
export class TARecognitionROURuleDetailComponent implements OnInit {
  tARecognitionROURule: ITARecognitionROURule | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tARecognitionROURule }) => {
      this.tARecognitionROURule = tARecognitionROURule;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
