import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OutletTypeDetailComponent } from './outlet-type-detail.component';

describe('OutletType Management Detail Component', () => {
  let comp: OutletTypeDetailComponent;
  let fixture: ComponentFixture<OutletTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OutletTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ outletType: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(OutletTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(OutletTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load outletType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.outletType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
