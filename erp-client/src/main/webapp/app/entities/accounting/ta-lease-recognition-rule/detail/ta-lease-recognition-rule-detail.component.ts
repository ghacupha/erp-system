import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITALeaseRecognitionRule } from '../ta-lease-recognition-rule.model';

@Component({
  selector: 'jhi-ta-lease-recognition-rule-detail',
  templateUrl: './ta-lease-recognition-rule-detail.component.html',
})
export class TALeaseRecognitionRuleDetailComponent implements OnInit {
  tALeaseRecognitionRule: ITALeaseRecognitionRule | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tALeaseRecognitionRule }) => {
      this.tALeaseRecognitionRule = tALeaseRecognitionRule;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
