import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TAAmortizationRuleDetailComponent } from './ta-amortization-rule-detail.component';

describe('TAAmortizationRule Management Detail Component', () => {
  let comp: TAAmortizationRuleDetailComponent;
  let fixture: ComponentFixture<TAAmortizationRuleDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TAAmortizationRuleDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tAAmortizationRule: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TAAmortizationRuleDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TAAmortizationRuleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tAAmortizationRule on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tAAmortizationRule).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
