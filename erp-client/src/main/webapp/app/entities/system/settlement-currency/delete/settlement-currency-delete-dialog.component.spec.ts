jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { SettlementCurrencyService } from '../service/settlement-currency.service';

import { SettlementCurrencyDeleteDialogComponent } from './settlement-currency-delete-dialog.component';

describe('SettlementCurrency Management Delete Component', () => {
  let comp: SettlementCurrencyDeleteDialogComponent;
  let fixture: ComponentFixture<SettlementCurrencyDeleteDialogComponent>;
  let service: SettlementCurrencyService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SettlementCurrencyDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(SettlementCurrencyDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SettlementCurrencyDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SettlementCurrencyService);
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
