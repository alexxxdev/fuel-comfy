# fuel-comfy 
[![](https://jitpack.io/v/alexxxdev/fuel-comfy.svg)](https://jitpack.io/#alexxxdev/fuel-comfy)

More comfortable use of [Fuel](https://github.com/kittinunf/fuel) as in [Retrofit](https://square.github.io/retrofit/) or [Feign](https://github.com/OpenFeign/feign)

## Basics

Usage typically looks like this

```kotlin
@FuelInterface
interface GitHubService {

    @Get("repos/{owner}/{repo}/contributors")
    fun contributors(@Param("owner") owner: String, @Param("repo") repo: String): Result<Contributor, Exception>
}

FuelManager.instance.basePath = "https://api.github.com/"

val service = FuelManager.instance.setInterface(GitHubService::class)
val contributors = service.contributors("alexxxdev", "fuel-comfy")
contributors.fold({ list ->
            // TODO
        }, { exception ->
            // TODO
        })

```

## Features
- [x] HTTP GET/POST requests
- [x] Serialization/Deserialization using [fuel-kotlinx-serialization](https://github.com/kittinunf/fuel/tree/master/fuel-kotlinx-serialization)
- [x] Support `suspend` function
- [ ] HTTP PUT/DELETE/HEAD/PATCH requests
- [ ] Serialization/Deserialization using [Gson](https://github.com/google/gson)
- [ ] Maybe something else ...
<br><br>

### Interface Annotations

#### @FuelInterface

Defines the interface for fuel-comfy

#### @Get

Defines the GET HttpMethod and UriTemplate for request. Expressions, values wrapped in curly-braces {expression} are resolved using their corresponding @Param annotated parameters.

#### @Post

Defines the POST HttpMethod and UriTemplate for request. Expressions, values wrapped in curly-braces {expression} are resolved using their corresponding @Param annotated parameters.

#### @Param

Defines a template variable, whose value will be used to resolve the corresponding template Expression, by name.

#### @Header

When used on a Interface, will be applied to every request. When used on a Method, will apply only to the annotated method.

```kotlin
@FuelInterface
@Headers("Accept: application/json")
interface GitHubService {

  @Headers("Content-Type: application/json")
  @POST("...")
  fun post()
}
```

#### @QueryMap

Defines a Map of name-value pairs, to expand into a query string.

```kotlin
@FuelInterface
interface GitHubService {

  @GET("...")
  fun get(@QueryMap params: Map<String, Any>)
}
```

#### @Query

Defines name-value pair, to expand into a query string.

```kotlin
@FuelInterface
interface GitHubService {

  @GET("...")
  fun get(@Query("id") id: Int)
}
```

####  @Body	

Defines a request body

```kotlin
@FuelInterface
interface GitHubService {

  @GET("...")
  fun get(@Body contributor: Contributor)
}
```

<br><br><br>
## Installation
Add it in your root build.gradle at the end of repositories:
```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
Add the dependency
```groovy
dependencies {
    implementation 'com.github.alexxxdev.fuel-comfy:api:-SNAPSHOT'
    kapt 'com.github.alexxxdev.fuel-comfy:processor:-SNAPSHOT'
}
```
