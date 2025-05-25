# --- Stage 1: Build the React application ---
FROM node:20-alpine as build

WORKDIR /app

COPY package.json ./
COPY package-lock.json ./

RUN npm install --force

COPY . .

RUN npm run build

FROM nginx:alpine as production

COPY nginx.conf /etc/nginx/conf.d/default.conf

COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]