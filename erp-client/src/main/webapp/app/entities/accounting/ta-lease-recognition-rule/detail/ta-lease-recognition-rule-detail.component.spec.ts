import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TALeaseRecognitionRuleDetailComponent } from './ta-lease-recognition-rule-detail.component';

describe('TALeaseRecognitionRule Management Detail Component', () => {
  let comp: TALeaseRecognitionRuleDetailComponent;
  let fixture: ComponentFixture<TALeaseRecognitionRuleDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TALeaseRecognitionRuleDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tALeaseRecognitionRule: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TALeaseRecognitionRuleDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TALeaseRecognitionRuleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tALeaseRecognitionRule on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tALeaseRecognitionRule).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
