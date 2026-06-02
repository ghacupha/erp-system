import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ServiceOutletDetailComponent } from './service-outlet-detail.component';

describe('ServiceOutlet Management Detail Component', () => {
  let comp: ServiceOutletDetailComponent;
  let fixture: ComponentFixture<ServiceOutletDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ServiceOutletDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ serviceOutlet: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ServiceOutletDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ServiceOutletDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load serviceOutlet on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.serviceOutlet).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
