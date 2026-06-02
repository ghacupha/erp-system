import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ParticularsOfOutletDetailComponent } from './particulars-of-outlet-detail.component';

describe('ParticularsOfOutlet Management Detail Component', () => {
  let comp: ParticularsOfOutletDetailComponent;
  let fixture: ComponentFixture<ParticularsOfOutletDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ParticularsOfOutletDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ particularsOfOutlet: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ParticularsOfOutletDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ParticularsOfOutletDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load particularsOfOutlet on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.particularsOfOutlet).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
