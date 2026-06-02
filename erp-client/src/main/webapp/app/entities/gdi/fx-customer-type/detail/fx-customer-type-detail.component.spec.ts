import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FxCustomerTypeDetailComponent } from './fx-customer-type-detail.component';

describe('FxCustomerType Management Detail Component', () => {
  let comp: FxCustomerTypeDetailComponent;
  let fixture: ComponentFixture<FxCustomerTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FxCustomerTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ fxCustomerType: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FxCustomerTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FxCustomerTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load fxCustomerType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.fxCustomerType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
