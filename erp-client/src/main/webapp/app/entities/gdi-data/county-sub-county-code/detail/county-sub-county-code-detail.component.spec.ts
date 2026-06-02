import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CountySubCountyCodeDetailComponent } from './county-sub-county-code-detail.component';

describe('CountySubCountyCode Management Detail Component', () => {
  let comp: CountySubCountyCodeDetailComponent;
  let fixture: ComponentFixture<CountySubCountyCodeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CountySubCountyCodeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ countySubCountyCode: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CountySubCountyCodeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CountySubCountyCodeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load countySubCountyCode on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.countySubCountyCode).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
