require "net/http"
require "uri"

module Doctorj
  # Client Abstraction for the Doctorj PDF Converter API
  class Client

    def initialize(host, port)
      @host = host
      @port = port
    end

    def get_conversion_status(id)
      uri = URI.parse("http://#{@host}:#{@port}/convert/#{id}")
      http = Net::HTTP.new(uri.host, uri.port)
      request = Net::HTTP::Get.new(uri.request_uri)
      response = http.request(request)
      return response.body
    end

    def add_conversion(contents)
      uri = URI.parse("http://#{@host}:#{@port}/convert")
      http = Net::HTTP.new(uri.host, uri.port)
      request = Net::HTTP::Post.new(uri.request_uri)
      request["Content-Type"] = "application/octet-stream"
      request.body = contents
      response = http.request(request)
      return response.body
    end

    def get_conversion_file(id)
      uri = URI.parse("http://#{@host}:#{@port}/convert/#{id}/file")
      http = Net::HTTP.new(uri.host, uri.port)
      request = Net::HTTP::Get.new(uri.request_uri)
      response = http.request(request)
      return response.body
    end

    def delete_conversion(id)
      uri = URI.parse("http://#{@host}:#{@port}/convert/#{id}")
      http = Net::HTTP.new(uri.host, uri.port)
      request = Net::HTTP::Delete.new(uri.request_uri)
      response = http.request(request)
      return response.body
    end

  end
end