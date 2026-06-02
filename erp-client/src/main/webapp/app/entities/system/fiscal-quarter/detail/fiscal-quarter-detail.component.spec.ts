import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FiscalQuarterDetailComponent } from './fiscal-quarter-detail.component';

describe('FiscalQuarter Management Detail Component', () => {
  let comp: FiscalQuarterDetailComponent;
  let fixture: ComponentFixture<FiscalQuarterDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FiscalQuarterDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ fiscalQuarter: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FiscalQuarterDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FiscalQuarterDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load fiscalQuarter on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.fiscalQuarter).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
