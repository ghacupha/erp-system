jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { CrbProductServiceFeeTypeService } from '../service/crb-product-service-fee-type.service';

import { CrbProductServiceFeeTypeDeleteDialogComponent } from './crb-product-service-fee-type-delete-dialog.component';

describe('CrbProductServiceFeeType Management Delete Component', () => {
  let comp: CrbProductServiceFeeTypeDeleteDialogComponent;
  let fixture: ComponentFixture<CrbProductServiceFeeTypeDeleteDialogComponent>;
  let service: CrbProductServiceFeeTypeService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CrbProductServiceFeeTypeDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(CrbProductServiceFeeTypeDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CrbProductServiceFeeTypeDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CrbProductServiceFeeTypeService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
