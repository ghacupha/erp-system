///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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

export interface ITerminalFunctions {
  id?: number;
  functionCode?: string;
  terminalFunctionality?: string;
}

export class TerminalFunctions implements ITerminalFunctions {
  constructor(public id?: number, public functionCode?: string, public terminalFunctionality?: string) {}
}

export function getTerminalFunctionsIdentifier(terminalFunctions: ITerminalFunctions): number | undefined {
  return terminalFunctions.id;
}
