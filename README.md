# Shared libraries
## Introduction

This project contains different modules implementing common configuration and some standard for developing spring boot 
applications. This project is a multi-module maven project. Each starter module is a wrapper around spring boot starter
module for different project. It contains some common configuration and standard for developing spring boot applications.


## Project Structure
For a project (like web) there is separate module. i.e `skmonjurul-web`. And for each module there are `starter` module
under `skmonjurul-starters` directory. i.e `skmonjurul-starter-web`. Each starter module is a wrapper around spring boot 
starter module. It contains some common configuration and standard for developing spring boot applications.

## How to create a new project
To create a new project, you need to create a new module under root directory `skmonjurul-<project_name>`. 
Then create a new module under `skmonjurul-starters`, i.e `skmonjurul-starter-<project_name>`. 
Now if you want to create autoconfiguration **_(please note autoconfiguration is not mandatory)_** for your new project, you need to create a new package under 
`skmonjurul-autoconfigure` with name `com.skmonjurul.shared_library.autoconfigure.<project_name>`. 
Then create a new class `AutoConfiguration` under this package.

## Reference Implementation
* [skmonjurul-starter-web](./skmonjurul-starters/skmonjurul-starter-web/README.md#custom-spring-boot-starter-for-spring-boot-starter-web)