# CSVonFHIR
Project developed during the #codevscovid19 hackathon in 2020.

## Introduction
### Goal
The goal of this project is to create a dynamic parser which generates FHIR document based on a CSV input and then populates
this FHIR document with the CSV data.

### Purpose of this project
[FHIR](https://www.hl7.org/fhir/index.html) is a standard which describes data formats and resources as well as an API for
exchanging electronic health records. 
Since hospitals and other health care parties use a variety of systems and services it becomes difficult to exchange data
in a fast and meaningful way. 
Furthermore, adopting a new standard is often times coupled with a lot of effort. 
We try to tackle part of what causes this problem by creating an easy way to convert patient data stored in a CSV format 
into the FHIR format. 
This will offer an easy way to make data accessible and sharable between various groups without much effort. 

## Setup 
1. Clone the project from github
2. Use Maven to import the basic dependencies of the project
3. Download the following the [FHIR Validator jar](https://github.com/ahdis/cda-core-2.0/releases/download/v0.0.2-dev/org.hl7.fhir.validation.cli.jar) 
4. Add the jar as a library to your project as described [in this guide by jetbrains](https://www.jetbrains.com/help/idea/library.html#define-a-project-library). 
<br>This is required, since this program is currently using a patched version of the 
FHIR Validator, which is not available as a Maven dependency. 

### Run Configuration
#### IntelliJ Setup
In case you are using IntelliJ, the default run configuration is included in your project and should be available as "Main.java".

#### Other Development Environments
If you are using a different development environment than IntelliJ you have to create a run configuration. For this you 
can use the following default configuration which uses the included example files:
* Input CSV: Covid_Data_Final.csv
* FHIR Map: CovidDataFinalMap.map

Following are the parameter values you need to set: 
```
 -i
 ./res/inputCSV/Covid_Data_Final.csv
 -t
 CovidDataFinal
 -m
 ./res/maps/CovidDataFinalMap.map
 -d
 ./res/parsedCSV/PatientDataList.json
 -s
 ./res/structuredefinition/CovidDataFinalStructureDef.json
 -o
 ./res/FHIR/
```
More information about this configuration and how to adjust it can be found in the section [User Manual](#UserManual).

## <a name="UserManual"></a>User Manual
CSVonFHIR is executed over the command line like a standard Java application via the command `java -jar CSVonFHIR.jar`.
The application uses command line parameters to accept input:
  - Required arguments:
    - `-i [path]`: The path to the input CSV file. This file is then parsed into JSON and fed through the FHIR transformator.
    - `-t [string]`: The string to use in the FHIR format as the type of the data.
    - `-m [path]`: The path to the mapping file (in FHIR mapping language) that serves to translate the output to FHIR.
  - Optional arguments:
    - `-d [path]`: The path where the output data JSON (the converted input CSV) is saved. The default value is `./data.json`.
    - `-s [path]`: The path where the FHIR structure definition file is saved. The default value is `./structure-definition.json`.
    - `-o [path]`: The path where the generated FHIR files are saved to. The default value is `./fhir_output/`.



