# Welcome to the AVMFramework (AVM_f_)

AVM_f_ is a framework and Java implementation of the Alternating Variable Method (AVM), a heuristic local search algorithm originally due to Korel (1990). The AVM was originally designed to optimize the values of integer variables according to an objective function. It has since been extended to handle floating point numbers (Harman and McMinn, 2007) and strings (McMinn et al., 2015), and enhanced with different local search algorithm that are proven to be faster under certain conditions (Kempka et al., 2015).

AVM_f_ was designed to enable researchers and practitioners understand the algorithms behind the AVM and use it in their projects.

## Installing Maven

The AVM_f_ project has been implemented using Maven, a build automation tool for projects programmed in the Java
programming language. If you wish to build the AVM_f_ tool from its source code, then you will first need to install
Maven on your workstation. If you have already installed Maven, then please go directly to the next section. Otherwise,
follow the installation guidelines at https://maven.apache.org/install.html.

## Downloading and Installing

1. Clone the AVM_f_ project repository using either a graphical Git client or by running the following command at the
	 prompt of your terminal window:

	 `git clone https://github.com/AVMf/avmf.git`

2. To run the examples provided with AVM_f_, you will need to build AVM_f_ from its source code by following the instructions in the next section.

### Installing

As AVM_f_ has been implemented as a Maven project using the Java programming language, the easiest method of
generating the executable tool involves importing the project into an integrated development environment (IDE) and
generating the Java archive (JAR) from inside the IDE. Instructions are presented for doing this using two common IDEs:
Eclipse (https://www.eclipse.org/downloads/) and IntelliJ (https://www.jetbrains.com/idea/download/). However, if you
would prefer to build the project using the command line in an appropriate terminal emulator, then instructions to do so
are also provided.

#### Java Version

AVM_f_ has been implemented to run using Java Development Kit (JDK) 7 or 8, which can be downloaded from
http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html and
http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html, respectively. Instead of
downloading the JDK from one of the aforementioned web sites, you can also use your operating system's package manager
to install it correctly. After downloading and installing the JDK, you are also likely to have to set Java 1.7 (or, Java
1.8) as the chosen Java Development Kit for the AVM_f_ project. Please follow the instructions provided by either
your operating system or your integrated development environments to accomplish this task.

#### Installation with Eclipse

1. Select 'File' &rarr; 'Import'.
2. From the project options, select 'Maven' &rarr; 'Existing Maven Projects'.
3. Select the root directory of your downloaded copy of AVM_f_.
4. Click 'Finish' to complete the import.
5. To generate the JAR file, select 'Run' &rarr; 'Run As' &rarr; 'maven install'.
6. A JAR file called `avm-jar-with-dependencies.jar` should have been created in the `target` directory of AVM_f_'s main directory; if this JAR file does not exist, then the installation with Eclipse failed and you will not yet be able to use AVM_f_. Please try these steps again or, alternatively, try another IDE or the command-line-based approach.

#### Installation with IntelliJ

1. Select 'File' &rarr; 'Open'.
2. Navigate to the root directory of your installation of AVM_f_.
3. Select the 'pom.xml' file and click 'Finish'.
4. Open the Maven Projects toolbar using 'View' &rarr; 'Tool Windows' &rarr; 'Maven Projects'.
5. Select the AVM_f_ project and click 'package'.
6. A JAR file called `avm-jar-with-dependencies.jar` should have been created in the `target` directory of AVM_f_'s main directory; if this JAR file does not exist, then the installation with IntelliJ failed and you will not yet be able to use AVM_f_. Please try these steps again or, alternatively, try another IDE or the command-line-based approach.

#### Installation at the Command Line

1. Navigate to the root directory containing of your installation of AVM_f_.
2. Type the following command to build the tool: `mvn package`
3. Maven will build the project from scratch, downloading all the required dependencies for the project automatically.
6. A JAR file called `avm-jar-with-dependencies.jar` should have been created in the `target` directory of AVM_f_'s main directory; if this JAR file does not exist, then the installation with the command line failed and you will not yet be able to use AVM_f_. Please try these steps again or, alternatively, try one of the methods that uses an IDE.

## Running the Provided Examples

AVM_f_ includes various examples of the AVM optimizing different problems. `Quadratic` finds the roots of a quadratic equation. `AllZeros` optimizes a vector of initially arbitrary integer values to zeros. `String` optimizes an initially random string to some desired target string.

If you have already installed and built AVM_f_ as detailed in the previous sections, you can run these examples from the command line as follows:

``
java -cp target/avmf-1.0-jar-with-dependencies.jar org.avmframework.examples.Quadratic
``

``
java -cp target/avmf-1.0-jar-with-dependencies.jar org.avmframework.examples.AllZeros
``

``
java -cp target/avmf-1.0-jar-with-dependencies.jar org.avmframework.examples.String
``

In each of these examples, the AVM is configured to use "Iterated Pattern Search", as initially described by Korel (1990). To use "Geometric" or "Lattice" search instead, as defined by Kempka et al. (2015), provide the option "GeometricSearch" or "LatticeSearch" to one of the above commands as follows:

``
java -cp target/avmf-1.0-jar-with-dependencies.jar org.avmframework.examples.AllZeros GeometricSearch
``

``
java -cp target/avmf-1.0-jar-with-dependencies.jar org.avmframework.examples.AllZeros LatticeSearch
``

## Problems or Praise?

If you have any problems with building, installing, or executing AVM_f_, then please feel free to create an issue
associated with this Git repository using the "Issues" link at the top of this site. The contributors to the repository will do all that they can to resolve your issue and ensure that the entire tool works well in your development environment and for your web site. If you find that AVM_f_ works well, then we also encourage you to "star" and "watch" the project.

Thank you for your interest in the framework.

## References

- Phil McMinn, Chris J. Wright and Gregory M. Kapfhammer.
The Effectiveness of Test Coverage Criteria for Relational Database Schema Integrity Constraints.
ACM Transactions on Software Engineering and Methodology, vol. 25, no. 1, pp. 8:1–8:49, 2015.

- Joseph Kempka, Phil McMinn and Dirk Sudholt.
Design and Analysis of Different Alternating Variable Searches for Search-Based Software Testing.
Theoretical Computer Science, vol. 605, pp. 1–20, 2015.

- Mark Harman and Phil McMinn.
A Theoretical and Empirical Analysis of Evolutionary Testing and Hill Climbing for Structural Test Data Generation.
International Symposium on Software Testing and Analysis (ISSTA 2007), pp. 73–83, 2007.