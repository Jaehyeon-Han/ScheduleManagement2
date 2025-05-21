# DefaultApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**deleteUserById**](DefaultApi.md#deleteUserById) | **DELETE** /users/{userId} | 사용자 삭제 |
| [**findUserById**](DefaultApi.md#findUserById) | **GET** /users/{userId} | 고유 식별자로 사용자 조회 |
| [**getUserList**](DefaultApi.md#getUserList) | **GET** /users | 전체 사용자 페이지네이션 조회 |
| [**saveUser**](DefaultApi.md#saveUser) | **POST** /users |  |
| [**updateUserById**](DefaultApi.md#updateUserById) | **PATCH** /users/{userId} | 사용자 정보를 변경 |


<a name="deleteUserById"></a>
# **deleteUserById**
> deleteUserById(userId)

사용자 삭제

    사용자를 DB에서 삭제한다.

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **userId** | **Integer**|  | [default to null] |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="findUserById"></a>
# **findUserById**
> UserResponse findUserById(userId)

고유 식별자로 사용자 조회

    DB의 id로 사용자를 조회한다.

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **userId** | **Integer**|  | [default to null] |

### Return type

[**UserResponse**](../Models/UserResponse.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="getUserList"></a>
# **getUserList**
> List getUserList(page, size, sort)

전체 사용자 페이지네이션 조회

    기본적으로는 등록일 기준 내림차순 정렬, 페이지 1번, 한 페이지 사용자 정보 10개씩 조회

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **page** | **Integer**| 조회하려는 페이지 | [optional] [default to 0] |
| **size** | **Integer**| 한 페이지에 포함시킬 사용자 수 | [optional] [default to 10] |
| **sort** | **String**| 정렬 기준 | [optional] [default to createdAt,desc] |

### Return type

[**List**](../Models/UserResponse.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="saveUser"></a>
# **saveUser**
> saveUser(CreateUserRequest)



### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **CreateUserRequest** | [**CreateUserRequest**](../Models/CreateUserRequest.md)|  | [optional] |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

<a name="updateUserById"></a>
# **updateUserById**
> UserResponse updateUserById(userId)

사용자 정보를 변경

    자신의 비밀번호를 변경할 수 있다.

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **userId** | **Integer**|  | [default to null] |

### Return type

[**UserResponse**](../Models/UserResponse.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

