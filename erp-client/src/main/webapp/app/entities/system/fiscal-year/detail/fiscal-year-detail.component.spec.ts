import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FiscalYearDetailComponent } from './fiscal-year-detail.component';

describe('FiscalYear Management Detail Component', () => {
  let comp: FiscalYearDetailComponent;
  let fixture: ComponentFixture<FiscalYearDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FiscalYearDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ fiscalYear: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FiscalYearDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FiscalYearDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load fiscalYear on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.fiscalYear).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
