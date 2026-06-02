import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IsoCountryCodeDetailComponent } from './iso-country-code-detail.component';

describe('IsoCountryCode Management Detail Component', () => {
  let comp: IsoCountryCodeDetailComponent;
  let fixture: ComponentFixture<IsoCountryCodeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [IsoCountryCodeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ isoCountryCode: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(IsoCountryCodeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(IsoCountryCodeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load isoCountryCode on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.isoCountryCode).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
