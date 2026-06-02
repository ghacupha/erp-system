import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PerformanceOfForeignSubsidiariesDetailComponent } from './performance-of-foreign-subsidiaries-detail.component';

describe('PerformanceOfForeignSubsidiaries Management Detail Component', () => {
  let comp: PerformanceOfForeignSubsidiariesDetailComponent;
  let fixture: ComponentFixture<PerformanceOfForeignSubsidiariesDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PerformanceOfForeignSubsidiariesDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ performanceOfForeignSubsidiaries: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PerformanceOfForeignSubsidiariesDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PerformanceOfForeignSubsidiariesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load performanceOfForeignSubsidiaries on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.performanceOfForeignSubsidiaries).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
