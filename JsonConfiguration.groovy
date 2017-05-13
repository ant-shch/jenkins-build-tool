import groovy.json.JsonSlurperClassic

def readJsonFromFile(def path) { 
    def configurationFile = new File(path)
    return new JsonSlurperClassic().parseText(configurationFile.text) 
}

def readJsonFromText(def text) { 
    return new JsonSlurperClassic().parseText(text) 
}

return this;
