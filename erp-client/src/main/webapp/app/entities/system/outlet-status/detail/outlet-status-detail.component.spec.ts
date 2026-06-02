import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OutletStatusDetailComponent } from './outlet-status-detail.component';

describe('OutletStatus Management Detail Component', () => {
  let comp: OutletStatusDetailComponent;
  let fixture: ComponentFixture<OutletStatusDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OutletStatusDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ outletStatus: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(OutletStatusDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(OutletStatusDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load outletStatus on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.outletStatus).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
