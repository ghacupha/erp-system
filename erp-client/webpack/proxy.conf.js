/*
 * Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
 * Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
// require('dotenv').config();

function setupProxy({ tls }) {
  const conf = [
    {
      context: [
        '/api',
        '/services',
        '/management',
        '/swagger-resources',
        '/v2/api-docs',
        '/v3/api-docs',
        '/h2-console',
        '/auth',
        '/health',
      ],
      // target: `http${tls ? 's' : ''}://localhost:8980`,
      target: process.env.SERVER_API_URL_URL,
      secure: false,
      changeOrigin: tls,
    },
    {
      context: ['/websocket'],
      // target: 'ws://127.0.0.1:8980',
      target: process.env.SERVER_API_WS_URL,
      ws: true,
    },
  ];
  return conf;
}

module.exports = setupProxy;
