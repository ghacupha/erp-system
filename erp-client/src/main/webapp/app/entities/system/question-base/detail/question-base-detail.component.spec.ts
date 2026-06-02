import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { QuestionBaseDetailComponent } from './question-base-detail.component';

describe('QuestionBase Management Detail Component', () => {
  let comp: QuestionBaseDetailComponent;
  let fixture: ComponentFixture<QuestionBaseDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [QuestionBaseDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ questionBase: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(QuestionBaseDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(QuestionBaseDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load questionBase on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.questionBase).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
