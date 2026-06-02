import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WorkInProgressOverviewDetailComponent } from './work-in-progress-overview-detail.component';

describe('WorkInProgressOverview Management Detail Component', () => {
  let comp: WorkInProgressOverviewDetailComponent;
  let fixture: ComponentFixture<WorkInProgressOverviewDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WorkInProgressOverviewDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ workInProgressOverview: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(WorkInProgressOverviewDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(WorkInProgressOverviewDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load workInProgressOverview on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.workInProgressOverview).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
