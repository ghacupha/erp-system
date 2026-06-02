import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WorkInProgressReportDetailComponent } from './work-in-progress-report-detail.component';

describe('WorkInProgressReport Management Detail Component', () => {
  let comp: WorkInProgressReportDetailComponent;
  let fixture: ComponentFixture<WorkInProgressReportDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WorkInProgressReportDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ workInProgressReport: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(WorkInProgressReportDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(WorkInProgressReportDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load workInProgressReport on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.workInProgressReport).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
