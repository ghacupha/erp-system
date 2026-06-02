import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DepreciationPeriodDetailComponent } from './depreciation-period-detail.component';

describe('DepreciationPeriod Management Detail Component', () => {
  let comp: DepreciationPeriodDetailComponent;
  let fixture: ComponentFixture<DepreciationPeriodDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DepreciationPeriodDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ depreciationPeriod: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DepreciationPeriodDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DepreciationPeriodDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load depreciationPeriod on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.depreciationPeriod).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
