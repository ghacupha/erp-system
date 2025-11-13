///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { crc32, md5, ripemd160, sha1, sha256, sha384, sha512, sm3, whirlpool } from 'hash-wasm';
import { FormGroup } from '@angular/forms';
import { Injectable } from '@angular/core';

/**
 * This service calculates the checksums on a live form-group and updates the field according to the
 * file uploaded
 */
@Injectable({ providedIn: 'root' })
export class FileUploadChecksumService {

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  updateFileUploadChecksum(editForm: FormGroup, fileUploadField: string, fileUploadCheckSumField: string, checksumAlgorithm: string): void {

    switch (checksumAlgorithm) {
      case "sha512": {
        editForm.get([`${fileUploadField}`])?.valueChanges.subscribe((fileAttachment) => this.calculate512(fileAttachment, editForm, fileUploadCheckSumField));
        break;
      }
      case "sha256": {
        editForm.get([`${fileUploadField}`])?.valueChanges.subscribe((fileAttachment) => this.calculate256(fileAttachment, editForm, fileUploadCheckSumField));
        break;
      }
      case "sha384": {
        editForm.get([`${fileUploadField}`])?.valueChanges.subscribe((fileAttachment) => this.calculate384(fileAttachment, editForm, fileUploadCheckSumField));
        break;
      }
      case "md5": {
        editForm.get([`${fileUploadField}`])?.valueChanges.subscribe((fileAttachment) => this.calculateMD5(fileAttachment, editForm, fileUploadCheckSumField));
        break;
      }
      case "crc32": {
        editForm.get([`${fileUploadField}`])?.valueChanges.subscribe((fileAttachment) => this.calculateCRC32(fileAttachment, editForm, fileUploadCheckSumField));
        break;
      }
      case "ripemd160": {
        editForm.get([`${fileUploadField}`])?.valueChanges.subscribe((fileAttachment) => this.calculateRIPEMD160(fileAttachment, editForm, fileUploadCheckSumField));
        break;
      }
      case "sha1": {
        editForm.get([`${fileUploadField}`])?.valueChanges.subscribe((fileAttachment) => this.calculateSHA1(fileAttachment, editForm, fileUploadCheckSumField));
        break;
      }
      case "whirlpool": {
        editForm.get([`${fileUploadField}`])?.valueChanges.subscribe((fileAttachment) => this.calculateWhirlPool(fileAttachment, editForm, fileUploadCheckSumField));
        break;
      }
      case "sm3": {
        editForm.get([`${fileUploadField}`])?.valueChanges.subscribe((fileAttachment) => this.calculateSM3(fileAttachment, editForm, fileUploadCheckSumField));
        break;
      }
      default: {
        // TODO use sha512
        editForm.get([`${fileUploadField}`])?.valueChanges.subscribe((fileAttachment) => this.unsupportedAlgorithmUpdate(fileAttachment, editForm, fileUploadCheckSumField));
        break;
      }
    }
  }

  calculateSM3(fileAttachment: any, editForm: FormGroup, fileUploadCheckSumField: string): void {
    sm3(this.fileDataArray(fileAttachment)).then(sha512Token => {
      editForm.get([`${fileUploadCheckSumField}`])?.setValue(sha512Token)
    });
  }

  calculateWhirlPool(fileAttachment: any, editForm: FormGroup, fileUploadCheckSumField: string): void {
    whirlpool(this.fileDataArray(fileAttachment)).then(sha512Token => {
      editForm.get([`${fileUploadCheckSumField}`])?.setValue(sha512Token)
    });
  }
  calculate512(fileAttachment: any, editForm: FormGroup, fileUploadCheckSumField: string): void {
    sha512(this.fileDataArray(fileAttachment)).then(sha512Token => {
      editForm.get([`${fileUploadCheckSumField}`])?.setValue(sha512Token)
    });
  }

  calculate256(fileAttachment: any, editForm: FormGroup, fileUploadCheckSumField: string): void {
    sha256(this.fileDataArray(fileAttachment)).then(sha256Token => {
      editForm.get([`${fileUploadCheckSumField}`])?.setValue(sha256Token)
    });
  }

  calculate384(fileAttachment: any, editForm: FormGroup, fileUploadCheckSumField: string): void {
    sha384(this.fileDataArray(fileAttachment)).then(sha384Token => {
      editForm.get([`${fileUploadCheckSumField}`])?.setValue(sha384Token)
    });
  }

  calculateMD5(fileAttachment: any, editForm: FormGroup, fileUploadCheckSumField: string): void {
    md5(this.fileDataArray(fileAttachment)).then(token => {
      editForm.get([`${fileUploadCheckSumField}`])?.setValue(token)
    });
  }

  calculateCRC32(fileAttachment: any, editForm: FormGroup, fileUploadCheckSumField: string): void {
    crc32(this.fileDataArray(fileAttachment)).then(token => {
      editForm.get([`${fileUploadCheckSumField}`])?.setValue(token)
    });
  }

  calculateRIPEMD160(fileAttachment: any, editForm: FormGroup, fileUploadCheckSumField: string): void {
    ripemd160(this.fileDataArray(fileAttachment)).then(token => {
      editForm.get([`${fileUploadCheckSumField}`])?.setValue(token)
    });
  }

  calculateSHA1(fileAttachment: any, editForm: FormGroup, fileUploadCheckSumField: string): void {
    sha1(this.fileDataArray(fileAttachment)).then(token => {
      editForm.get([`${fileUploadCheckSumField}`])?.setValue(token)
    });
  }

  unsupportedAlgorithmUpdate(fileAttachment: any, editForm: FormGroup, fileUploadCheckSumField: string): void {
      editForm.get([`${fileUploadCheckSumField}`])?.setValue("Unsupported algorithm, Please select a cryptographic hashing algorithm")
  }

  fileDataArray(b64String: string): Uint8Array {
    const byteCharacters = atob(b64String);
    const byteNumbers = new Array(byteCharacters.length);
    for (let i = 0; i < byteCharacters.length; i++) {
      byteNumbers[i] = byteCharacters.charCodeAt(i);
    }
    return new Uint8Array(byteNumbers);
  }
}
