# Generated by Buildr 1.4.10, change to your liking


# Version number for this release
VERSION_NUMBER = "1.0.0"
# Group identifier for your projects
GROUP = "doctorj"
COPYRIGHT = ""

$LOAD_PATH.unshift File.expand_path(File.dirname(__FILE__) + '/buildlib')

require 'rbconfig'
target_os = RbConfig::CONFIG['target_os']

# Specify Maven 2.0 remote repositories here, like this:
repositories.remote << "http://repo1.maven.org/maven2"


DROPWIZARD = ['com.yammer.dropwizard:dropwizard-core:jar:0.6.1']
JETTY = ['org.mortbay.jetty:servlet-api:jar:3.0.20100224',
         'org.eclipse.jetty.aggregate:jetty-all:jar:8.1.9.v20130131']
JERSEY = ['asm:asm:jar:3.3.1',
          'com.sun.jersey:jersey-bundle:jar:1.17']
JACKSON = ['com.fasterxml.jackson.core:jackson-core:jar:2.1.4',
           'com.fasterxml.jackson.core:jackson-databind:jar:2.1.4',
           'com.fasterxml.jackson.core:jackson-annotations:jar:2.1.4',
           'com.fasterxml.jackson.jaxrs:jackson-jaxrs-json-provider:jar:2.1.4',
           'com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:jar:2.1.1',
           'com.fasterxml.jackson.datatype:jackson-datatype-joda:jar:2.1.1',
           'com.fasterxml.jackson.datatype:jackson-datatype-guava:jar:2.1.1']
METRICS = ['com.yammer.metrics:metrics-core:jar:2.2.0',
           'com.yammer.metrics:metrics-annotation:jar:2.2.0',
           'com.yammer.metrics:metrics-servlet:jar:2.2.0',
           'com.yammer.metrics:metrics-jetty:jar:2.2.0',
           'com.yammer.metrics:metrics-jersey:jar:2.2.0',
           'com.yammer.metrics:metrics-logback:jar:2.2.0']
GUAVA = ['com.google.guava:guava:jar:14.0']
JODA = ['joda-time:joda-time:jar:2.1']
ARGPARSE4J = ['net.sourceforge.argparse4j:argparse4j:jar:0.4.0']
LOGBACK = ['org.slf4j:slf4j-api:jar:1.7.2',
           'org.slf4j:jul-to-slf4j:jar:1.7.2',
           'org.slf4j:log4j-over-slf4j:jar:1.7.2',
           'ch.qos.logback:logback-core:jar:1.0.9',
           'ch.qos.logback:logback-classic:jar:1.0.9',
           'org.jboss.logging:jboss-logging:jar:3.1.2.GA']
VALIDATION = ['javax.validation:validation-api:jar:1.0.0.GA',
              'org.hibernate:hibernate-validator:jar:4.3.1.Final']

task :smoke_test do
  require 'doctorj/test_scripts'

  Doctorj::TestScripts.run_smoke_test
end

task :load_test do
  require 'doctorj/test_scripts'

  document_count = 1000
  Doctorj::TestScripts.run_load_test document_count
end

desc "The Doctorj project"
define "doctorj" do
  project.version = VERSION_NUMBER
  project.group = GROUP
  manifest["Implementation-Vendor"] = COPYRIGHT

  if target_os =~ /darwin/
    ure_path = '/Applications/LibreOffice.app/Contents/ure-link/share/java'
    uno_path = '/Applications/LibreOffice.app/Contents/MacOS'
    java_args = ["-Xms512m", "-Xmx512m", "-Ddoctorj.converterClass=doctorj.DummyDocumentConverter"]
  elsif target_os =~ /mswin32/
    ure_path = 'C:/Program Files (x86)/LibreOffice 4.0/URE/java'
    uno_path = 'C:/Program Files (x86)/LibreOffice 4.0/program'
    java_args = ["-d32", "-Xms512m", "-Xmx512m", "-Dcom.sun.star.lib.loader.unopath=\"#{uno_path}\"", "-Ddoctorj.converterClass=doctorj.LibreOfficeDocumentConverter"]
  end

  compile.with Dir["#{ure_path}/*.jar"]
  compile.with "#{uno_path}/classes/unoil.jar"
  compile.with DROPWIZARD
  compile.with JETTY
  compile.with JERSEY
  compile.with JACKSON
  compile.with METRICS
  compile.with GUAVA
  compile.with JODA
  compile.with LOGBACK
  compile.with ARGPARSE4J
  compile.with VALIDATION
  
  test.compile.with Dir["#{ure_path}/*.jar"]
  test.compile.with "#{uno_path}/classes/unoil.jar"
  test.compile.with DROPWIZARD
  test.compile.with JETTY
  test.compile.with JERSEY
  test.compile.with JACKSON
  test.compile.with METRICS
  test.compile.with GUAVA
  test.compile.with JODA
  test.compile.with LOGBACK
  test.compile.with ARGPARSE4J
  test.compile.with VALIDATION
    
  package(:jar)
  
  run.using :main => ["doctorj.DoctorjService", "server", "doctorj.yml"],
            :java_args => java_args
end
