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

/**
 * Ties one "Create New" click to its eventual completion in the inlineCreateReturnState
 * slice, including across a nested create (Invoice while creating a Settlement). Falls back
 * off crypto.randomUUID for environments (older browsers, some test runners) without it.
 */
export function generateCorrelationId(): string {
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const randomUUID = typeof crypto !== 'undefined' ? (crypto as any).randomUUID : undefined;
  if (typeof randomUUID === 'function') {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-call
    return String(randomUUID.call(crypto));
  }
  return `ic-${Date.now()}-${Math.random().toString(36).slice(2)}`;
}
