import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LeaseLiabilityScheduleReportItemDetailComponent } from './lease-liability-schedule-report-item-detail.component';

describe('LeaseLiabilityScheduleReportItem Management Detail Component', () => {
  let comp: LeaseLiabilityScheduleReportItemDetailComponent;
  let fixture: ComponentFixture<LeaseLiabilityScheduleReportItemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LeaseLiabilityScheduleReportItemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ leaseLiabilityScheduleReportItem: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LeaseLiabilityScheduleReportItemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LeaseLiabilityScheduleReportItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load leaseLiabilityScheduleReportItem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.leaseLiabilityScheduleReportItem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
