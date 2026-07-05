jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFRS16LeaseContractService } from '../service/ifrs-16-lease-contract.service';

import { IFRS16LeaseContractDeleteDialogComponent } from './ifrs-16-lease-contract-delete-dialog.component';

describe('IFRS16LeaseContract Management Delete Component', () => {
  let comp: IFRS16LeaseContractDeleteDialogComponent;
  let fixture: ComponentFixture<IFRS16LeaseContractDeleteDialogComponent>;
  let service: IFRS16LeaseContractService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [IFRS16LeaseContractDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(IFRS16LeaseContractDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(IFRS16LeaseContractDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(IFRS16LeaseContractService);
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
