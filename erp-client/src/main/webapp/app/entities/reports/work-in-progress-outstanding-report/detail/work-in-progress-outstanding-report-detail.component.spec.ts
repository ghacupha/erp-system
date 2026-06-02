import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WorkInProgressOutstandingReportDetailComponent } from './work-in-progress-outstanding-report-detail.component';

describe('WorkInProgressOutstandingReport Management Detail Component', () => {
  let comp: WorkInProgressOutstandingReportDetailComponent;
  let fixture: ComponentFixture<WorkInProgressOutstandingReportDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WorkInProgressOutstandingReportDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ workInProgressOutstandingReport: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(WorkInProgressOutstandingReportDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(WorkInProgressOutstandingReportDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load workInProgressOutstandingReport on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.workInProgressOutstandingReport).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
