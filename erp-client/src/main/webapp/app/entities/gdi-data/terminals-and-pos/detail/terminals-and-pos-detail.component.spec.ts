import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TerminalsAndPOSDetailComponent } from './terminals-and-pos-detail.component';

describe('TerminalsAndPOS Management Detail Component', () => {
  let comp: TerminalsAndPOSDetailComponent;
  let fixture: ComponentFixture<TerminalsAndPOSDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TerminalsAndPOSDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ terminalsAndPOS: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TerminalsAndPOSDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TerminalsAndPOSDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load terminalsAndPOS on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.terminalsAndPOS).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
