#
# ERP System - ERP data management platform
# Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program. If not, see <http://www.gnu.org/licenses/>.
#

# Stage 1
FROM node:10-alpine AS compile-image

WORKDIR /opt/app
# Enable the line below for local dev behind proxy
COPY .npmrc /opt/app
COPY package.json /opt/app
COPY package-lock.json /opt/app

COPY . /opt/app
RUN npm install

ENV PATH="./node_modules/.bin:$PATH"

RUN npm run build --prod

# Stage 2
FROM nginx
COPY src/main/docker/nginx/nginx-default.conf /etc/nginx/conf.d/default.conf
COPY --from=compile-image /opt/app/build/resources/main/static /usr/share/nginx/html

CMD ["nginx", "-g", "daemon off;"]