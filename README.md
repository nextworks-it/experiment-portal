### Prerequisites

* [Oracle-Java8] - Oracle version 8 is preferred, otherwise [OpenJDK-8] 
* [Maven] - version 3.3.9 is required
* [PostgresSQL] - a DB should be created to be used by the Experiment LCM


### Dependencies

* The following table enumerates the dependencies that should be installed to run the Experiment LCM
* Each dependency should be installed by downloading the code from the repository, 
* and running "mvn clean install" inside the dependency folder. 

| Dependency | Repository link | Folder |
| --- | --- | --- |
| NfvManoLibsCommon | https://github.com/nextworks-it/nfv-ifa-libs/tree/feat-librefactor | NFV_MANO_LIBS_COMMON |
| NfvManoLibsDescriptors | https://github.com/nextworks-it/nfv-ifa-libs/tree/feat-librefactor | NFV_MANO_LIBS_DESCRIPTORS |
| NfvManoLibsIfa13NsLcmIf | https://github.com/nextworks-it/nfv-ifa-libs/tree/feat-librefactor | NFV_MANO_LIBS_IFA13_NS_LCM_IF |
| --- | --- | --- |
| VsBlueprintsIM | https://github.com/nextworks-it/slicer-catalogue/tree/5geve-release | VS_BLUEPRINTS_IM |
| EveBlueprintsIM | https://github.com/nextworks-it/slicer-catalogue/tree/5geve-release | EVE_BLUEPRINTS_IM |
| VsBlueprintsCatalogueInterfaces | https://github.com/nextworks-it/slicer-catalogue/tree/5geve-release | VS_BLUEPRINTS_CATALOGUE_INTERFACES |
| EveBlueprintsCatalogueInterfaces | https://github.com/nextworks-it/slicer-catalogue/tree/5geve-release | EVE_BLUEPRINTS_CATALOGUE_INTERFACES |
| TranslatorServiceInterface | https://github.com/nextworks-it/slicer-catalogue/tree/5geve-release | TranslatorServiceInterface |
| --- | --- | --- |
| NfvoLcmAbstractDriver | https://github.com/nextworks-it/nfvo-drivers | NFVO_LCM_ABSTRACT_DRIVER |
| msnoClient | https://github.com/nextworks-it/nfvo-drivers | msnoClient |
| OsmClient | https://github.com/nextworks-it/nfvo-drivers | OSM_CLIENT |
| NfvoLcmSpecificDrivers | https://github.com/nextworks-it/nfvo-drivers | NFVO_LCM_SPECIFIC_DRIVERS |
| NfvoLcmService | https://github.com/nextworks-it/nfvo-drivers | NFVO_LCM_SERVICE |
| --- | --- | --- |

### Configuration 
* The Experiment LCM configured should be updated using the correct values inside the application.properties file (src/main/resources folder). 
* Example values have been provided inside the file to help the configuration. 

### Compilation and Execution
* Compile and Execute by executing "mvn clean install" and "mvn spring-boot:run" inside the ExperimentLifecycleManager folder

