import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IQuestionBase } from '../question-base.model';

@Component({
  selector: 'jhi-question-base-detail',
  templateUrl: './question-base-detail.component.html',
})
export class QuestionBaseDetailComponent implements OnInit {
  questionBase: IQuestionBase | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ questionBase }) => {
      this.questionBase = questionBase;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
