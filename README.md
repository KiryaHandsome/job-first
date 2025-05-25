# Дипломная работа (Пригожий Кирилл, 153504)

## Платформа для поиска работы молодым специалистам


### Steps to run application

1. Run `docker build -t diplom-be .` in kotlin folder (PostgreSQL need to be installed locally)
2. Run `docker build -t diplom-fe .` in javascript folder
3. Run `docker run -p 8080:8080 diplom-be`
4. Run `docker run -p 80:80 diplom-fe`
5. Access site in http://localhost
