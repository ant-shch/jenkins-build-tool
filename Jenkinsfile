#!groovy
import groovy.json.JsonSlurperClassic
node {
    def configuration
    def buildStatus = BuildStatus.Ok

    timestamps {
        stage('Checkout') {
            cleanDir(env.WORKSPACE)
            checkoutComponents(env.COMPONENTS)
            configuration = getConfiguration('BuildConfiguration.json')
        }
        
        try {
            stage('Build') {
                for(def component : configuration.components ) {
                    def solution = "${component.name}\\${component.solution}"
                    bat "\"${tool 'nuget'}\" restore $solution"
                    bat "\"${tool 'msbuild'}\" $solution ${component.properties} /p:ProductVersion=1.0.0.${env.BUILD_NUMBER}"
                }
            }
            
            if(configuration.build.tests) {
                stage('Tests') {
                    dir(env.WORKSPACE){
                        bat """${tool 'nunit'} ${getFilePaths(configuration.tests.wildcards).join(' ')} --work=${configuration.reports}"""
                        nunit testResultsPattern: "${configuration.reports}/TestResult.xml"
                    }
                }
            }
            
            if(configuration.build.codeQuality) {
                stage('CodeQuality') {
                  def assemblies = getFilePaths(configuration.codeQuality.fxcop.wildcards)
                  dir(env.WORKSPACE){
                      for(def assembly : assemblies ) { 
                         try{
                          bat """"${tool 'fxcop'}" /f:$assembly /o:${configuration.reports}\\${new File(assembly).name}.fxcop.xml"""
                         } catch(Exception ex) {
                            echo ex.getMessage()
                         }
                      }
                  }
                }
            }
            
            if(configuration.build.archive) {
                stage('Archive') {
                  dir(env.WORKSPACE){
                     for(def archive : configuration.archive ) { 
                       archiveArtifacts artifacts: archive, onlyIfSuccessful: true
                     }
                  }
                }
            }
            
        } catch (ex) {
            buildStatus = BuildStatus.Error;
            echo ex
            exit 1
        } finally {
            if(configuration.build.notifications) {
                stage('Notifications') {
                  def subject = "Build $buildStatus - $JOB_NAME ($BUILD_DISPLAY_NAME)"

                  def nunitTestBody = renderTemplete(
                    configuration.reportsTemplates + 'nunitTestResult.template.html', 
                    getTestReportModel(configuration.reports + '\\TestResult.xml'))

                  def fxCopTestBody = renderTemplete(
                    configuration.reportsTemplates + 'fxCopTestResult.template.html', 
                    getFxCopReporModel(configuration.codeQuality.fxcop.reports))

                  def emailBody = renderTemplete(
                    configuration.reportsTemplates + 'buildresult.template.html', 
                    getBuildCompleteModel(nunitTestBody, fxCopTestBody, buildStatus))  

                  emailext body: emailBody, subject: subject, to: 'khdevnet@gmail.com'
                }
            }
       }
    }
}

def checkoutComponents(components){
    for(def gitUrl : readJsonFromText(components) ) {
        dir(getComponentFolder(gitUrl)) {
            git url: gitUrl
         }
    }
}

def getConfiguration(configurationFileName) {
    def buildConfigurationJsonFile = findFiles(glob: "**/**/$configurationFileName").first()
    return readJSON file: "${buildConfigurationJsonFile.path}"
}

def getComponentFolder(giturl) {
    giturl.replace('.git','').tokenize( '/' ).last()
}

def readJsonFromText(def text) { 
    return new JsonSlurperClassic().parseText(text) 
}

def readJsonFromFile(def path) { 
    def configurationFile = new File(path)
    return new JsonSlurperClassic().parseText(configurationFile.text) 
}

// parse fx cop
def getFxCopReporModel(fxCopReportFileWildCards){
    def reportMap = [:]
    for(def fxCopReportFilePath : getFilePaths(fxCopReportFileWildCards) ) {
        def fxCopReportFile = new File(env.WORKSPACE, fxCopReportFilePath)
        def dllName = fxCopReportFile.name.replace(".fxcop.xml", "");
        def statistic = parseFxCopReportXmlFile(fxCopReportFile)
        echo dllName
        echo statistic
        reportMap.put(dllName, statistic)
    }

    def statisticHtml = '';
    for(def model : reportMap ) {
         statisticHtml+="<li>${model.key}: ${model.value}</li>"
    }
    
    return ["statistic": statisticHtml]
}

