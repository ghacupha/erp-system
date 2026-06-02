import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FiscalMonthDetailComponent } from './fiscal-month-detail.component';

describe('FiscalMonth Management Detail Component', () => {
  let comp: FiscalMonthDetailComponent;
  let fixture: ComponentFixture<FiscalMonthDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FiscalMonthDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ fiscalMonth: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FiscalMonthDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FiscalMonthDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load fiscalMonth on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.fiscalMonth).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
