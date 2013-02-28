# doctorj

A simple server to connect to LibreOffice and convert documents to PDF format.

The name comes from "Document to Renderable (Java)", 
since I needed some software to batch convert Word Documents to PDFs that 
are renderable in the browser.  But it's an easy name to remember because of 
the basketball star.

![Screenshot](http://s18.postimage.org/6ambx23hl/doctorj.png)

## Installation

1. Download and install LibreOffice_4.0.0
  * http://www.libreoffice.org/download/


2. Ensure a 32bit JDK is installed on the machine
  * http://www.oracle.com/technetwork/java/javase/downloads/index.html


3. Ensure your JAVA_HOME environment variable is set and ensure %JAVA_HOME%\bin is in the PATH environment variable.


4. Ensure a 32bit JRuby is installed on the machine
  * http://jruby.org/


5. Install the buildr gem for JRuby:
  * jruby -S gem install buildr


6. If on Windows, ensure the Windows Error Reporting\DontShowUI registry setting is set to 1
  * This will prevent LibreOffice crashes from hanging the doctorj process.
  * http://social.technet.microsoft.com/Forums/en-US/winservergen/thread/8e7a7f48-a65e-4cd5-a55a-a62e4f7604cc/


7. If any firewall is running, ensure that TCP port 8080 is allowed for inbound connections.


8. Clone this git repository


9. In the repository's root directory, execute: 
  * buildr run


10. Use the REST API to submit files for conversion, check on conversion status, and retrieve files.


## Usage

You can see an example ruby client in the test script:  https://github.com/denniskuczynski/doctorj/tree/master/buildlib/doctorj

## Testing

1. In the root directory, execute: buildr test
2. There are also two Ruby test scripts
  * buildr smoke_test
  * buildr load_test

## Contributing

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Added some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request

## Licensing

LibreOffice is released under the LGPL
http://www.libreoffice.org/download/license/

## Screenshot

![Screenshot](http://s23.postimage.org/5naezxp8r/Screen_Shot_2013_02_28_at_2_13_43_PM.png)
