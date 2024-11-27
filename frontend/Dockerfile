FROM node:18 AS build

WORKDIR /app

COPY package*.json ./

RUN npm install

RUN npm audit fix

COPY . .

RUN npm run build --omit=dev

FROM nginx:alpine

RUN rm -r /usr/share/nginx/html/*
COPY --from=build /app/dist/jung-garde-frontend/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf

CMD ["nginx", "-g", "daemon off;"]
