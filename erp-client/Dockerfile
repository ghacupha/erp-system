# Stage 1
FROM node:14.17-alpine AS compile-image

WORKDIR /opt/app
# Enable the line below for in-container npm configurations
COPY .npmrc /opt/app
COPY package.json /opt/app
COPY package-lock.json /opt/app

COPY . /opt/app
RUN npm install

ENV PATH="./node_modules/.bin:$PATH"

RUN npm run webapp:prod

# Stage 2
FROM nginx
COPY src/main/docker/nginx/nginx-default.conf /etc/nginx/conf.d/default.conf
COPY --from=compile-image /opt/app/target/classes/static /usr/share/nginx/html

CMD ["nginx", "-g", "daemon off;"]
