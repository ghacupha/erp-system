import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStringQuestionBase } from '../string-question-base.model';

@Component({
  selector: 'jhi-string-question-base-detail',
  templateUrl: './string-question-base-detail.component.html',
})
export class StringQuestionBaseDetailComponent implements OnInit {
  stringQuestionBase: IStringQuestionBase | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ stringQuestionBase }) => {
      this.stringQuestionBase = stringQuestionBase;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