def parseFxCopReportXmlFile(fxCopReportFile){
   def errorsCount = 0
   def warningsCount = 0
   def fxCopRootNode = new XmlParser().parse(fxCopReportFile)
   def namespacesNode = getFirstNodeByName(fxCopRootNode.children(), 'Namespaces')
   def namespaceNodes = getAllNodesByName(namespacesNode.children(), 'Namespace');
   
   for(def node : namespaceNodes ) {
       def messagesNode = getFirstNodeByName(node.children(), 'Messages')
       def messageNodes = getAllNodesByName(messagesNode.children(), 'Message')
       for(def messageNode : messageNodes ) {
           def issueNode = getFirstNodeByName(messageNode.children(), 'Issue')
           def issueNodeAttributes = issueNode.attributes()
           def levelAttribute = issueNodeAttributes.get('Level')
           if(levelAttribute != null) {
           if(levelAttribute == 'Warning'){
               warningsCount++
           }
           if(levelAttribute == 'Error'){
               errorsCount++
             }
           }
       }
    }
    
    return "Warnings: ${warningsCount}, Errors: ${errorsCount}"
}

def getFirstNodeByName(nodes, nodeName){
        for(def node : nodes ) {
            if(node.name() == nodeName){
                return node
            }
        }
    }
    
def getAllNodesByName(nodes, nodeName){
        def list = []
        for(def node : nodes ) {
            if(node.name() == nodeName){
               list << node
            }
        }
        return list
    }

def getBuildCompleteModel(nunitResultBody, fxCopResultBody, buildStatus){
    return ["buildResultUrl": "$BUILD_URL", "buildStatus": buildStatus, 
           "buildNumber": "$BUILD_DISPLAY_NAME", "applicationName": "$JOB_NAME",
           "nunitResultBody" : "$nunitResultBody", "fxCopResultBody": "$fxCopResultBody"]
}

def mergeMap(target, map){
    for(def result : map ) { target.put(map.key, map.value) }
    return target
}

def renderTemplete(templateFilePath, model){
    def templateBody =  new File(env.WORKSPACE, templateFilePath).text
    def engine = new groovy.text.SimpleTemplateEngine()
    engine.createTemplate(templateBody).make(model).toString()
}

def getTestReportModel(nunitTestReportXmlFilePath){
    def testXmlRootNode = new XmlParser().parse(new File(env.WORKSPACE, nunitTestReportXmlFilePath))
    def resultNode = findlastNode(testXmlRootNode.children(),'test-suite')
    def result = resultNode.attributes();
    result.put('testResultsUrl', env.JOB_URL + env.BUILD_ID + '/testReport')
    return result
}

def findlastNode(list, nodeName){
    for(def element : list.reverse() ) { 
       if(element.name()==nodeName){
           return element
       }
    }
}

def getFilePaths(wildcards){
    def files = []
    for(def wildcard : wildcards ) { 
        files.addAll(findFiles(glob: wildcard))
    }
    
    def filePaths = []
    for(def file : files ) { filePaths << file.path }
    return filePaths
}

def getFiles(wildcards, rootDir=''){
    def files = []
    for(def wildcard : wildcards ) { 
        files.addAll(findFiles(glob: wildcard))
    }
    
    def names = []
    def prefix = rootDir == '' ? '' : rootDir + '\\'
    for(def file : files ) { names << prefix + file.name }
    return names
}

def cleanDir(dirPath) {
     def dir = new File(dirPath)
     if (dir.exists()) dir.deleteDir()
     if (!dir.exists()) dir.mkdirs()
}

def makeDir(dirPath) {
     def dir = new File(dirPath)
     if (!dir.exists()) dir.mkdirs()
}

def removeDir(dirPath) {
     def dir = new File(dirPath)
     if (dir.exists()) dir.deleteDir()
}
def log(message){
    println message
} 

class BuildStatus {
    static String Ok = 'Ok'
    static String Error = 'Error'
    static String Warning = 'Warning'
}
