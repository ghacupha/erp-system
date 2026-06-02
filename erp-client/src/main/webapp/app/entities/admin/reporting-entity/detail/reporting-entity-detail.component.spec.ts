import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReportingEntityDetailComponent } from './reporting-entity-detail.component';

describe('ReportingEntity Management Detail Component', () => {
  let comp: ReportingEntityDetailComponent;
  let fixture: ComponentFixture<ReportingEntityDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ReportingEntityDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ reportingEntity: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ReportingEntityDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ReportingEntityDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load reportingEntity on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.reportingEntity).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
