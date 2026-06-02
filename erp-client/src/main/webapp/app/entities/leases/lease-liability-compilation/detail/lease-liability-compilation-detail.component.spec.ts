import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LeaseLiabilityCompilationDetailComponent } from './lease-liability-compilation-detail.component';

describe('LeaseLiabilityCompilation Management Detail Component', () => {
  let comp: LeaseLiabilityCompilationDetailComponent;
  let fixture: ComponentFixture<LeaseLiabilityCompilationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LeaseLiabilityCompilationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ leaseLiabilityCompilation: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LeaseLiabilityCompilationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LeaseLiabilityCompilationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load leaseLiabilityCompilation on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.leaseLiabilityCompilation).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
