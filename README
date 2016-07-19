# SuperClock

This is a very simple Grails application exposing a REST API consumed by an Angular.js application.

## Requirements

- Java 7
- Grails 2.3.11

## Test data

To facilitate testing the application (in a non-production environment), there are three users already created:

| username | password | role |
| --- | --- | --- |
| user | user | USER |
| user2 | user2 | USER |
| admin | admin | ADMIN |

## Web application

After running the `grails run-app` command in your console, you can access the application at:

```
http://localhost:8080/superclock
```

## REST API

### Authentication

There are only two endpoints which do not require authentification.

The first one is the signup functionality. This creates a new user that can be then used to access the application. Its role will always be USER.

```
POST /api/signup

{
  "username":<username>,
  "password":<password>
}
```

Then, of course, there is the endpoint to request a new access token:

```
POST /api/login

{
  "username":<username>,
  "password":<password>
}
```

If the username and password are correct, you will get an access token in the response body.

Every other endpoint requires using that access token as the value of the `X-Auth-Token` header. If not valid or not present, a 401 status code will be responded.

### Timezones

These endpoints are accessible to **USER** and **ADMIN** roles.

| Method | URI | Description |
| --- | --- | --- |
| GET | /api/timezones | Lists the timezones created by the user (or all timezones if admin) |
| GET | /api/timezones/{id} | Get the timezone with the specified id. Returns 404 if it does not exist or 403 if the timezone belongs to another user (and the current user is not an admin). |
| POST | /api/timezones | Creates a new timezone with the provided data in the request body. Returns 201 if the entity was created or 422 if the server could not process the provided data. |
| PUT | /api/timezones/{id} | Updates an existing timezone with the provided data in the request body. Returns 200 if the entity was updated successfully or 422 if the server could not process the request data. It also returns 404 if the timezone does not exist or 403 if it belongs to another user (and the current user is not an admin). |
| DELETE | /api/timezones/{id} | Deletes an existing timezone. Returns 200 if the entity was successfully deleted. It also returns 404 if the timezone does not exist or 403 if it belongs to another user (and the current user is not an admin). |

### Users

These endpoints are only accessible to users with the **ADMIN** role.

| Method | URI | Description |
| --- | --- | --- |
| GET | /api/users | Lists all the users |
| GET | /api/users/{id} | Get the user with the specified id. Returns 404 if it does not exist. |
| POST | /api/users | Creates a new user with the provided data in the request body. Returns 201 if the entity was created or 422 if the server could not process the provided data. |
| PUT | /api/users/{id} | Updates an existing user with the provided data in the request body. Returns 200 if the entity was updated successfully or 422 if the server could not process the request data. It also returns 404 if the user does not exist. |
| DELETE | /api/users/{id} | Deletes an existing user. Returns 200 if the entity was successfully deleted. It also returns 404 if the user does not exist. If the user deletes itself, it will no longer have access to the application. |