# Welcome to the AVMFramework (AVM<i>f</i>)

AVM<i>f</i> is a framework and Java implementation of the Alternating Variable Method (AVM), a heuristic local search algorithm that has been applied to the automation of several important software engineering problems, such as test data generation.

AVM<i>f</i> was designed to enable researchers and practitioners understand the algorithms behind the AVM and use it in their projects, and is freely available for use under the MIT licence. More information can be found on the framework's website: <http://avmframework.org>

## Downloading, Installing and Building

To download AVM<i>f</i> you will need to clone the AVM<i>f</i> project repository using either a graphical Git client or by running the following command at the prompt of your terminal window:

`git clone https://github.com/AVMf/avmf.git`

Then, to run the examples provided with AVM<i>f</i>, you will need to build AVM<i>f</i> from its source code by following the instructions in the next section.

### Building

As AVM<i>f</i> has been implemented as a Maven project using the Java programming language, the easiest method of
generating the executable tool involves importing the project into an integrated development environment (IDE) and
generating the Java archive (JAR) from inside the IDE. Instructions are presented for doing this using two common IDEs:
Eclipse (https://www.eclipse.org/downloads/) and IntelliJ (https://www.jetbrains.com/idea/download/). However, if you
would prefer to build the project using the command line in an appropriate terminal emulator, then instructions to do so
are also provided.

AVM<i>f</i> has been implemented to run using Java Development Kit (JDK) 7 or 8, which can be downloaded from
http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html and
http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html, respectively. Instead of
downloading the JDK from one of the aforementioned web sites, you can also use your operating system's package manager
to install it correctly. After downloading and installing the JDK, you are also likely to have to set Java 1.7 (or, Java
1.8) as the chosen Java Development Kit for the AVM<i>f</i> project. Please follow the instructions provided by either
your operating system or your integrated development environments to accomplish this task.

#### Building with Eclipse

1. Select 'File' &rarr; 'Import'.
2. From the project options, select 'Maven' &rarr; 'Existing Maven Projects'.
3. Select the root directory of your downloaded copy of AVM<i>f</i>.
4. Click 'Finish' to complete the import.
5. To generate the JAR file, select 'Run' &rarr; 'Run As' &rarr; 'maven install'.
6. A JAR file called `avmf-1.0-jar-with-dependencies.jar` should have been created in the `target` directory of AVM<i>f</i>'s main directory; if this JAR file does not exist, then the installation with Eclipse failed and you will not yet be able to use AVM<i>f</i>. Please try these steps again or, alternatively, try another IDE or the command-line-based approach.

#### Building with IntelliJ

1. Select 'File' &rarr; 'Open'.
2. Navigate to the root directory of your installation of AVM<i>f</i>.
3. Select the 'pom.xml' file and click 'Finish'.
4. Open the Maven Projects toolbar using 'View' &rarr; 'Tool Windows' &rarr; 'Maven Projects'.
5. Select the AVM<i>f</i> project and click 'package'.
6. A JAR file called `avmf-1.0-jar-with-dependencies.jar` should have been created in the `target` directory of AVM<i>f</i>'s main directory; if this JAR file does not exist, then the installation with IntelliJ failed and you will not yet be able to use AVM<i>f</i>. Please try these steps again or, alternatively, try another IDE or the command-line-based approach.

#### Building at the Command Line

If you wish to build the AVMf tool from the command line, then you will first need to install Maven on your workstation. If you have already installed Maven, then please go directly to the next section. Otherwise, follow the installation guidelines at <https://maven.apache.org/install.html>. Following this:

1. Navigate to the root directory containing of your installation of AVM<i>f</i>.
2. Type the following command to build the tool: `mvn package`
3. Maven will build the project from scratch, downloading all the required dependencies for the project automatically.
6. A JAR file called `avmf-1.0-jar-with-dependencies.jar` should have been created in the `target` directory of AVM<i>f</i>'s main directory; if this JAR file does not exist, then the installation with the command line failed and you will not yet be able to use AVM<i>f</i>. Please try these steps again or, alternatively, try one of the methods that uses an IDE.

## Running the Provided Examples

AVM<i>f</i> includes various examples of the AVM optimizing different problems. `Quadratic` finds the roots of a quadratic equation. `AllZeros` optimizes a vector of initially arbitrary integer values to zeros. `String` optimizes an initially random string to some desired target string.

If you have already installed and built AVM<i>f</i> as detailed in the previous sections, you can run these examples from the command line as follows:

``
java -cp target/avmf-1.0-jar-with-dependencies.jar org.avmframework.examples.Quadratic
``

``
java -cp target/avmf-1.0-jar-with-dependencies.jar org.avmframework.examples.AllZeros
``

``
java -cp target/avmf-1.0-jar-with-dependencies.jar org.avmframework.examples.StringOptimization
``

In each of these examples, the AVM is configured to use "Iterated Pattern Search", as initially described by Korel (1990). To use "Geometric" or "Lattice" search instead, as defined by Kempka et al. (2015), provide the option "GeometricSearch" or "LatticeSearch" to one of the above commands as follows:

``
java -cp target/avmf-1.0-jar-with-dependencies.jar org.avmframework.examples.AllZeros GeometricSearch
``

``
java -cp target/avmf-1.0-jar-with-dependencies.jar org.avmframework.examples.AllZeros LatticeSearch
``

## Problems or Other Comments?

If you have any problems with building, installing, or executing AVM<i>f</i>, then please feel free to create an issue
associated with this Git repository using the "Issues" link at the top of this site. The contributors to the repository will do all that they can to resolve your issue and ensure that the entire tool works well in your development environment and for your web site.

If you find that AVM<i>f</i> works well, then we also encourage you to "star" and "watch" the project. We would also love to know that you are using the framework. Please drop [Phil McMinn](http://philmcminn.staff.shef.ac.uk) an email. Unless you would prefer not to, we will record your contact details to keep you updated with developments regarding the framework.

Thank you for your interest in the framework.

## Acknowlegements
We would like to thank Joseph Kempka and Dirk Sudholt for an initial implementation of Geometric and Lattice search that we used to test and validate the corresponding implementation in AVM<i>f</i> against.
