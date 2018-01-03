# CRDT

-LWW Set implementation in Java.

Last-Write-Wins-Element Set. Uses 'timestamps' associated with addition and deletion operations for picking the winner with a bias towards keeping the elements.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

need maven and java installed


### Installing

Clone the project and Import as Maven project in the IDE. Run build from root directory of the project

```
mvn clean install

```


## Running the tests

Run the JUnit test cases from IDE or using maven

```
mvn test
```

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management


## Authors

* **Manas Sikder** - *Initial work* - [manassikder](https://github.com/manassikder)


## License

This project is licensed under the Apache License - see the [LICENSE.md](LICENSE) file for details



