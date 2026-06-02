import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PrepaymentAmortizationDetailComponent } from './prepayment-amortization-detail.component';

describe('PrepaymentAmortization Management Detail Component', () => {
  let comp: PrepaymentAmortizationDetailComponent;
  let fixture: ComponentFixture<PrepaymentAmortizationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PrepaymentAmortizationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ prepaymentAmortization: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PrepaymentAmortizationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PrepaymentAmortizationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load prepaymentAmortization on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.prepaymentAmortization).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
