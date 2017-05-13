import groovy.json.JsonSlurperClassic

class JsonConfiguration {
   def read(def path) { 
         def configurationFile = new File(path)
         return new JsonSlurper().parseText(configurationFile.text) 
       }
}

return new JsonConfiguration();
