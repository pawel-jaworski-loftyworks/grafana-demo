List all users

```bash
curl -H "X-Request-ID: $(uuidgen)" http://localhost:8081/api/users 
```

Broken request with 500

```bash
curl -H "X-Request-ID: $(uuidgen)" -H 'Content-Type: application/json' -X POST -d '{"firstName": "Pawel", "lastName": "Jaworski", "email": "pawel.jaworski@loftyworks.com"}' http://localhost:8081/api/users 
```

Create a new user

```bash
curl -H "X-Request-ID: $(uuidgen)" -H 'Content-Type: application/json' -X POST -d '{"username": "pawelj", "firstName": "Pawel", "lastName": "Jaworski", "email": "pawel.jaworski@loftyworks.com"}' http://localhost:8081/api/users > user.json &&USER_ID=$(cat user.json | jq -r '.id') 
```

Get permissions for this user
```bash
curl -H "X-Request-ID: $(uuidgen)" -H 'Content-Type: application/json' http://localhost:8083/api/permissions/user/${USER_ID} | jq 
```


Get documents for user

```bash
curl -H "X-Request-ID: $(uuidgen)" http://localhost:8082/api/documents/user/$USER_ID
```

Get an exception

```bash
curl -H "X-Request-ID: $(uuidgen)" http://localhost:8082/api/documents/user/$(uuidgen)
```