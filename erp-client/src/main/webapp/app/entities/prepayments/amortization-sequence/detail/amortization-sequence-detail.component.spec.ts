import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AmortizationSequenceDetailComponent } from './amortization-sequence-detail.component';

describe('AmortizationSequence Management Detail Component', () => {
  let comp: AmortizationSequenceDetailComponent;
  let fixture: ComponentFixture<AmortizationSequenceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AmortizationSequenceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ amortizationSequence: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AmortizationSequenceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AmortizationSequenceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load amortizationSequence on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.amortizationSequence).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
