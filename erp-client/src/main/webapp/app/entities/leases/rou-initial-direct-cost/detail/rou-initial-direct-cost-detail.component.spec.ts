import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RouInitialDirectCostDetailComponent } from './rou-initial-direct-cost-detail.component';

describe('RouInitialDirectCost Management Detail Component', () => {
  let comp: RouInitialDirectCostDetailComponent;
  let fixture: ComponentFixture<RouInitialDirectCostDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RouInitialDirectCostDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ rouInitialDirectCost: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RouInitialDirectCostDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RouInitialDirectCostDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load rouInitialDirectCost on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.rouInitialDirectCost).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
