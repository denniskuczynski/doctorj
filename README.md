# doctorj

A simple server to connect to LibreOffice and convert documents to PDF format.

The name comes from "Document to Renderable (Java)", 
since I needed some software to batch convert Word Documents to PDFs that 
are renderable in the browser.  But it's an easy name to remember because of 
the basketball star.

## Installation

1. Download and install LibreOffice_4.0.0
http://www.libreoffice.org/download/

2. Ensure a 32bit JDK is installed on the machine
http://www.oracle.com/technetwork/java/javase/downloads/index.html

3. Ensure your JAVA_HOME environment variable is set

4. Ensure a 32bit JRuby is installed on the machine
http://jruby.org/

5. Install the buildr gem for JRuby:
    jruby -S gem install buildr

6. Clone this git repository

7. In the repository's root directory, execute: 
    buildr run

8. Use the REST API to submit files for conversion, check on conversion status, and retrieve files.

## Testing

1. In the root directory, execute: buildr test

## Contributing

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Added some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request

## Licensing

LibreOffice is released under the LGPL
http://www.libreoffice.org/download/license/
