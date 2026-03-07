Table: users
id
name
email
phone
password_hash
role
created_at

Table: transporters
id
user_id
company_name
rating
verified

Table: trucks
id
transporter_id
truck_number
truck_type
capacity
location_city
status

Table: bookings
id
customer_id
truck_id
source
destination
weight
price
status
created_at