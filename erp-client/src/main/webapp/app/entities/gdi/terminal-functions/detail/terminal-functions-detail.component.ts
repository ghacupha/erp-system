import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITerminalFunctions } from '../terminal-functions.model';

@Component({
  selector: 'jhi-terminal-functions-detail',
  templateUrl: './terminal-functions-detail.component.html',
})
export class TerminalFunctionsDetailComponent implements OnInit {
  terminalFunctions: ITerminalFunctions | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ terminalFunctions }) => {
      this.terminalFunctions = terminalFunctions;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
