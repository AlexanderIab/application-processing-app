### **Endpoints by role:**

Все запросы кроме login, требуют использвания токена для отправки запросов, который 
выдается на 20 мин.

Изменить БД, username, password - используется postgres (для всего).

 - user1 -> pass1
 - user2 -> pass2
 - user3 -> pass3
 - admin1 -> pass4
 - admin2 -> pass5 
 - operator1 -> pass6 
 - operator2 -> pass7 
 - admin_operator -> pass8 
 - user_admin -> pass9 
 - user_operator -> pass10

### Dadata API

Токены для отправки запроса в файле - [dadata.properties](src%2Fmain%2Fresources%2Fdadata.properties)
Логика: при создании запроса или обновлении - берет номер телефона, получает ответ и сохраняет в отдельную таблицу, 
а пользователю добавляет PhoneDetails

### Commons:

- POST http://localhost:8080/api/auth/login


- POST http://localhost:8080/api/auth/logout


- POST http://localhost:8080/api/requests/status/search

P.S. В общих запросах т.к не указано к какой роли относится. Сортировка по дате, фильтрация по статусу.

#### Role = USER

- POST http://localhost:8080/api/requests/create

Body:
`{
"title":"Test",
"text":"Some test",
"phoneNumber":"+78121234567"
}`

- PUT http://localhost:8080/api/requests/update

Body:
`{
"id":"UUID",
"title":"Test",
"text":"Some test",
"phoneNumber":"+78121234567"
}`

P.S. Id - обязателен, остальные по желанию

- PUT http://localhost:8080/api/requests/send

Body:
`{
"UUID"
}`

- POST http://localhost:8080/api/requests/user/search

Body:
`{
"sortDirection":"desc",
"pageNumber": 0
}`

P.S. Сортировка по дате создания

#### Role = OPERATOR

- POST http://localhost:8080/api/requests/id

Body:
`{
"UUID"
}`

P.S. Просматривание заявки по id

- PUT http://localhost:8080/api/requests/accept

Body:
`{
"UUID"
}`

- PUT http://localhost:8080/api/requests/reject

Body:
`{
"UUID"
}`

- POST http://localhost:8080/api/requests/operator/search

Body:
`{
"username":"test",
"sortDirection":"asc",
"pageNumber": 0
}`

P.S. Сортировка по дате создания, фильтрация по имени / части имени

#### Role = ADMIN

- POST http://localhost:8080/api/requests/admin/search

Body:
`{
"username":"test",
"pageNumber": 0
}`

P.S. Фильтрация по имени

- GET http://localhost:8080/api/user/all

P.S. Получение всех пользователей

- POST http://localhost:8080/api/user/give/role

Body:
`{
"UUID"
}`

P.S. Присвоить роль Operator
