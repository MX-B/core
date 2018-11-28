# Gr1d Java Core
This project was created to share common codes across multiple Java Services. The common features will be described bellow.

## Features
More features will be added as long as most of java services use and need.

---

### Default Messaging

#### `CreatedResponse`
To return only `{ "uuid": "generated-uuid" }`

#### `Gr1dError`
Common error structure to return when any error happens.

#### `PageResult`
If an endpoint has the need to return a paginated list
```$json
{
    "content": [],
    "total_elements": 0,
    "page": 0,
    "size" 0
}
```

#### `Gr1dHttpException`
To return an specific exception, this Exception receive the HttpStatus to return

#### `Gr1dNotFoundException`
Extending `Gr1dHttpException` to always return 404.

---

### StrategyResolver
Some projects might need strategies and this is the common code to resolve these strategies for a given `String`.

---

### Validators

#### CPFCNPJValidator
To validate a field which can be either CPF or CNPJ.

#### ValueValidator
To validate a string field which allows a limited number of values.
> For example personType ['INDIVIDUAL', 'COMPANY']

#### DateRangeValidator
To validade two dates and `start_date` to never be greater of `end_date`.

---

### Healthcheck
Common healthcheck endpoint

---

### Swagger
Automatic Swagger doc generation

---

## Publish policies
On branch Develop, we have to always use `-SNAPSHOT` flag 

## Testing
> mvn test

## Contributors
- Sérgio Marcelino
- Efraim Coutinho