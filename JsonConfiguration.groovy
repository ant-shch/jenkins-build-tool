import groovy.json.JsonSlurperClassic

   def readJson(def path) { 
         def configurationFile = new File(path)
         return new JsonSlurperClassic().parseText(configurationFile.text) 
       }
}

return [readJson: this.&readJson];
