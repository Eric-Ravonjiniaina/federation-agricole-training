

# Membre


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**id** | **String** |  |  [optional] |
|**nom** | **String** |  |  [optional] |
|**prenom** | **String** |  |  [optional] |
|**dateNaissance** | **LocalDate** |  |  [optional] |
|**genre** | [**GenreEnum**](#GenreEnum) |  |  [optional] |
|**adresse** | **String** |  |  [optional] |
|**metier** | **String** |  |  [optional] |
|**telephone** | **String** |  |  [optional] |
|**email** | **String** |  |  [optional] |
|**dateAdhesion** | **LocalDate** |  |  [optional] |
|**poste** | [**PosteEnum**](#PosteEnum) |  |  [optional] |



## Enum: GenreEnum

| Name | Value |
|---- | -----|
| MASCULIN | &quot;masculin&quot; |
| FEMININ | &quot;feminin&quot; |



## Enum: PosteEnum

| Name | Value |
|---- | -----|
| PRESIDENT | &quot;president&quot; |
| PRESIDENT_ADJOINT | &quot;presidentAdjoint&quot; |
| TRESORIER | &quot;tresorier&quot; |
| SECRETAIRE | &quot;secretaire&quot; |
| MEMBRE_CONFIRME | &quot;membreConfirme&quot; |
| MEMBRE_JUNIOR | &quot;membreJunior&quot; |



