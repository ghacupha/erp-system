import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AmortizationPostingReportDetailComponent } from './amortization-posting-report-detail.component';

describe('AmortizationPostingReport Management Detail Component', () => {
  let comp: AmortizationPostingReportDetailComponent;
  let fixture: ComponentFixture<AmortizationPostingReportDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AmortizationPostingReportDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ amortizationPostingReport: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AmortizationPostingReportDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AmortizationPostingReportDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load amortizationPostingReport on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.amortizationPostingReport).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
