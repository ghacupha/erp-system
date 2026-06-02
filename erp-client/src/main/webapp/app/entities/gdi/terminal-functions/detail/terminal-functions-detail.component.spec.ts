import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TerminalFunctionsDetailComponent } from './terminal-functions-detail.component';

describe('TerminalFunctions Management Detail Component', () => {
  let comp: TerminalFunctionsDetailComponent;
  let fixture: ComponentFixture<TerminalFunctionsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TerminalFunctionsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ terminalFunctions: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TerminalFunctionsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TerminalFunctionsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load terminalFunctions on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.terminalFunctions).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
