import groovy.json.JsonSlurper

class JsonConfiguration {
   def Read(def path) { 
         def configurationFile = new File(path)
         return new JsonSlurper().parseText(configurationFile.text) 
       }
}

return new JsonConfiguration();
