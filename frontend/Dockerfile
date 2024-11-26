FROM node:18 AS build

WORKDIR /app

COPY package*.json ./

RUN npm install

COPY . .

RUN npm run build --omit=dev

FROM nginx:alpine

RUN rm -r /usr/share/nginx/html/*
COPY --chown=777 --from=build /app/dist/jung-garde-frontend/browser /usr/share/nginx/html
COPY --chown=777 nginx.conf /etc/nginx/nginx.conf

USER 777

CMD ["nginx", "-g", "daemon off;"]
