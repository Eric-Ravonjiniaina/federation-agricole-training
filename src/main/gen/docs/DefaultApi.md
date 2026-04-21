# DefaultApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**collectivitesPost**](DefaultApi.md#collectivitesPost) | **POST** /collectivites | Ouverture d’une nouvelle collectivité |
| [**cotisationsPost**](DefaultApi.md#cotisationsPost) | **POST** /cotisations | Enregistrement d’une cotisation |
| [**membresPost**](DefaultApi.md#membresPost) | **POST** /membres | Admission d’un nouveau membre |


<a id="collectivitesPost"></a>
# **collectivitesPost**
> collectivitesPost(collectivite)

Ouverture d’une nouvelle collectivité

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DefaultApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");

    DefaultApi apiInstance = new DefaultApi(defaultClient);
    Collectivite collectivite = new Collectivite(); // Collectivite | 
    try {
      apiInstance.collectivitesPost(collectivite);
    } catch (ApiException e) {
      System.err.println("Exception when calling DefaultApi#collectivitesPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **collectivite** | [**Collectivite**](Collectivite.md)|  | |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Collectivité créée avec succès |  -  |

<a id="cotisationsPost"></a>
# **cotisationsPost**
> cotisationsPost(cotisation)

Enregistrement d’une cotisation

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DefaultApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");

    DefaultApi apiInstance = new DefaultApi(defaultClient);
    Cotisation cotisation = new Cotisation(); // Cotisation | 
    try {
      apiInstance.cotisationsPost(cotisation);
    } catch (ApiException e) {
      System.err.println("Exception when calling DefaultApi#cotisationsPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **cotisation** | [**Cotisation**](Cotisation.md)|  | |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Cotisation enregistrée |  -  |

<a id="membresPost"></a>
# **membresPost**
> membresPost(membre)

Admission d’un nouveau membre

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DefaultApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");

    DefaultApi apiInstance = new DefaultApi(defaultClient);
    Membre membre = new Membre(); // Membre | 
    try {
      apiInstance.membresPost(membre);
    } catch (ApiException e) {
      System.err.println("Exception when calling DefaultApi#membresPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **membre** | [**Membre**](Membre.md)|  | |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Membre admis avec succès |  -  |

