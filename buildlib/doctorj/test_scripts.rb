require 'doctorj/client'

module Doctorj
  module TestScripts

    def self.run_smoke_test
      client = Doctorj::Client.new('127.0.0.1', 8080)
      id = client.add_conversion(get_example_document_contents)
      process_conversion_request(client, id)      
    end

    def self.run_load_test(document_count)
      client = Doctorj::Client.new('127.0.0.1', 8080)
      ids = []
      for i in 1..document_count
        ids << client.add_conversion(get_example_document_contents)
      end
      ids.each do |id|
        process_conversion_request(client, id)
      end
    end

    def self.get_example_document_contents
      file_path = File.expand_path(File.dirname(__FILE__) + 
        '/../../data/example_docs/test1.odt')
      contents = open(file_path, "rb") {|io| io.read }
      contents
    end

    def self.process_conversion_request(client, id)
      puts "Processing tracking id: #{id}"
      processed = false
      while not processed
        status = client.get_conversion_status(id)
        puts "Current status of request #{id}: #{status}"
        if status == 'Complete'
          break
        elsif status == 'InProcess'
          sleep 1
          next
        end
      end
      file = client.get_conversion_file(id)
      if not file
        puts "ERROR: Unable to retrieve file for request #{id}!"
      else
        puts "Retrieved: #{file.size} bytes for request #{id}"
        puts "Cleaning up data for request #{id}"
        status = client.delete_conversion(id)
        status = client.get_conversion_status(id)
        if status != 'NotFound'
          puts "ERROR: request #{id} not cleaned up!"     
        else
          puts "Request #{id} cleaned up successfuly"
        end
      end
    end

  end
end