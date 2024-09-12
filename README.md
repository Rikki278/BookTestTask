# Тестове завдання: Створення API для управління списком книг

## RestAPI
```
http://localhost:8080/api/book
```

# Список методів:

## Get методи
- [**GET /all-books"**](#-allBooks) – *Отримати всі книги*
- [**GET /{id}**](#-bookByid) – *Отримати книгу за айді*
- [**GET /search**](#-search) – *Пошук книг за категоріями*
- [**GET /pages**](#-pages) – *Отримання всіх книг з пагінацією*

## Post методи
- [**POST /post**](#-post) – *Додавання книги*

## Put методи
- [**PUT /update**](#-update) – *Оновлення книги*

## Delete методи
- [**DELETE /delete/{id}**](#-deleteById) – *Видалення книги за айді*

# Описание методов: 

## • allBooks
Отримаємо всі книги

### Приклад запроса
```js
/localhost:8080/api/book/all-books
```
### Приклад відповіді
```json
[
    {
        "id": 1,
        "title": "Fullmetal Alchemist3",
        "author": "Sapkowski",
        "publicationYear": "2001",
        "genre": "Roman",
        "isbn": "9781591169202"
    },
    {
        "id": 2,
        "title": "Fight Club",
        "author": "Chuck Palahniuk",
        "publicationYear": "1996",
        "genre": "Psychological Fiction",
        "isbn": "9780393327342"
    }
]
```
***

## • bookbyid
Отримаємо книгу за айді

### Приклад запроса
```js
/localhost:8080/api/book/1
```
### Приклад відповіді
```json
{
    "id": 1,
    "title": "Fullmetal Alchemist3",
    "author": "Sapkowski",
    "publicationYear": "2001",
    "genre": "Roman",
    "isbn": "9781591169202"
}
```
***

## • search
Отримаємо книги за категоріями

### Приклад запроса
```js
/localhost:8080/api/book/search?genre=Fantasy&author=Sapkowski
```
### Приклад відповіді
```json
[
    {
        "id": 3,
        "title": "The Witcher: The Last Wish",
        "author": "Andrzej Sapkowski",
        "publicationYear": "1993",
        "genre": "Fantasy",
        "isbn": "9780316029187"
    },
    {
        "id": 4,
        "title": "The Witcher: Sword of Destiny",
        "author": "Andrzej Sapkowski",
        "publicationYear": "1992",
        "genre": "Fantasy",
        "isbn": "9780316389710"
    }
]
```
***

## • pages
Отримаємо книгу з пагінацією

### Приклад запроса
```js
/localhost:8080/api/book/pages?page=1&size=2
```
### Приклад відповіді
```json
{
    "content": [
        {
            "id": 3,
            "title": "The Witcher: The Last Wish",
            "author": "Andrzej Sapkowski",
            "publicationYear": "1993",
            "genre": "Fantasy",
            "isbn": "9780316029187"
        },
        {
            "id": 4,
            "title": "The Witcher: Sword of Destiny",
            "author": "Andrzej Sapkowski",
            "publicationYear": "1992",
            "genre": "Fantasy",
            "isbn": "9780316389710"
        }
    ],
    "pageable": {
        "pageNumber": 1,
        "pageSize": 2,
        "sort": {
            "empty": true,
            "sorted": false,
            "unsorted": true
        },
        "offset": 2,
        "paged": true,
        "unpaged": false
    },
    "last": true,
    "totalElements": 4,
    "totalPages": 2,
    "size": 2,
    "number": 1,
    "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
    },
    "first": false,
    "numberOfElements": 2,
    "empty": false
}
```
***

## • post
Додавання книги

### Приклад запроса
```js
/localhost:8080/api/book/post
```

### Приклад тіла
```json
{
   "title": "The Witcher: Sword of Destiny",
    "author": "Andrzej Sapkowski",
    "publicationYear": 1992,
    "genre": "Fantasy",
    "isbn": "9780316389710"
}
```

### Приклад відповіді
```json
{
    "id": 4,
    "title": "The Witcher: Sword of Destiny",
    "author": "Andrzej Sapkowski",
    "publicationYear": "1992",
    "genre": "Fantasy",
    "isbn": "9780316389710"
}
```
***

## • update
Оновлення книги

### Приклад запроса
```js
/localhost:8080/api/book/update
```

### Приклад тіла
```json
{
    "id": 1,
    "title": "UPDATE",
    "author": "Sapkowski",
    "publicationYear": 2024,
    "genre": "Roman"
}
```

### Приклад відповіді
```json
{
    "id": 1,
    "title": "UPDATE",
    "author": "Sapkowski",
    "publicationYear": "2024",
    "genre": "Roman",
    "isbn": "9781591169202"
}
```
***

## • deleteById
Видалення книги за айді

### Приклад запроса
```js
/localhost:8080/api/book/delete/1
```

### Приклад відповіді
```json
{
}
```
***
