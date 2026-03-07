POST /api/auth/register

Request:
{
 "name": "Rahul",
 "email": "rahul@mail.com",
 "password": "123456",
 "role": "CUSTOMER"
}

POST /api/auth/login

Response:
{
 "token": "jwt-token"
}

GET /api/trucks

POST /api/bookings

GET /api/bookings/{id}/tracking