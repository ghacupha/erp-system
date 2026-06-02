import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DepreciationJobDetailComponent } from './depreciation-job-detail.component';

describe('DepreciationJob Management Detail Component', () => {
  let comp: DepreciationJobDetailComponent;
  let fixture: ComponentFixture<DepreciationJobDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DepreciationJobDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ depreciationJob: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DepreciationJobDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DepreciationJobDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load depreciationJob on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.depreciationJob).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
