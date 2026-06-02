import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { StringQuestionBaseDetailComponent } from './string-question-base-detail.component';

describe('StringQuestionBase Management Detail Component', () => {
  let comp: StringQuestionBaseDetailComponent;
  let fixture: ComponentFixture<StringQuestionBaseDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [StringQuestionBaseDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ stringQuestionBase: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(StringQuestionBaseDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(StringQuestionBaseDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load stringQuestionBase on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.stringQuestionBase).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
