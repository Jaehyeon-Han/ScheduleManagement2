# Documentation for 할일 관리 API

<a name="documentation-for-api-endpoints"></a>
## Documentation for API Endpoints

All URIs are relative to *http://localhost*

| Class | Method | HTTP request | Description |
|------------ | ------------- | ------------- | -------------|
| *DefaultApi* | [**deleteUserById**](Apis/DefaultApi.md#deleteuserbyid) | **DELETE** /users/{userId} | 사용자 삭제 |
*DefaultApi* | [**findUserById**](Apis/DefaultApi.md#finduserbyid) | **GET** /users/{userId} | 고유 식별자로 사용자 조회 |
*DefaultApi* | [**getUserList**](Apis/DefaultApi.md#getuserlist) | **GET** /users | 전체 사용자 페이지네이션 조회 |
*DefaultApi* | [**saveUser**](Apis/DefaultApi.md#saveuser) | **POST** /users |  |
*DefaultApi* | [**updateUserById**](Apis/DefaultApi.md#updateuserbyid) | **PATCH** /users/{userId} | 사용자 정보를 변경 |


<a name="documentation-for-models"></a>
## Documentation for Models

 - [CreateUserRequest](./Models/CreateUserRequest.md)
 - [ErrorResponse](./Models/ErrorResponse.md)
 - [FieldErrorResponse](./Models/FieldErrorResponse.md)
 - [UpdateUserRequest](./Models/UpdateUserRequest.md)
 - [UserResponse](./Models/UserResponse.md)
 - [access-date-time](./Models/access-date-time.md)


<a name="documentation-for-authorization"></a>
## Documentation for Authorization

All endpoints do not require authorization.
