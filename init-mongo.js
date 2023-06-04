// init-mongo.js



// Switch to the "retailStore" database
db = db.getSiblingDB('retailStore');

// Create a new "users" collection if it doesn't already exist
db.createCollection('users');

if (db.users.count() === 0) {
// Insert new user documents with different types
  db.users.insert([
    {
      "_id": ObjectId(),
      "userName": "employeeUser",
      "userType": "EMPLOYEE",
      "registrationDate": ISODate("2021-01-01T00:00:00Z"),
      "password": "$2a$10$w7698TS6Mds7zFOGHWMiKus0gO/9RU3PIY88gQsHEWglbe51zDov." // hashed password 'password'
    },
    {
      "_id": ObjectId(),
      "userName": "affiliateUser",
      "userType": "AFFILIATE",
      "registrationDate": ISODate("2021-01-01T00:00:00Z"),
      "password": "$2a$10$w7698TS6Mds7zFOGHWMiKus0gO/9RU3PIY88gQsHEWglbe51zDov." // hashed password 'password'
    },
    {
      "_id": ObjectId(),
      "userName": "loyalCustomer",
      "userType": "CUSTOMER",
      "registrationDate": ISODate("2019-01-01T00:00:00Z"),
      "password": "$2a$10$w7698TS6Mds7zFOGHWMiKus0gO/9RU3PIY88gQsHEWglbe51zDov." // hashed password 'password'
    },
    {
      "_id": ObjectId(),
      "userName": "newCustomer",
      "userType": "CUSTOMER",
      "registrationDate": ISODate("2022-01-01T00:00:00Z"),
      "password": "$2a$10$w7698TS6Mds7zFOGHWMiKus0gO/9RU3PIY88gQsHEWglbe51zDov." // hashed password 'password'
    }
  ]);
}

// Create a new "products" collection if it doesn't already exist
db.createCollection('products');

if (db.products.count() === 0) {
// Insert new product documents
  db.products.insert([
    {
      "_id": ObjectId(),
      "name": "Product 1",
      "type": "GROCERY",
      "price": 10.0
    },
    {
      "_id": ObjectId(),
      "name": "Product 2",
      "type": "GROCERY",
      "price": 20.0
    },
    {
      "_id": ObjectId(),
      "name": "Product 3",
      "type": "ELECTRONICS",
      "price": 30.0
    },
    {
      "_id": ObjectId(),
      "name": "Product 4",
      "type": "ELECTRONICS",
      "price": 40.0
    },
    {
      "_id": ObjectId(),
      "name": "Product 5",
      "type": "GROCERY",
      "price": 50.0
    },
    {
      "_id": ObjectId(),
      "name": "Product 6",
      "type": "ELECTRONICS",
      "price": 60.0
    },
    {
      "_id": ObjectId(),
      "name": "Product 7",
      "type": "GROCERY",
      "price": 70.0
    },
    {
      "_id": ObjectId(),
      "name": "Product 8",
      "type": "ELECTRONICS",
      "price": 80.0
    },
    {
      "_id": ObjectId(),
      "name": "Product 9",
      "type": "GROCERY",
      "price": 90.0
    },
    {
      "_id": ObjectId(),
      "name": "Product 10",
      "type": "ELECTRONICS",
      "price": 100.0
    },
  ]);
}
// Create a new user and give read/write permissions on the "retailStore" database
db.createUser(
    {
      user: "retailStoreUser",
      pwd: "password",
      roles: [ { role: "readWrite", db: "retailStore" } ]
    }
);
