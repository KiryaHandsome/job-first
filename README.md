# Дипломная работа (Пригожий Кирилл, 153504)

## Платформа для поиска работы молодым специалистам

### Реализовано

* Логин и регистрация
* Получение списка вакансий с фильтрами
* Кабинет соискателя
    * Редактирование данных о пользователе
    * Создание/редактирование резюме
    * Отклик на вакансии
* Кабинет работодателя
    * Создание компании
    * Информация по созданным вакансиям

### Осталось Реализовать

* Кабинет работодателя
    * Создание вакансии
        * Подсчет стоимости выкладки вакансии
    * Добавление пользователей в компанию

### Steps to run application

1. Run `docker build -t diplom-be .` in kotlin folder (PostgreSQL need to be installed locally)
2. Run `docker build -t diplom-fe .` in javascript folder
3. Run `docker run -p 8080:8080 diplom-be` in javascript folder
4. Run `docker run -p 80:80 diplom-fe`
5. Access site in http://localhost
