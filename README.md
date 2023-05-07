# Лабораторная работа №2

Доработать приложение из лабораторной работы #1, реализовав в нём управление транзакциями и разграничение доступа к операциям бизнес-логики в соответствии с заданной политикой доступа.
Управление транзакциями необходимо реализовать следующим образом:
1.	Переработать согласованные с преподавателем прецеденты (или по согласованию с ним разработать новые), объединив взаимозависимые операции в рамках транзакций.
2.	Управление транзакциями необходимо реализовать с помощью Spring JTA.
3.	В реализованных (или модифицированных) прецедентах необходимо использовать программное управление транзакциями.
4.	В качестве менеджера транзакций необходимо использовать Atomikos.  

Разграничение доступа к операциям необходимо реализовать следующим образом:
1.	Разработать, специфицировать и согласовать с преподавателем набор привилегий, в соответствии с которыми будет разграничиваться доступ к операциям.
2.	Специфицировать и согласовать с преподавателем набор ролей, осуществляющих доступ к операциям бизнес-логики приложения.
3.	Реализовать разработанную модель разграничений доступа к операциям бизнес-логики на базе Spring Security. Информацию об учётных записях пользователей необходимо сохранять в бд, для аутентификации использовать JWT.

![Добавить кул новость](https://user-images.githubusercontent.com/70958074/236685111-9d478368-3d45-4bcb-ac4f-d62a3572acce.svg)
![Добавить рецепт](https://user-images.githubusercontent.com/70958074/236685115-86e1157f-af05-4e89-978d-8d7659295c04.svg)
![Принять_Отклонить рецепт](https://user-images.githubusercontent.com/70958074/236685117-6a50190e-d3f5-4d23-b490-a57e1051f9f3.svg)
![Просмотр всех рецептов на проверке](https://user-images.githubusercontent.com/70958074/236685120-424cfb0e-eba0-40d5-9cdc-0c6bbe9a6db8.svg)
![Просмотр кул новостей](https://user-images.githubusercontent.com/70958074/236685122-13ef904b-339c-451b-b984-65df29cc3ce9.svg)
![Просмотр рецептов](https://user-images.githubusercontent.com/70958074/236685124-eb66432b-a00a-4778-a2f8-c49a445b208a.svg)
![Редактирование_Удаление рецептов](https://user-images.githubusercontent.com/70958074/236685125-cac96321-ed83-42e0-a34a-15db0c74ef6c.svg)
