import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CustomerIDDocumentTypeDetailComponent } from './customer-id-document-type-detail.component';

describe('CustomerIDDocumentType Management Detail Component', () => {
  let comp: CustomerIDDocumentTypeDetailComponent;
  let fixture: ComponentFixture<CustomerIDDocumentTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CustomerIDDocumentTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ customerIDDocumentType: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CustomerIDDocumentTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CustomerIDDocumentTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load customerIDDocumentType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.customerIDDocumentType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
