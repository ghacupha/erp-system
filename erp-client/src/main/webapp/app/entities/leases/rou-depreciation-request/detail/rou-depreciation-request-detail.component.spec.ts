import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RouDepreciationRequestDetailComponent } from './rou-depreciation-request-detail.component';

describe('RouDepreciationRequest Management Detail Component', () => {
  let comp: RouDepreciationRequestDetailComponent;
  let fixture: ComponentFixture<RouDepreciationRequestDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RouDepreciationRequestDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ rouDepreciationRequest: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RouDepreciationRequestDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RouDepreciationRequestDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load rouDepreciationRequest on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.rouDepreciationRequest).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
