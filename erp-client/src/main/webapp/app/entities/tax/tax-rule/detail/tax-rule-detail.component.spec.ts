import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TaxRuleDetailComponent } from './tax-rule-detail.component';

describe('TaxRule Management Detail Component', () => {
  let comp: TaxRuleDetailComponent;
  let fixture: ComponentFixture<TaxRuleDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TaxRuleDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ taxRule: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TaxRuleDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TaxRuleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load taxRule on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.taxRule).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
