import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LeasePeriodDetailComponent } from './lease-period-detail.component';

describe('LeasePeriod Management Detail Component', () => {
  let comp: LeasePeriodDetailComponent;
  let fixture: ComponentFixture<LeasePeriodDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LeasePeriodDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ leasePeriod: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LeasePeriodDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LeasePeriodDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load leasePeriod on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.leasePeriod).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
